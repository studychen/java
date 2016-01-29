package biz;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import model.CommonException;
import model.SimpleArticleItem;
import util.ColumnType;
import util.ColumnUrlTool;
import util.HttpTool;

public class ColumnBiz {

	private static Pattern regexCountPage = Pattern.compile("\\d+/(\\d+)");

	/**
	 * 爬取本科教学、研究生、就业招聘等栏目，
	 * 
	 * @return 返回新闻列表
	 */
	public static List<SimpleArticleItem> parseColumn(int type, int current) {

		return null;
	}

	/**
	 * 根据栏目类型获取 本栏目共有几页
	 * 
	 * @param type
	 *            栏目类型
	 * @return 总页数
	 * @throws CommonException
	 */
	public static int getTotalPage(int type) throws CommonException {
		// 最新消息栏目特殊，只有1页，没有下一页
		if (type == ColumnType.LATEST)
			return 1;
		String columnUrl = ColumnUrlTool.generateUrl(type, 1);

		String htmlStr = HttpTool.doGet(columnUrl);

		Document doc = Jsoup.parse(htmlStr);
		// 正则匹配 1262 条记录 1/26 页
		Element page = doc.getElementById("div_page");

		Matcher matcher = regexCountPage.matcher(page.text());
		if (matcher.find()) {
			return Integer.parseInt(matcher.group(1));
		} else {
			// 根据经验值，一个栏目至少有5页
			return 5;
		}
	}

}
