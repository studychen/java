package auto;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author tomchen
 *
 */
public class HADRThreeNodes extends HADRTwoNodes {

	// 保存sap_set得到的配置信息
	// 实力格式
	// PRI, ase_port 4901
	// PRI, ase_user DR_admin
	// PRI, ase_backup_server_port 4902
	ArrayList<String[]> oneSiteInfo;
	ArrayList<String[]> otherSiteInfo;
	ArrayList<String[]> thirdSiteInfo;

	// 3个节点的名称，比如PRI STA D3
	String oneSiteName;
	String otherSiteName;
	String thirdSiteName;

	String sap_sid;

	// 当前的主、从、第3节点
	String curPrimarySite;
	String curStandbySite;
	String thirdNodeName;

	// key -> String 存放节点名称
	// value -> HashMap存放状态和对应的值
	Map<String, HashMap<String, String>> pathStatus;

	public HADRThreeNodes(String userName, String password, String serverName, String port, String thirdNodeName) {
		super(userName, password, serverName, port);
		this.thirdNodeName = thirdNodeName;

		oneSiteInfo = new ArrayList<String[]>();
		otherSiteInfo = new ArrayList<String[]>();
		thirdSiteInfo = new ArrayList<String[]>();
		// The order below should now be changed.
		getServerSiteInfo();
		updateSysInfo();

		// System.out.println("Pri:"+curPrimarySite);
		// System.out.println("Sec:"+curStandbySite);
		// System.out.println("3rd node:"+thirdNodeName);
		// printSysInfo();
	}

	public String retrieveInfoFromSiteInfo(String keyInfo) {
		String value = "";

		// 示例格式 D0, dr_plugin_port 4909
		// Chech from serverInfo to find the info your want.
		// For example: keyInfo=D0, ase_port
		for (int i = 0; i < oneSiteInfo.size(); i++) {
			if (oneSiteInfo.get(i)[0].equals(keyInfo)) {
				return oneSiteInfo.get(i)[1];
			}
		}

		for (int i = 0; i < otherSiteInfo.size(); i++) {
			if (otherSiteInfo.get(i)[0].equals(keyInfo)) {
				return otherSiteInfo.get(i)[1];
			}
		}

		for (int i = 0; i < thirdSiteInfo.size(); i++) {
			if (thirdSiteInfo.get(i)[0].equals(keyInfo)) {
				return thirdSiteInfo.get(i)[1];
			}
		}

		return value;
	}

	/**
	 * 查看最新的 sap_status path
	 * 更新主从的节点名称（类变量存放）
	 */
	public void updateSysInfo() {
		String sqlCmd = "sap_status path";
		ArrayList<String[]> tmpInfo = new ArrayList<String[]>();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlCmd);
			ResultSetMetaData rsMetaData = rs.getMetaData();
			Integer totalCols = rsMetaData.getColumnCount();
			while (rs.next()) {
				String[] info = new String[totalCols];
				for (int i = 1; i <= totalCols; i++) {
					String value = rs.getString(i);
					info[i - 1] = value;
				}
				tmpInfo.add(info);
			}
			rs.close();
			stmt.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}

		// 示例格式如下
		// PRI HADR Status Primary : Active Identify the primary and standby
		// 找出当前主和从的名称
		for (int i = 2; i < tmpInfo.size(); i++) {
			if (tmpInfo.get(i)[1].trim().equals("HADR Status")) {
				if (tmpInfo.get(i)[2].trim().equals("Primary : Active")) {
					curPrimarySite = tmpInfo.get(i)[0];
				} else if (tmpInfo.get(i)[2].trim().equals("Standby : Inactive")) {
					if (!tmpInfo.get(i)[0].equals(thirdNodeName)) {
						curStandbySite = tmpInfo.get(i)[0];
					}
				}
			}

			// Update status:
			try {
				// 改变 key 和 vaule
				// 先 get 得到一个 hashmap，在对这个 hashmap put key & value
				pathStatus.get(tmpInfo.get(i)[0]).put(tmpInfo.get(i)[1], tmpInfo.get(i)[2]);
			} catch (Exception exp) {
				System.err.println("There are record can't be hanned! exit...");
				throw new RuntimeException("Code error, exit...");
			}
		}
	}

	/**
	 * 打印出产品的状态信息
	 */
	public void printSysInfo() {
		System.out.println("********************************************************");
		// 共3个节点，遍历每个节点
		for (String key : pathStatus.keySet()) {
			System.out.println(key + "***************************");
			// 打印出每个节点的信息
			for (String key2 : pathStatus.get(key).keySet()) {
				System.out.print(key2 + "\t\t");
				System.out.println(pathStatus.get(key).get(key2));
			}
		}
	}

	/**
	 * 获取 site 信息
	 */
	private void getServerSiteInfo() {
		// 设置信息
		String sqlCmd = "sap_set";
		ArrayList<String[]> tmpInfo = new ArrayList<String[]>();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlCmd);
			ResultSetMetaData rsMetaData = rs.getMetaData();
			Integer totalCols = rsMetaData.getColumnCount();

			while (rs.next()) {
				String[] info = new String[totalCols];
				for (int i = 1; i <= totalCols; i++) {

					String value = rs.getString(i);
					info[i - 1] = value;
				}
				tmpInfo.add(info);
			}
			rs.close();
			stmt.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}

		sap_sid = tmpInfo.get(1)[1].trim();

		// 一共3个节点，将所有信息分为3组
		// 忽略前面有4条记录
		int nItem = (tmpInfo.size() - 4) / 3;

		for (int i = 0; i < nItem; i++) {
			oneSiteInfo.add(tmpInfo.get(i + 4));
			otherSiteInfo.add(tmpInfo.get(nItem + i + 4));
			thirdSiteInfo.add(tmpInfo.get(nItem * 2 + i + 4));
		}

		// 各个节点的主机名
		// 原字符串为PRI, ase_port
		oneSiteName = oneSiteInfo.get(0)[0].split(",")[0].trim();
		otherSiteName = otherSiteInfo.get(0)[0].split(",")[0].trim();
		thirdSiteName = thirdSiteInfo.get(0)[0].split(",")[0].trim();
		constructPathStatus(sap_sid, oneSiteName, otherSiteName, thirdSiteName);
	}

	private void constructPathStatus(String sap_sid, String oneSiteName, String otherSiteName, String thirdSiteName) {
		pathStatus = new HashMap<String, HashMap<String, String>>();

		HashMap<String, String> statusRecordForSiteOne = new HashMap<String, String>();
		HashMap<String, String> statusRecordForSiteTwo = new HashMap<String, String>();
		HashMap<String, String> statusRecordForSiteThree = new HashMap<String, String>();

		pathStatus.put(oneSiteName, statusRecordForSiteOne);
		pathStatus.put(otherSiteName, statusRecordForSiteTwo);
		pathStatus.put(thirdSiteName, statusRecordForSiteThree);

		ArrayList<String> keysForRepStatus = new ArrayList<String>();

		// 比如下面的形式 PRI.STA.NW7
		// 初始状态 PRI.STA.NW7 和 PRI.STA.master 应该是 Active
		keysForRepStatus.add(oneSiteName + "." + otherSiteName + "." + sap_sid);
		keysForRepStatus.add(oneSiteName + "." + thirdSiteName + "." + sap_sid);
		keysForRepStatus.add(oneSiteName + "." + otherSiteName + ".master");
		keysForRepStatus.add(oneSiteName + "." + thirdSiteName + ".master");

		keysForRepStatus.add(otherSiteName + "." + oneSiteName + "." + sap_sid);
		keysForRepStatus.add(otherSiteName + "." + thirdSiteName + "." + sap_sid);
		keysForRepStatus.add(otherSiteName + "." + oneSiteName + ".master");
		keysForRepStatus.add(otherSiteName + "." + thirdSiteName + ".master");

		keysForRepStatus.add(thirdSiteName + "." + oneSiteName + "." + sap_sid);
		keysForRepStatus.add(thirdSiteName + "." + otherSiteName + "." + sap_sid);
		keysForRepStatus.add(thirdSiteName + "." + oneSiteName + ".master");
		keysForRepStatus.add(thirdSiteName + "." + otherSiteName + ".master");

		// 比如下面的形式
		// PRI.STA.NW7 State Active Path is active and replication can occur.
		// PRI.STA.NW7 Latency Time 2015-03-19 13:58:26.100 Time latency last
		// calculated
		// PRI.STA.NW7 Latency 50306050 Latency (ms)
		// PRI.STA.NW7 Commit Time 2015-03-19 14:20:35.480 Time last commit
		// replicated
		// PRI.STA.NW7 Distribution Path STA The path of Replication Server
		// through which transactions travel.

		// 每一个类似PRI.STA.NW7的 key 都会有下面几个对应的信息
		for (String keyForOneRepStatus : keysForRepStatus) {
			HashMap<String, String> oneRepStatus = new HashMap<String, String>();
			oneRepStatus.put("State", "");
			oneRepStatus.put("Latency Time", "");
			oneRepStatus.put("Latency", "");
			oneRepStatus.put("Commit Time", "");
			oneRepStatus.put("Distribution Path", "");
			pathStatus.put(keyForOneRepStatus, oneRepStatus);
		}

	}

	public void toSync() {
		updateSysInfo();
		String cmdChangePri = "sap_update_replication synchronization_mode " + curPrimarySite + ", sync";
		String cmdChangeSec = "sap_update_replication synchronization_mode " + curStandbySite + ", sync";
		this.getResultBySQLCmd(cmdChangePri);
		this.getResultBySQLCmd(cmdChangeSec);
		waitForToken();
	}

	/**
	 * 清空rs_ticket_history，然后主节点只插一条记录
	 * 预期从节点也会有一条记录
	 */
	private void waitForToken() {
		// Run updateSysInfo() before run this command.
		// Current Pri=curPrimarySite
		HADRTwoNodes tmpPriDRAdmin = new HADRTwoNodes("DR_admin", "**passwd**",
				pathStatus.get(curPrimarySite).get("Hostname"),
				this.retrieveInfoFromSiteInfo(curPrimarySite + ", dr_plugin_port"));
		HADRTwoNodes tmpSecSapsa = new HADRTwoNodes("sapsa", "**passwd**",
				pathStatus.get(curStandbySite).get("Hostname"),
				this.retrieveInfoFromSiteInfo(curStandbySite + ", ase_port"));

		String sendTraceCmd = "sap_send_trace " + curPrimarySite;
		// First clean <SID>.rs_ticket_history
		tmpSecSapsa.getResultOfDBTable("use " + sap_sid, "truncate table rs_ticket_history", "NORET");

		tmpPriDRAdmin.getResultBySQLCmd(sendTraceCmd);
		// Check whether the trace is get or not
		boolean tokenGet = false;
		int countOfToken = 0;
		while (!tokenGet) {
			try {
				Thread.sleep(3000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (tmpSecSapsa.getResultOfDBTable("use " + sap_sid, "select * from rs_ticket_history", "HASRET") > 0) {
				System.out.println("Token get from standby,the route is active");
				tokenGet = true;
			}
			countOfToken++;
			// 最多检验10次，还未结束，抛出异常测试失败
			if (countOfToken > 10) {
				System.out.println("Error!Can't get token from standby");
				throw new RuntimeException("Error!Can't get token from standby");
			}
		}
	}

	public void toAsync() {
		checkHADRStatus(); // Check status to make sure the env is OK before we
							// do test.
		String cmdChangePri = "sap_update_replication synchronization_mode " + curPrimarySite + ", async";
		String cmdChangeSec = "sap_update_replication synchronization_mode " + curStandbySite + ", async";
		this.getResultBySQLCmd(cmdChangePri);
		this.getResultBySQLCmd(cmdChangeSec);
		waitForToken();
		checkHADRStatus(); // Check status to make sure the env is OK after we
							// do test.
	}

	public void toNearSync() {
		checkHADRStatus();
		String cmdChangePri = "sap_update_replication synchronization_mode " + curPrimarySite + ", nearsync";
		String cmdChangeSec = "sap_update_replication synchronization_mode " + curStandbySite + ", nearsync";
		this.getResultBySQLCmd(cmdChangePri);
		this.getResultBySQLCmd(cmdChangeSec);
		waitForToken();
		checkHADRStatus();
	}

	public void toRemote() {
		checkHADRStatus();
		String cmdChangePri = "sap_update_replication distribution_mode " + curPrimarySite + ", remote, "
				+ curStandbySite;
		String cmdChangeSec = "sap_update_replication distribution_mode " + curStandbySite + ", remote, "
				+ curPrimarySite;
		this.getResultBySQLCmd(cmdChangePri);
		this.getResultBySQLCmd(cmdChangeSec);
		waitForToken();
		checkHADRStatus();
	}

	public void toLocal() {
		checkHADRStatus();
		String cmdChangePri = "sap_update_replication distribution_mode " + curPrimarySite + ", local";
		String cmdChangeSec = "sap_update_replication distribution_mode " + curStandbySite + ", local";
		this.getResultBySQLCmd(cmdChangePri);
		this.getResultBySQLCmd(cmdChangeSec);
		checkHADRStatus();
	}

	public void failover(String curPrimary, String curStandby, String timeout, String otherParam) {
		// 在 failover 之前检查状态
		// 不该在 failover 之后检查，因为一次 failover 可能会导致状态不对
		checkHADRStatus();
		String cmd = "";
		if (otherParam == null || otherParam.toLowerCase().equals("")) {
			cmd = "sap_failover " + curPrimary + "," + curStandby + "," + timeout;
		} else if (otherParam.toLowerCase().equals("unplanned")) {
			cmd = "sap_failover " + curPrimary + "," + curStandby + "," + timeout + " unplanned";
		} else {
			cmd = "sap_failover " + curPrimary + "," + curStandby + "," + timeout + "," + otherParam;
		}
		this.getResultBySQLCmd(cmd);
		// 检查异步命令是否执行完成
		this.waitUntilFinished();
		this.getResultBySQLCmd(cmd);
	}

	public void sapHostRemaining() {
		String cmd = "sap_failover_remaining 120";
		this.getResultBySQLCmd(cmd);
		waitForToken();
	}

	public void sapHostAvailable(String curPrimary) {
		String cmd = "sap_host_available " + curPrimary;
		this.getResultBySQLCmd(cmd);
	}

	/**
	 * 检查链路是否
	 */
	public void checkHADRStatus() {
		updateSysInfo();
		String primary = this.curPrimarySite;
		String standby = this.curStandbySite;
		String thirdNode = this.thirdNodeName;
		String sid = this.sap_sid.toUpperCase();
		ArrayList<String> repShouldActive = new ArrayList<String>();
		ArrayList<String> repShouldSuspended = new ArrayList<String>();

		// 主从链路应是 active
		repShouldActive.add(primary + "." + standby + ".master");
		repShouldActive.add(primary + "." + thirdNode + ".master");
		repShouldActive.add(primary + "." + standby + "." + sid);
		repShouldActive.add(primary + "." + thirdNode + "." + sid);

		// 从主链路应是 suspended
		repShouldSuspended.add(standby + "." + primary + ".master");
		repShouldSuspended.add(standby + "." + thirdNode + ".master");
		repShouldSuspended.add(standby + "." + primary + "." + sid);
		repShouldSuspended.add(standby + "." + thirdNode + "." + sid);

		// 3rd节点应是 suspended
		repShouldSuspended.add(thirdNode + "." + primary + ".master");
		repShouldSuspended.add(thirdNode + "." + standby + ".master");
		repShouldSuspended.add(thirdNode + "." + primary + "." + sid);
		repShouldSuspended.add(thirdNode + "." + standby + "." + sid);

		// 只能多次遍历，因为可能主从链路状态显示在前，也有从主链路在前
		for (String key : repShouldActive) {
			if (!pathStatus.get(key).get("State").trim().equals("Active")) {
				throw new RuntimeException("HADR status is WRONG! Check the env before continue!");
			}
		}

		for (String key : repShouldSuspended) {
			if (!pathStatus.get(key).get("State").trim().equals("Suspended")) {
				throw new RuntimeException("HADR status is WRONG! Check the env before continue!");
			}
		}
	}

}
