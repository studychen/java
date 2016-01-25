package com.chenxb.news;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;

import com.chenxb.biz.ArticleItemBiz;
import com.chenxb.model.ArticleItem;
import com.chenxb.util.MysqlTool;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

public class Test {
	public static void main(String arg[]) throws IOException {

	}
}

class FetchRunnable extends Thread {
	private CallbackListener listener;

	public FetchRunnable(CallbackListener listener) {
		this.listener = listener;
	}

	@Override
	public void run() {
		try {
			System.out.println(getName() + "start");
			Thread.sleep(3000);
			int i = new Random().nextInt(10);
			System.out.println(getName() + "====" + i);
			listener.method(i);
		} catch (InterruptedException e) {
		}
	}

}

interface CallbackListener {
	void method(int i);
}
