package com.chenxb.news;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.chenxb.common.StreamTool;
import com.chenxb.model.NewsDetailItem;

public class NewsDetail {
	
	private static final Pattern pattern = Pattern.compile("\\d+"); 
	
	public static NewsDetailItem loadDetail(String num, boolean flag) {
		String newsDetailUrl;
		if(flag) {
			newsDetailUrl = "http://see.xidian.edu.cn/html/news/"+ num +".html";
		} else {
			newsDetailUrl = num;
		}
		
		Document document;
		try {
			document = Jsoup.connect(newsDetailUrl).timeout(100000).get();
			
			Element element = document.getElementById("article_title");
			
			String title = element.text();

			 element = document.getElementById("article_detail");
			
			String date = element.select("span").get(0).text(); 
			
			int readCount = getReadCount(num ,true);
			
			element = document.getElementById("article_content");
			StringBuilder contentStr = new StringBuilder();
			
			contentStr.append( element.html().toString() );
//			//有p标签可能也有span标签
//			//有span标签一定没有p标签
//			if (! element.select("p").text().isEmpty() &&
//					element.select("p").text() != null) {
//				Elements elements = element.select("p");
//				for (Element ele : elements) {
//					if (ele.toString().contains("<img") || ele.toString().contains("<a")) {
//						contentStr.append(ele.toString());
//					}  else {
//						contentStr.append("<p>" + ele.text().replaceAll(
//								Jsoup.parse("&nbsp;").text(), "") +"</p>");
//					}
//					
//				}
//			} else if( ! element.select("span").text().isEmpty() &&
//					element.select("span").text() != null ) {
//				Elements elements = element.select("span");
//				for (Element ele : elements) {
//					if (ele.toString().contains("<img") || ele.toString().contains("<a")) {
//						contentStr.append(ele.toString());
//					}  else {
//					contentStr.append("<p>" + ele.text().replaceAll(
//							Jsoup.parse("&nbsp;").text(), "") +"</p>");
//					}
//				}
//			} else {
//				contentStr.append("<p>" + element.html().toString() + "</p>");
//			}
			
			String body = contentStr.toString();
			body = body.replaceAll(
					"(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
			body = body.replaceAll(
					"(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
			body = body.replaceAll(
					"src=\"(?!http)", "src=\"http://see.xidian.edu.cn");

			// 添加点击图片放大支持
			body = body.replaceAll("(<img[^>]+src=\")(\\S+)\"",
					"$1$2\" onClick=\"javascript:mWebViewImage" +
					"Listener.onImageClick('$2')\"");
			
			body += "<div style='margin-bottom: 10px'/>";
			
			return new NewsDetailItem(title, date, readCount, body);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace(System.err);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(System.err);
		}
		return null;

	}
	
	//找出阅读次数，get放松
	private static int getReadCount(String num, boolean flag) throws Exception {
		String scriptUrl;
		if(flag) {
			scriptUrl = "http://see.xidian.edu.cn/index.php/news/click/id/" + num;
		} else {
			Matcher matcher = pattern.matcher(num);  
			String tempNum = null ;  
	        while (matcher.find()) {  
	        	tempNum =  matcher.group(0);
	        }
	        scriptUrl = "http://see.xidian.edu.cn/index.php/news/click/id/" + tempNum ;
		}

		URL url = new URL(scriptUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.addRequestProperty(
                      "User-Agent",
                      "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36");
		con.setConnectTimeout(5000);
		con.setRequestMethod("GET");
		
		if (con.getResponseCode() == 200){
			InputStream inputStr = con.getInputStream();
			String info = new String(StreamTool.read(inputStr), "UTF-8");
			Matcher matcher = pattern.matcher(info);  
	        while (matcher.find()) {  
	            num =  matcher.group(0); 
	        }
	        return Integer.valueOf(num);
		}
		return 233; //return 一个假的数值 233
	}
	
}
