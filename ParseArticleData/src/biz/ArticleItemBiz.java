package biz;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

import Util.ColumnType;
import Util.ColumnUrlTool;
import Util.HttpTool;
import Util.ImageTool;
import model.CommonException;
import model.SimpleArticleItem;
import model.ArticleItem;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ArticleItemBiz {

	private static final String ARTICLE_BASE_URL = "http://see.xidian.edu.cn/html/news/";
	// 统计点击次数的 url
	private static final String COUNT_BASE_URL = "http://see.xidian.edu.cn/index.php/news/click/id/";

	/**
	 * 新闻的 url 格式为 http://see.xidian.edu.cn/html/news/7928.html
	 * 
	 * @param currentPage
	 *            某个新闻页面的序号
	 * @return 爬取该页面上的新闻信息，提取相应的信息，存到新闻bean里
	 * @throws CommonException
	 */
	public static ArticleItem parseNewsItem(int currentPage) throws CommonException {
		// 根据后缀的数字，拼接新闻 url
		String urlStr = ARTICLE_BASE_URL + currentPage + ".html";

		String htmlStr = HttpTool.doGet(urlStr);

		Document doc = Jsoup.parse(htmlStr);
		System.out.println(doc);

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
		for (int i = 0; i < imageUrls.length; i++) {
			imageUrls[i] = images.get(i).attr("src");
		}
		return new ArticleItem(currentPage, imageUrls, titleStr, dateStr, sourceStr, readTimes, contentStr);
	}

}
