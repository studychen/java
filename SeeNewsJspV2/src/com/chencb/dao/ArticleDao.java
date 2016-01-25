package com.chencb.dao;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import com.chenxb.model.ArticleItem;
import com.chenxb.util.MysqlTool;
import com.sina.sae.util.SaeUserInfo;

public class ArticleDao {
	private Connection connection;

	public ArticleDao() throws Exception {
		connection = MysqlTool.getConnection();
	}

	public int insertArticle(ArticleItem article) throws SQLException

	{
		// the mysql insert statement
		String query = " insert into latest (id, image_urls, title, publish_date, read_times,source,body)"
				+ " values (?, ?, ?, ?, ?,?,?)";

		// create the mysql insert preparedstatement
		PreparedStatement preparedStmt = connection.prepareStatement(query);
		preparedStmt.setInt(1, article.getId());
		preparedStmt.setString(2, Arrays.toString(article.getImageUrls()));
		preparedStmt.setString(3, article.getTitle());
		preparedStmt.setDate(4, Date.valueOf(article.getPublishDate()));
		preparedStmt.setInt(5, article.getReadTimes());
		preparedStmt.setString(6, article.getSource());
		preparedStmt.setString(7, article.getBody());
		return preparedStmt.executeUpdate();
	}

	public ArticleItem getArticleById(int id) throws SQLException {
		// the mysql select statement
		String query = "select * from latest where id = ?";

		// create the mysql preparedstatement
		PreparedStatement preparedStmt = connection.prepareStatement(query);
		preparedStmt.setInt(1, id);

		ResultSet rs = preparedStmt.executeQuery();
		while (rs.next()) {
			String[] imageUrls = rs.getString(2).replace("[", "").replace("]", "").split(", ");
			String title = rs.getString(3);
			String date = rs.getDate(4).toString();
			int readTimes = rs.getInt(5);
			String source = rs.getString(6);
			String body = rs.getString(7);
			ArticleItem article = new ArticleItem(id, imageUrls, title, date, readTimes, source, body);
			return article;
		}

		return null;
	}

}
