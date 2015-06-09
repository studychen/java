package ASE;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AseExcute {
	private Connection masterCon;
	private Connection sidCon;
	
	public AseExcute(String userName,String password,String serverName,String port,String sid) throws Exception {
		 ConnPool connPool = new ConnPool();
		 masterCon = connPool.getConn(userName, password, serverName, port);
		 sidCon = connPool.getConnSid(userName, password, serverName, port,sid);
	}
	
	public void excuteCom(String command, boolean flag) {
		if (flag) { //use master
			try {
				Statement statment = masterCon.createStatement();
				statment.executeUpdate(command);
				statment.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else { //use NW7
			try {
				Statement statment = sidCon.createStatement();
				statment.executeUpdate(command);
				statment.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void releaseCon() {
		try {
			masterCon.close();
			sidCon.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
