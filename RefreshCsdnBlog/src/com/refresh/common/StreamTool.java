package com.refresh.common;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTool {
	
	/**
	 * 工具类, 读入InputStream, 输出 byte[] 
	 * byte[] 可以转化为 String
	 * @param inputStr
	 * @return
	 * @throws Exception
	 */
	public static byte[] read(InputStream inputStr) throws Exception {
		ByteArrayOutputStream outStr = new ByteArrayOutputStream();
		// TODO Auto-generated method stub
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inputStr.read(buffer)) != -1) {
			String searchTitle = new String(buffer, "UTF-8");
			if (searchTitle.contains(" <title>Java 数组 声明 定义 传参数 - never_cxb的专栏")){
				System.out.println(searchTitle);
			}
			outStr.write(buffer, 0, len);
		}
		inputStr.close();
		return outStr.toByteArray();
	}

}