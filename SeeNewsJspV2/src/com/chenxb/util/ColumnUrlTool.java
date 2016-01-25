package com.chenxb.util;

public class ColumnUrlTool {
	// LATEST,//最新消息
	// NOTIFIC, //校园通知
	// BACHELOR, //本科教学 学士
	// MASTER, //研究生 硕士
	// RESEARCH, //科研
	// ACADEMIC //学术交流

	private static final String LATEST_URL = "http://see.xidian.edu.cn/index.php/index/more";
	// 格式为http://see.xidian.edu.cn/html/category/5/2.html
	private static final String NOTIFIC_URL = "http://see.xidian.edu.cn/html/category/";

	/**
	 * 
	 * @param type
	 * @param currentPage
	 *            不是无限大，有一定范围
	 * @return
	 */
	public static String generateUrl(int type, int currentPage) {
		currentPage = currentPage > 0 ? currentPage : 1;
		switch (type) {
		case ColumnType.LATEST:
			return LATEST_URL;
		case ColumnType.NOTIFIC:
		case ColumnType.BACHELOR:
		case ColumnType.MASTER:
		case ColumnType.ACADEMIC:
		case ColumnType.JOB:
			return NOTIFIC_URL + type + "/" + currentPage + ".html";
		default:
			return LATEST_URL;
		}
	}
}
