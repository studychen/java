package com.chenxb.news;

import javax.script.ScriptException;

import com.chenxb.bean.NewsDetailItem;

public class Test {
	public static void main(String arg[]) throws ScriptException {

		NewsList list = NewsList.loadNewsListItem("0", true);
		System.out.println(list.getList());
		
//		NewsDetailItem detail = NewsDetail.loadDetail("7000", true);
//		String title = detail.getTitle();
//		String time = detail.getDate();
//		System.out.println(title);
//		
//		System.out.println(time);
//		
//		System.out.println(detail);
		
	}

}
