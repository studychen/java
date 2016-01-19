package Util;

import java.io.File;
import java.util.Arrays;

import javax.crypto.Mac;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

public class ImageTool {
	private static final String IMAGE_BASE = "http://see.xidian.edu.cn";
	public static final String ACCESS_KEY = "0JdTNdUAyxP1i4GuAPzb2lF-HpELjf12ZvcQ7f0a"; // 你的access_key
	public static final String SECRET_KEY = "rsorpEMhIC3rYxud9StCH15-jbAOkyPAD51zx_eG"; // 你的secret_key
	public static final String BUCKET_NAME = "seenews"; // 你的secret_key

	private static final String BUCKET_HOST_NAME = "7xq7ik.com1.z0.glb.clouddn.com";

	public static String convertUrl(String origin) {
		// 图片资源不一定都是在 uploads 文件夹下面
		// 也有可能外链到其他网站的图片
		if (origin.startsWith("/uploads")) {
			return IMAGE_BASE + origin;
		} else {
			// 这部分 todo，识别其他格式的图片
			// 或者试图访问这个图片，但失败了，则不是完整的 url
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
public String uploadByUrl(String originalUrl) {
	// 获取到 Access Key 和 Secret Key 之后，您可以按照如下方式进行密钥配置
	Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

	// 获取空间管理器
	BucketManager bucketManager = new BucketManager(auth);
	String newUrl = originalUrl;
	try {

		// 要求url可公网正常访问BucketManager.fetch(url, bucketName, key);
		// @param url 网络上一个资源文件的URL
		// @param bucketName 空间名称
		// @param key 空间内文件的key[唯一的]
		String imageKey = "testimage";
		DefaultPutRet putret = bucketManager.fetch(originalUrl, BUCKET_NAME, imageKey);
		newUrl = BUCKET_HOST_NAME + "/" + imageKey;
		System.out.println(newUrl);
		System.out.println("succeed upload image");
	} catch (QiniuException e1) {
		e1.printStackTrace();
	}

	return null;
}

	// private static String getUpToken(Auth auth) {
	// return auth.uploadToken(BUCKET_NAME, "imageOne");
	// }

	public static void main(String[] args) throws QiniuException {
		new ImageTool().uploadByUrl("http://img.blog.csdn.net/20160119111734404");
	}
}
