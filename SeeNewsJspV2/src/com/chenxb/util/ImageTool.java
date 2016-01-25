package com.chenxb.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class ImageTool {
	private static final String IMAGE_BASE = "http://see.xidian.edu.cn";

	// 附件下载的图标，忽略
	private static final String ICON_FILE = "http://rsc.xidian.edu.cn/plus/img/addon.gif";

	public static String convertUrl(int currentPage, String origin) {
		// 图片资源不一定都是在 uploads 文件夹下面
		// 也有可能外链到其他网站的图片/uploads/image/20141120/20141120**.jpg
		if (origin.startsWith("/uploads")) {
			// 相对路径，比如
			// 把图片上传给七牛
			String wholeURl = IMAGE_BASE + origin;

			String imageKey = StringTool.createMD5(origin);

			uploadByUrl(wholeURl, imageKey);
			return imageKey;
			// 上传成功，返回七牛的地址，否则返回原来地址
			// if (uploadByUrl(wholeURl, imageKey)) {
			// return BUCKET_HOST_NAME + imageKey;
			// } else {
			// return IMAGE_BASE + origin;
			// }

		} else if (origin.startsWith(IMAGE_BASE)) {
			// 以绝对路径开头，最前面是网站域名
			// 比如 http://see.xidian.edu.cn/uploads/image/20141120/201411**.png
			String imageKey = StringTool.createMD5(origin);
			uploadByUrl(imageKey, imageKey);
			return imageKey;
		} else if (origin.equals(ICON_FILE)) {
			return origin;
		} else {
			// 这部分 todo，识别其他格式的图片
			// 或者试图访问这个图片，但失败了，则不是完整的 url
			StringBuilder builder = new StringBuilder();
			builder.append("<p>ImageTool.convertUrl() 无法解析图片</p>");
			builder.append("<p>图片 url = " + origin + "</p>");
			MailTool.sendException(builder.toString(), currentPage, MailTool.IMAGE_UNUSUAL);
			return origin;
		}

	}

	/**
	 * 
	 * @param url
	 *            给定图片的 url
	 * @return 将图片上传至七牛，返回七牛上图片的 url
	 * @throws QiniuException
	 */
	private static void uploadByUrl(String originalUrl, String key) {
		FetchRunnable f = new FetchRunnable(originalUrl, key);
		new Thread(f).start();
	}

}

/**
 * 图片上传使用多线程
 * 有问题？上传失败如何回滚？
 * 失败的概率很小，暂时不考虑
 * 或者失败了发邮件通知
 * @author tomchen
 *
 */
class FetchRunnable implements Runnable {
	private static final String ACCESS_KEY = "0JdTNdUAyxP1i4GuAPzb2lF-HpELjf12ZvcQ7f0a"; // 你的access_key
	private static final String SECRET_KEY = "rsorpEMhIC3rYxud9StCH15-jbAOkyPAD51zx_eG"; // 你的secret_key
	private static final String BUCKET_NAME = "seenews"; // 你的secret_key

	private String url;
	private String key;

	public FetchRunnable(String url, String key) {
		this.url = url;
		this.key = key;
	}

	@Override
	public void run() {
		// 获取到 Access Key 和 Secret Key 之后，您可以按照如下方式进行密钥配置
		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		// 获取空间管理器
		BucketManager bucketManager = new BucketManager(auth);
		try {
			// 要求url可公网正常访问BucketManager.fetch(url, bucketName, key);
			// @param url 网络上一个资源文件的URL
			// @param bucketName 空间名称
			// @param key 空间内文件的key[唯一的]
			DefaultPutRet putret = bucketManager.fetch(url, BUCKET_NAME, key);
		} catch (QiniuException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));

			StringBuilder builder = new StringBuilder(errors.toString());
			builder.append("<p> 七牛fetch(url, BUCKET_NAME, key)发生异常！</p>");
			MailTool.sendException(builder.toString(), 000, MailTool.ARTICLE_ITEM_BIZ);

			// to do 失败了邮件通知
		}
	}
}
