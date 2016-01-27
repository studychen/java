package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AseCommands {
	private Connection masterCon;
	private Connection sidCon;

	public AseCommands(String userName, String password, String serverName, String port, String sid) throws Exception {
		ConnPool connPool = new ConnPool();
		masterCon = connPool.getConn(userName, password, serverName, port);
		sidCon = connPool.getConnSid(userName, password, serverName, port, sid);
	}

	public void excuteCom(String command, boolean flag) {
		if (flag) { // use default: master database
			try {
				Statement statment = masterCon.createStatement();
				// 使用executeUpdate 而不是execute/executeQuery
				statment.executeUpdate(command);
				statment.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else { // use database defined by sid, e.g. NW7
			try {
				Statement statment = sidCon.createStatement();
				statment.executeUpdate(command);
				statment.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void releaseCon() {
		try {
			masterCon.close();
			sidCon.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
