package com.chenxb.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTool {
	private static final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");

	/**
	 * 
	 * @return 格式化，得到当前的日期
	 */
	public static String getCurrentTime() {
		return DateFormat.format(new Date());
	}

}
