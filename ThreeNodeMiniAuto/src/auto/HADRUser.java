package auto;

import java.sql.*;
import java.util.Properties;

/**
 * 
 * @author tomchen
 *
 */
public class HADRUser {
	// RMA的连接
	Connection connection;

	/**
	 * 
	 * @param userName 用户名
	 * @param password 密码
	 * @param serverName ip或者域名
	 * @param port 端口
	 */
	HADRUser(String userName, String password, String serverName, String port) {
		try {
			DriverManager.registerDriver((Driver) Class.forName("com.sybase.jdbc4.jdbc.SybDriver").newInstance());
			String connectionString = "jdbc:sybase:Tds:" + serverName + ":" + port;
			Properties info = new Properties();
			info.put("user", userName);
			info.put("password", password);
			info.put("ENCRYPT_PASSWORD", "true");
			connection = DriverManager.getConnection(connectionString, info);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	/**
	 * 根据输入命令，返回执行结果
	 * 这儿使用的是executeQuery，注意和execute区分
	 * @param sqlCmd 命令
	 * @return
	 */
	public int getResultBySQLCmd(String sqlCmd) {
		int numberOfItems = 0;
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlCmd);
			ResultSetMetaData rsMetaData = rs.getMetaData();
			Integer totalCols = rsMetaData.getColumnCount();
			while (rs.next()) {
				numberOfItems++;
				for (int i = 1; i <= totalCols; i++) {
					String value = rs.getString(i);
					System.out.print(value + "\t\t\t");
				}
				System.out.println();
			}
			rs.close();
			stmt.close();
		} catch (Exception exp) {
			exp.printStackTrace();
			return numberOfItems;
		}
		return numberOfItems;
	}

	public int getResultOfDBTable(String useDBCmd, String tbOpCmd, String cmdType) {
		int numberOfItems = 0;
		try {
			Statement stmt = connection.createStatement();
			stmt.execute(useDBCmd);
			// stmt.executeUpdate(useDBCmd);
			if (cmdType.equals("NORET")) {
				stmt.execute(tbOpCmd);
			} else {
				System.out.println();
				ResultSet rs = stmt.executeQuery(tbOpCmd);

				while (rs.next()) {
					ResultSetMetaData rsMetaData = rs.getMetaData();
					Integer totalCols = rsMetaData.getColumnCount();
					numberOfItems++;
					for (int i = 1; i <= totalCols; i++) {
						String value = rs.getString(i);
						System.out.print(value + "\t\t\t");
					}
					System.out.println();
				}
				rs.close();
			}

			stmt.close();
		} catch (Exception exp) {
			exp.printStackTrace();
			return numberOfItems;
		}
		return numberOfItems;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		connection.close();
	}

	/**
	 * 异步命令，循环判断是否执行完毕
	 */
	public void waitUntilFinished() {
		int counter = 0;
		boolean notFinished = true;
		while (counter < 10 && notFinished) {
			System.out.println("#################################");
			try {
				Thread.sleep(3000);

				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("sap_status task");
				ResultSetMetaData rsMetaData = rs.getMetaData();
				Integer totalCols = rsMetaData.getColumnCount();
				while (rs.next()) {
					for (int i = 1; i <= totalCols; i++) {
						String value = rs.getString(i);
						System.out.print(value + "\t\t\t");
						if (i == 3 && value.trim().toLowerCase().equals("Completed".toLowerCase())) {
							System.out.println("Finished!");
							notFinished = false;
						}
					}
					System.out.println();
				}
				rs.close();
				stmt.close();
			} catch (Exception exp) {
				exp.printStackTrace();
				throw new RuntimeException("Error:Error happened in sap_status");
			}
		}
		// 防止异步命令一直执行，无法终止
		if (counter >= 10) {
			throw new RuntimeException("Error:Wait completed failed:Timeout!");
		} else {
			System.out.println("Task completed!");
		}

	}
}
