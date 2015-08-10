package com.refresh.common;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTool {
	
	/**
	 * ������, ����InputStream, ��� byte[] 
	 * byte[] ����ת��Ϊ String
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
			if (searchTitle.contains(" <title>Java ���� ���� ���� ������ - never_cxb��ר��")){
				System.out.println(searchTitle);
			}
			outStr.write(buffer, 0, len);
		}
		inputStr.close();
		return outStr.toByteArray();
	}

}