package auto;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * 自己最初的测试自动化工具类
 * 后期完善了并打包成jar包供linux命令行使用
 * 实现failover自动化、检查异步任务是否完成、切换模式等等
 * @author tomchen
 *
 */
public class FirstAutoTest {
	Connection connection;
	// 3个节点的别名
	String name1 = "PRI";
	String name2 = "STA";
	String name3 = "D3";

	// 需要的信息，字符串匹配，括号不能省去
	String wantInfo = "(HADR Status)|(Synchronization Mode)|(Distribution Mode)";

	FirstAutoTest(String userName, String password, String serverName, String port) {
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
	 * 和上面的方法getResultBySQLCmd类似
	 * 输出时过滤了一些信息
	 * @param sqlCmd
	 * @return
	 */
	public int sapstatusPathBySQLCmd(String sqlCmd) {
		int numberOfItems = 0;
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlCmd);
			String headName = name1 + "|" + name2 + "|" + name3;
			while (rs.next()) {
				numberOfItems++;
				String value = rs.getString(1);
				if (value.matches(headName) && rs.getString(2).matches(wantInfo)) {
					System.out.printf("%-40s", value);
					System.out.printf("%-40s", rs.getString(2));
					System.out.printf("%-40s", rs.getString(3));
					System.out.println();
				}
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
	 * 查看异步任务是否完成
	 * 可检查任务状态是否为Completed
	 * 或    已完成的任务数是否等于总任务数来检验
	 * @return
	 */
	public boolean checkTaskEnd() {
		boolean flag = true;
		String sqlCmd = "sap_status";
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlCmd);
			ResultSetMetaData rsMetaData = rs.getMetaData();
			Integer totalCols = rsMetaData.getColumnCount();
			String numOfTask = null;
			while (rs.next()) {
				for (int i = 1; i <= totalCols; i++) {
					String value = rs.getString(i);
					System.out.printf("%-30s", value);
				}
				System.out.println();
				String mark = rs.getString(2).trim();
				if (mark.equals("Task State")) {
					// 看任务是否等于Completed
					if (!(rs.getString(3).equals("Completed"))) {
						flag = false;
					}
				}
				// 当前执行的任务
				if (mark.equals("Current Task Number")) {
					System.out.println(rs.getString(3));
					numOfTask = rs.getString(3);
				}

				// 一共几个任务
				if (mark.equals("Total Number of Tasks")) {
					if (!(numOfTask.equals(rs.getString(3)))) {
						flag = false;
					}
				}
			}
			rs.close();
			stmt.close();
		} catch (Exception exp) {
			exp.printStackTrace();
			System.out.println("Task is Completed ?" + flag);
			return flag;
		}
		System.out.println("Task is Completed ?" + flag);
		return flag;
	}

	/**
	 * failover -> sap_status -> sap_host_available
	 */
	public void failOver() {
		getResultBySQLCmd("sap_failover PRI, STA, 120");

		// 30秒检验一次，任务是否completed
		while (true) {
			if (checkTaskEnd())
				break;
			try {
				Thread.sleep(30000); // 30s
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// 下面这个也是异步任务
		getResultBySQLCmd("sap_failover_remaining 60");
		while (true) {
			if (checkTaskEnd())
				break;
			try {
				Thread.sleep(30000); // 30s
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		getResultBySQLCmd("sap_host_available PRI");
		System.out.println("--------------failover end------------------");
	}

	public void failBack() {
		// // force or no force 待定
		getResultBySQLCmd("sap_failover STA, PRI, 120");

		while (true) {
			if (checkTaskEnd())
				break;
			try {
				Thread.sleep(30000); // 30s
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		getResultBySQLCmd("sap_failover_remaining 60");
		while (true) {
			if (checkTaskEnd())
				break;
			try {
				Thread.sleep(30000); // 30s
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		getResultBySQLCmd("sap_host_available STA");
		System.out.println("--------------failback end------------------");
	}

	/**
	 * 可refer to HADRThirdUser.checkHADRStatus
	 * 它检查了状态是否 Active 或 Suspended
	 */
	public void checkPath() {
		// 这儿 check path 只是输出信息
		// 没有代码检验链路 PRI.STA.NW7 PRI.STA.master 是否Active Suspended
		sapstatusPathBySQLCmd("sap_status path");
	}

	/**
	 * 这儿主和从都硬编码为PRI和STA
	 */
	public void modeSync() {
		sapstatusPathBySQLCmd("sap_update_replication synchronization_mode PRI, sync");
		sapstatusPathBySQLCmd("sap_update_replication synchronization_mode STA, sync");
	}

	public void modeAsync() {
		sapstatusPathBySQLCmd("sap_update_replication synchronization_mode PRI, async");
		sapstatusPathBySQLCmd("sap_update_replication synchronization_mode STA, async");
	}

	public void modeNearsync() {
		sapstatusPathBySQLCmd("sap_update_replication synchronization_mode PRI, nearsync");
		sapstatusPathBySQLCmd("sap_update_replication synchronization_mode STA, nearsync");
	}

	public void modeLocal() {
		sapstatusPathBySQLCmd("sap_update_replication distribution_mode PRI, local");
		sapstatusPathBySQLCmd("sap_update_replication distribution_mode STA, loca");
	}

	public void modeRemote() {
		sapstatusPathBySQLCmd("sap_update_replication distribution_mode PRI, remote, STA");
		sapstatusPathBySQLCmd("sap_update_replication distribution_mode STA, remote, PRI");
	}

	private void changeMode() {
		// TODO 更加友好地切换模式
	}

	public static void main(String[] args) {
		FirstAutoTest m = new FirstAutoTest("DR_admin", "**passwd**", "10.173.**.***", "4909");

		int n = 3;
		// 改变 n 值，执行不同的任务
		switch (n) {
		case 1:
			m.failOver();
			break;
		case 2:
			m.failBack();
			break;
		case 3:
			m.checkPath();
			break;
		case 4:
			m.changeMode();
			break;
		}
	}
}
