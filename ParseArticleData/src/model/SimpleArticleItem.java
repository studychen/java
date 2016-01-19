package model;

import java.util.Arrays;

/**
 * model/SimpleArticleItem.java 
 * 栏目显示新闻列表
 * 所需要的实体类 只包括 index 标题 图片 发布时间 阅读次数
 * 
 * @author tomchen
 *
 */

public class SimpleArticleItem {

	private int index;
	// 图片资源不是必须的
	private String[] imageUrls;
	private String title;
	private String publishDate;
	private int readTimes;

	public SimpleArticleItem(int index, String title, String publishDate, int readTimes) {
		this.index = index;
		this.title = title;
		this.publishDate = publishDate;
		this.readTimes = readTimes;
	}

	public SimpleArticleItem(int index, String[] imageUrls, String title, String publishDate, int readTimes) {
		this.index = index;
		this.imageUrls = imageUrls;
		this.title = title;
		this.publishDate = publishDate;
		this.readTimes = readTimes;
	}

}