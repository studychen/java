package com.chenxb.news;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.chenxb.model.*;

public class NewsList implements Serializable{
	
	private static final long serialVersionUID = -2587984874709755198L;
	private List<NewsListItem> list = new ArrayList<NewsListItem>();
	private static final int CACHE_TIME = 60*60000;//缓存失效时间
	
	//读取新闻列表
	public List<NewsListItem> getList() {
		return list;
	}
	
	public static NewsList loadNewsListItem(String num, boolean flag) {
		NewsList newsList = new NewsList();
		String newsListUrl;
		if(flag) {
			if(num.equals("0")) {
				newsListUrl = "http://see.xidian.edu.cn/index.php/index/more";
			} else {
				newsListUrl = "http://see.xidian.edu.cn/html/category/"+ num +".html";
			}
		} else {
			newsListUrl = num;
		}
		
		
		try {
			Document document = Jsoup.connect(newsListUrl).timeout(100000).get();
			Element element = document.getElementById("list_area");
			ListIterator<Element> eleIte =element.select("li").listIterator();
//			List<NewsListItem> newsList = new ArrayList<NewsListItem> ();
			while (eleIte.hasNext()) {
				Element ele = eleIte.next();
				String url = ele.select("a[href]").get(0).attr("href");
				String date = ele.getElementsByClass("left_date").text();
//				String newdate = date.substring(1, date.length()-1);
				String title = ele.select("a[href]").get(0).text().replace(date, "");
				String click = ele.getElementsByClass("news_date").text();
				
				
				NewsListItem tempNewsList = new NewsListItem(url, date, title, click);
				newsList.list.add(tempNewsList);
			}
			return newsList;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(System.err);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "NewsList [list=" + list + "]";
	}

}
