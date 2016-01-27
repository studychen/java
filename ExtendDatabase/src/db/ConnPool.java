package db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * 连接池
 * @author tomchen
 *
 */
public class ConnPool {

	public ConnPool() throws Exception {
		DriverManager.registerDriver((Driver) Class.forName("com.sybase.jdbc4.jdbc.SybDriver").newInstance());
	}

	/**
	 * 默认主数据库是 master 也可以是sid 指定的数据库
	 * @param userName 用户名
	 * @param password 密码
	 * @param serverName 服务器 ip 或者域名
	 * @param port 端口 默认是是4901
	 * @return
	 * @throws Exception
	 */
	public Connection getConn(String userName, String password, String serverName, String port) throws Exception {
		// 默认连接 master 数据库
		String connectionString = "jdbc:sybase:Tds:" + serverName + ":" + port + "/master";
		Properties info = new Properties();
		info.put("user", userName);
		info.put("password", password);
		info.put("ENCRYPT_PASSWORD", "true");
		Connection connection = DriverManager.getConnection(connectionString, info);
		return connection;
	}

	/**
	 * 连接名称为 sid 的数据库
	 * @param userName 
	 * @param password 
	 * @param serverName
	 * @param port
	 * @param sid 一般为 NW7 或者 PI1
	 * @return
	 * @throws Exception
	 */
	public Connection getConnSid(String userName, String password, String serverName, String port, String sid)
			throws Exception {
		String connectionString = "jdbc:sybase:Tds:" + serverName + ":" + port + "/" + sid;
		Properties info = new Properties();
		info.put("user", userName);
		info.put("password", password);
		info.put("ENCRYPT_PASSWORD", "true");
		Connection connection = DriverManager.getConnection(connectionString, info);
		return connection;
	}

}
