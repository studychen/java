package com.chenxb.test;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.chenxb.biz.ArticleItemBiz;

public class TestJob implements Job {

	private int articleId;

	public TestJob(int articleId) {
		this.articleId = articleId;
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		System.out.println("========== articleId " + articleId + " ==========");
		System.out.println("TestJob running");
	}

}
