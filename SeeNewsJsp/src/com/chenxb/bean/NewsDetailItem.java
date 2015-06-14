package com.chenxb.bean;


import java.io.Serializable;

public class NewsDetailItem implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1083501967358523607L;
	private String title;
	private String date;
	private int readCount;
	private String body;
	
	public NewsDetailItem(String title,  String date, int readCount, String body) {
		this.title = title;
		this.date = date;
		this.readCount = readCount;
		this.body = body;
	}
	
	@Override
	public String toString() {
		return "News [title=" + title + ",\n date=" + date + ",\n readCount="
				+ readCount + ",\n body=" + body + "]\n";
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getReadCount() {
		return readCount;
	}
	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

}
