package model;

import java.util.Arrays;

/**
 * model/ItemNews.java 
 * 新闻详情页面用到的完整实体类
 * 新闻实体类 包括标题，发布日期，阅读次数，新闻主体内容等
 * 
 * @author tomchen
 *
 */

public class ArticleItem {

	private int index;
	private String[] imageUrls;
	private String title;
	private String publishDate;
	private String source;
	private int readTimes;
	private String body;

	public ArticleItem(int index, String[] imageUrls, String title, String publishDate, String source, int readTimes,
			String body) {
		this.index = index;
		this.imageUrls = imageUrls;
		this.title = title;
		this.publishDate = publishDate;
		this.source = source;
		this.readTimes = readTimes;
		this.body = body;
	}

	@Override
	public String toString() {
		return "ArticleItem [index=" + index + ",\n imageUrls=" + Arrays.toString(imageUrls) + ",\n title=" + title
				+ ",\n publishDate=" + publishDate + ",\n source=" + source + ",\n readTimes=" + readTimes + ",\n body=" + body
				+ "]";
	}

	
}