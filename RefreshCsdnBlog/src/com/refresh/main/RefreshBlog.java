package com.refresh.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.refresh.common.StreamTool;

public class RefreshBlog {
	private static final String baseListUrl = "http://blog.csdn.net/never_cxb/article/list/";

	public static void main(String[] args) {

		HomePage home = countPage(baseListUrl);
		int page = home.getPage();

		List<Integer> originPages = new ArrayList<Integer>(page);
		for (int i = 0; i < page; i++) {
			originPages.add(i);
		}
		List<String> blogList = new ArrayList<String>(page);

		// random �����б�ҳ
		for (int i = 0; i < page; i++) {
			int random = new Random().nextInt(originPages.size());
			int index = originPages.get(random) + 1;
			String list = baseListUrl + index;
			originPages.remove(random);
			blogList.add(list);
		}

		for (String url : blogList) {
			List<String> origin = getBolgUrls(url);
			//����ÿҳ�ϵ�������
			List<String> random = randomUrlList(origin);
			for (String blogUrl: random) {
				accessBolg(blogUrl);
			}
		}
	}

	// ��ȡ����ҳ��
	public static HomePage countPage(String pageUrl) {
		Document doc;
		try {
			doc = Jsoup.connect(pageUrl).userAgent("Mozilla").timeout(3000)
					.get();

			Elements links = doc.select(".pagelist");
			Element link = links.first();

			String text = link.select("span").text();
			System.out.println(text);
			String pattern = new String("(\\d+)[^\\d]*(\\d+)[^\\d]*");

			// ���� Pattern ����
			Pattern r = Pattern.compile(pattern);

			Matcher m = r.matcher(text);
			if (m.find()) {

				return new HomePage(Integer.valueOf(m.group(2)),
						Integer.valueOf(m.group(1)));
			}
			// <div id="papelist" class="pagelist">
			// <span> 29������ ��2ҳ</span><a href="/never_cxb/article/list/1">��ҳ</a>
			// <a href="/never_cxb/article/list/1">��һҳ</a> <a
			// href="/never_cxb/article/list/1">1</a> <strong>2</strong>
			// </div>
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new HomePage();
	}

	// ��ȡ���б�ҳ�ϵ����� url ����
	public static List<String> getBolgUrls(String listUrl) {
		List<String> result = new ArrayList<String>(20);
		Document doc;
		try {
			doc = Jsoup.connect(listUrl).userAgent("Mozilla").timeout(3000)
					.get();

			Elements links = doc.select("a[href]");
			for (Element link : links) {

				if (link.text().equals("�Ķ�")) {
					// ��������ǰ�� abs: ǰ׺,ȡ��һ������·��,
					// �����Ϳ��Է��ذ�����·����URL��ַattr("abs:href")
					result.add(link.attr("abs:href"));
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	// ��ÿҳ������ url����
	public static List<String> randomUrlList(List<String> oriListUrl) {
		if (oriListUrl != null) {
			int size = oriListUrl.size();
			List<String> randomList = new ArrayList<String>(size);
			int[] oriArray = new int[size];

			for (int i = 0; i < size; i++) {
				oriArray[i] = i;
			}

			for (int i = 0; i < size; i++) {
				int random = new Random().nextInt(size - i);
				int value = oriArray[random];
				randomList.add(oriListUrl.get(value));
				
				// note ����i����,��ǰ���һ������λ�û���ǰ��
				oriArray[random] = oriArray[size - i - 1];
				oriArray[size - i - 1] = value;
			}
			return randomList;
		}
		return null;

	}

	// ��һ������url����get�������
	public static void accessBolg(String blogUrl) {
		URL url;
		try {
			url = new URL(blogUrl);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.addRequestProperty("Content-Type", "text/html; charset=UTF-8");
			con.addRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36");
			con.setRequestMethod("GET");

			if (con.getResponseCode() == 200) {
				InputStream inputStr = con.getInputStream();
				StreamTool.read(inputStr);
				System.out.println(blogUrl + " has been accessed");
				// �����������ͣ����
				int sleepSec = new Random().nextInt(200) + 100;
				Thread.sleep(sleepSec * 1000);

			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}