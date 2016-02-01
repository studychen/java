package com.chenxb;

import java.sql.*;
import java.util.Properties;

/**
 * 
 * @author tomchen
 *
 */
public class HADRUser {
	Connection connection;

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

	public HADRUser() {
	}

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

	/**
	 * 
	 * @param useDBCmd
	 * @param tbOpCmd
	 * @param cmdType 输出执行结果信息或者不输出
	 * @return
	 */
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
	 * 等待 3秒*10 = 30秒
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
		if (counter >= 10) {
			throw new RuntimeException("Error:Wait completed failed:Timeout!");
		} else {
			System.out.println("Task completed!");
		}

	}
}