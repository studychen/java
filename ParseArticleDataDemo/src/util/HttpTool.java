package util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import model.CommonException;

public class HttpTool {
	/**
	 * 
	 * @param urlStr
	 *            网页链接
	 * @return 网页的 html 源码
	 */
	public static String doGet(String urlStr) throws CommonException {
		URL url;
		String html = "";
		try {
			url = new URL(urlStr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			if (connection.getResponseCode() == 200) {
				InputStream in = connection.getInputStream();
				html = StreamTool.inToStringByByte(in);
				in.close();
			} else {
				throw new CommonException("新闻服务器返回值不为200");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException("get请求失败");
		}
		return html;
	}
}
