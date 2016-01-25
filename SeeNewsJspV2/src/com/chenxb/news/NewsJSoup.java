package com.chenxb.news;

import java.io.IOException;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



/**
 * @name Jsoup爬虫解析新闻
 * @description 线程中根据不同的标记决定解析一条新闻还是新闻列表
 * @author 樊俊彬
 * @date 2014-4-29
 * 
 */
public class NewsJSoup implements Runnable {

	public Document document;
	public Elements elements;
	public Element element;
	public String newsDetailUrl;
	public String newsListUrl;
	public int type;

	public NewsJSoup(String url, int type) {
		this.type = type;

		if (type == 1) {
			this.newsListUrl = url;
		} else {
			this.newsDetailUrl = url;
		}
		new Thread(this, "读取这条新闻详细线程").start();
	}

	public void run() {
		if (type == 1) {
			loadNewsList();
		} else {
			try {
				loadNewsDetail();
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 解析宁大综合新闻列表
	 */
	private void loadNewsList() {
		try {
			
			document = Jsoup.connect(newsListUrl).timeout(100000).get();
			// 新闻标题的源码和解析
			element = document.getElementById("list_area");
//			System.out.println("解析到的新闻发布链接：" + element.text());
			ListIterator<Element> eleIte =element.select("li").listIterator();
			while (eleIte.hasNext()) {
				Element ele = eleIte.next();
				String url = ele.select("a[href]").get(0).attr("href");
				System.out.println("解析到的新闻发布链接：" + url);
				
				String date = ele.getElementsByClass("left_date").text();
				String newdate = date.substring(1, date.length()-1);
				System.out.println("解析到的新闻发布时间：" + newdate);
				
				
				String title = ele.select("a[href]").get(0).text().replace(date, "");
				System.out.println("解析到的新闻发布标题：" + title);
				
				String read = ele.getElementsByClass("news_date").text();
				System.out.println("解析到的新闻点击次数：" + read);
			}
			
//			for (Element ele : elements ) {
//				String url = ele.getElementsByAttribute("href").text();
//				System.out.println("解析到的新闻发布链接：" + url);
//				String date = ele.getElementsByClass("left_date").text();
//				System.out.println("解析到的新闻发布时间：" + date);
//
//			}
////			System.out.println(element.toString());
//			String title = element.text();

//			// 新闻发布时间的源码和解析
//			element = document.getElementById("article_detail");
////			System.out.println(element.toString());
//			String pubDate = element.select("span").get(0).text(); 
//			Log.i(tag,  "解析到的新闻发布时间：" + element.select("span").get(0).text());
//			
//			int readCount = getReadCount();
//			// 新闻浏览次数的源码和解析
//			Log.i(tag,  "解析到的新闻浏览次数：" + readCount);
//			document = Jsoup.connect(newsListUrl).timeout(100000).get();
//			// 新闻源码中的表格共有28个，均没有id或class等标记，所以只能以穷举的方式来搜索目标表格（width=640）
//			for (int i = 0; i < 28; i++) {
//				Element element = document.select("table").get(i);
//				if (element.attr("width").toString().equals("640")) {
//					System.out.println(i);
//				}
//			}
//			// 目标表格width=240是源码中的第24个表格,加载它
//			Element element = document.select("table").get(24);
//			// <td width=500>是新闻的标题和url，<td width=80>是新闻的发表时间
//			Elements elements = element.getElementsByAttributeValueContaining(
//					"width", "500").select("a");
//			for (Element ele : elements) {
//
//				System.out.println("新闻标题：" + ele.attr("title"));
//				System.out.println("对应的URL：" + ele.attr("href"));
//			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("爬取失败");
			e.printStackTrace();
		}
	}

	/**
	 * 解析一条新闻的详细
	 * @throws ScriptException 
	 */
	private void loadNewsDetail() throws ScriptException {
		try {
			document = Jsoup.connect(newsDetailUrl).timeout(100000).get();
			// 新闻标题的源码和解析
			element = document.getElementById("article_title");
//			System.out.println(element.toString());
			System.out.println("\n" + "解析到的新闻标题：" + "\n"
					+ element.text() + "\n");

			// 新闻发布时间的源码和解析
			element = document.getElementById("article_detail");
//			System.out.println(element.toString());
			System.out.println("\n" + "解析到的新闻发布时间：" + "\n"
					+ element.select("span").get(0).text() + "\n");
			
			// 新闻浏览次数的源码和解析
			ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            Document newDOc = Jsoup.connect("http://see.xidian.edu.cn/index.php/news/click/id/7027").
            		timeout(100000).get();
         String t =  engine.eval(newDOc.html().toString()).toString();
         
         Pattern pattern = Pattern.compile("\\d+");  
         Matcher matcher = pattern.matcher(t);  
         while (matcher.find()) {  
             String num =  matcher.group(0);  
             System.out.println("次数：" + num);
         }  
       
            
//            System.out.println("次数：" + t);
			
//			System.out.println("\n" + "解析到的新闻发布次数：" + "\n"
//					+ element.select("span").get(2)+ "\n");

			// 新闻正文的源码和解析
         //注意将空格转换
			element = document.getElementById("article_content");
//			for (Element ele : elements) {
//				
//			}
			System.out.println("sss11" + (element.toString().contains("</p>") || element.toString().contains("</span>")));
			System.out.println("sss22" +element.select("p").text().toString());
			System.out.println("sss33" +element.select("p").text());
			
			//http://see.xidian.edu.cn/html/news/7088.html 特殊
			
			if (!element.select("p").text().toString().isEmpty() &&
					element.select("p").text() != null) {
				elements = element.select("p");
				System.out.println("\n" + "解析到的新闻正文pppp：");
				for (Element ele : elements) {
					if (ele.toString().contains("<img")) {
						System.out.println(ele.toString());
					} else {
						System.out.println(ele.text().replaceAll(
								Jsoup.parse("&nbsp;").text(), " "));
					}
					
				}
				
			} else if(!element.select("span").text().toString().isEmpty() &&
					element.select("span").text() != null){
				elements = element.select("span");
				System.out.println("\n" + "解析到的新闻正文span：");
				for (Element ele : elements) {
					System.out.println(ele.text().replaceAll(
							Jsoup.parse("&nbsp;").text(), " "));
				}
			} else {
				System.out.println("\n" + "解析到的新闻正文divdiv：");
				String temp = element.html().toString();
				
				System.out.println(temp);
			}
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("爬取失败");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// 宁大主页新闻详情
		String newsDetailUrl_1 = "http://see.xidian.edu.cn/html/news/7089.html";
		String newsDetailUrl_2 = "http://see.xidian.edu.cn/html/news/7083.html";
		String newsDetailUrl_3 = "http://see.xidian.edu.cn/html/news/7026.html";
		
		// 宁大主页综合新闻列表
		String newsListUrl_1 = "http://see.xidian.edu.cn/index.php/index/more";
		String newsListUrl_2 = "http://see.xidian.edu.cn/html/category/2.html";
		String newsListUrl_3 = "http://see.xidian.edu.cn/html/category/3.html";
		
		NewsJSoup jsSoupMain = new NewsJSoup(newsListUrl_1, 1);
		NewsJSoup jsSoupMain2 = new NewsJSoup(newsDetailUrl_2, 2);
	}

}

