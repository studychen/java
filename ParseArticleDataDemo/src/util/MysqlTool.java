package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlTool {
	public static void main(String[] args)
			throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		Class.forName("com.mysql.jdbc.Driver").newInstance(); // 加载MYSQL
																// JDBC驱动程序
		// Class.forName("org.gjt.mm.mysql.Driver");
		System.out.println("Success loading Mysql Driver!");

		String URL = "jdbc:mysql://r.rdc.sae.sina.com.cn:3307/app_seenews";
		String Username = "onwmo4jj3n";
		String Password = "0105zjxzw0x1x11wk1hm3izl050kx0zhw4mkj0m5";

		Connection con = DriverManager.getConnection(URL, Username, Password);

		Statement stmt = con.createStatement();
		
		String sql = "SELECT * FROM  `just_test` LIMIT 0 , 30";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			System.out.println(rs.getInt(0) + rs.getString(1));
		}
		con.close();
		System.out.println("Success connect Mysql server!");
	}

}
