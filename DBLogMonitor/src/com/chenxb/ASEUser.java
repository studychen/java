package com.chenxb;

import java.sql.Statement;

/**
 * 
 * @author tomchen
 *
 */
public class ASEUser extends HADRUser {
	String sid;

	public ASEUser(String userName, String password, String serverName, String port, String sid) {
		super(userName, password, serverName, port);
		this.sid = sid;
	}

	/**
	 * 清空 log
	 */
	public void dumpTranWithNoLog() {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("use master");
			stmt.execute("dump tran " + this.sid.toUpperCase() + " with no_log");
			stmt.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	/**
	 * ASE 置为 primary
	 */
	public void promoteASEtoPrimary() {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("use master");
			stmt.execute("sp_hadr_admin primary,'force'");
			stmt.execute("sp_hadr_admin activate");
			stmt.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	/**
	 * 启动 rep agent
	 * @param database
	 */
	public void startRAT(String database) {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("use master");
			stmt.execute("sp_start_rep_agent " + database);
			stmt.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	/**
	 * 将ase置为standy
	 */
	public void degradeASEtoStandby() {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("use master");
			stmt.execute("sp_hadr_admin 'deactivate','120','test','force'");
			stmt.execute("sp_hadr_admin standby");
			stmt.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	/**
	 * 停止ase
	 * 或者 kill -9 ase_pid
	 */
	public void shutdown() {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("shutdown");
		} catch (Exception exp) {
			// exp.printStackTrace();
		}
	}

	/**
	 * 停止 rep agent
	 * @param database
	 */
	public void stopRat(String database) {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("use master");
			stmt.execute("sp_stop_rep_agent " + database);
			stmt.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}
