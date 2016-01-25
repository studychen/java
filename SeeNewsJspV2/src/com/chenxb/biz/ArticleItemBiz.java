package com.chenxb.biz;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.chenxb.model.ArticleItem;
import com.chenxb.util.Constant;
import com.chenxb.util.HttpTool;
import com.chenxb.util.ImageTool;
import com.chenxb.util.MailTool;
import com.qiniu.common.QiniuException;

public class ArticleItemBiz {

	// 统计点击次数的 url
	private static final String COUNT_BASE_URL = "http://see.xidian.edu.cn/index.php/news/click/id/";

	private static final String SOURCE_PREFIX = "来源:";

	/**
	 * 新闻的 url 格式为 http://see.xidian.edu.cn/html/news/7928.html
	 * 
	 * @param currentPage
	 *            某个新闻页面的序号
	 * @return 爬取该页面上的新闻信息，提取相应的信息，存到新闻bean里。如果没有爬取到新闻返回null
	 * @throws Exception
	 */
	public static ArticleItem parseNewsItem(int currentPage) throws Exception {
		// 根据后缀的数字，拼接新闻 url
		String urlStr = Constant.ARTICLE_BASE_URL + currentPage + ".html";

		// 利用get请求获取字符串再解析会有小部分乱码
		// String htmlStr = HttpTool.doGet(urlStr);
		// Document doc = Jsoup.parse(htmlStr);
		// try {
		Document doc = Jsoup.connect(urlStr).timeout(10000).get();
		// 去掉jsoup对html字符串加的"\n"，方便json字符串返回
		doc.outputSettings().prettyPrint(false);

		Element articleEle = doc.getElementById("article");
		// 标题
		Element titleEle = articleEle.getElementById("article_title");
		String titleStr = titleEle.text();

		// article_detail包括了 2016-01-15 来源: 浏览次数:177
		Element detailEle = articleEle.getElementById("article_detail");
		Elements details = detailEle.getElementsByTag("span");

		// 发布时间
		String dateStr = details.get(0).text();

		// 新闻来源
		String sourceStr = details.get(1).text();

		// 去掉"来源:"
		if (SOURCE_PREFIX.equals(sourceStr.trim())) {
			sourceStr = "SeeNews";
		} else {
			sourceStr = sourceStr.substring(3).trim();
		}

		// 访问这个新闻页面，浏览次数会+1，次数是 JS 渲染的
		String jsStr = HttpTool.doGet(COUNT_BASE_URL + currentPage);
		int readTimes = Integer.parseInt(jsStr.replaceAll("\\D+", ""));
		// 或者使用下面这个正则方法
		// String readTimesStr = jsStr.replaceAll("[^0-9]", "");

		Element contentEle = articleEle.getElementById("article_content");
		// 新闻主体内容

		String contentStr = contentEle.toString();

		// 如果用 text()方法，新闻主体内容的 html 标签会丢失
		// 为了在 Android 上用 WebView 显示 html，用toString()
		// String contentStr = contentEle.text();
		Elements images = contentEle.getElementsByTag("img");
		String[] imageUrls = new String[images.size()];

		// 图片上传到七牛
		// 将body中的图片地址替换为七牛的地址
		for (int i = 0; i < imageUrls.length; i++) {
			String origin = images.get(i).attr("src");
			imageUrls[i] = ImageTool.convertUrl(currentPage, origin);
			if (!origin.equals(imageUrls[i])) {
				// 不相等，才替换为七牛的url
				contentStr = contentStr.replace(origin, Constant.BUCKET_HOST_NAME + imageUrls[i]);
			}
		}
		return new ArticleItem(currentPage, imageUrls, titleStr, dateStr, readTimes, sourceStr, contentStr);
		// }
		// } catch (Exception e) {
		// // 将异常的栈轨迹转化为字符串发给邮箱
		// StringWriter errors = new StringWriter();
		// e.printStackTrace(new PrintWriter(errors));
		//
		// StringBuilder builder = new StringBuilder(errors.toString());
		// builder.append("<p>ArticleItem.parseNewsItem() 发生异常！</p>");
		// MailTool.sendException(builder.toString(), currentPage,
		// MailTool.ARTICLE_ITEM_BIZ);
		// }
		// return null;
	}

}
