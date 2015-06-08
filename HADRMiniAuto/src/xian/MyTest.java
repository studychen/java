package xian;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Properties;
import java.util.regex.Pattern;

public class MyTest {
	Connection connection;
	String name1 = "PRI";
	String name2 = "STA";
	String name3 = "D3";
	String wantInfo = "(HADR Status)|(Synchronization Mode)|(Distribution Mode)";
	MyTest(String userName,String password,String serverName,String port){
        try{
            DriverManager.registerDriver((Driver) Class.forName("com.sybase.jdbc4.jdbc.SybDriver").newInstance());
            String connectionString="jdbc:sybase:Tds:"+serverName+":"+port;
            Properties info = new Properties();
            info.put("user", userName);
            info.put("password", password);
            info.put("ENCRYPT_PASSWORD","true");
            connection=DriverManager.getConnection(connectionString,info);
        }catch(Exception exp){
            exp.printStackTrace();
        }
    }
	
	public int getResultBySQLCmd(String sqlCmd){
        int numberOfItems=0;
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCmd);
            ResultSetMetaData rsMetaData=rs.getMetaData();
            Integer totalCols=rsMetaData.getColumnCount();
            while (rs.next()) {
                numberOfItems++;
                for(int i=1;i<=totalCols;i++){
                    String value=rs.getString(i);
                    System.out.print(value+"\t\t\t");
                }
                System.out.println();
            }
            rs.close();
            stmt.close();
        }catch(Exception exp){
            exp.printStackTrace();
            return numberOfItems;
        }
        return  numberOfItems;
    }
	
	public int sapstatusPathBySQLCmd(String sqlCmd){
        int numberOfItems=0;
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCmd);
            ResultSetMetaData rsMetaData=rs.getMetaData();
            Integer totalCols=rsMetaData.getColumnCount();
            String headName = name1 + "|"+ name2 + "|" + name3;
            while (rs.next()) {
                numberOfItems++;
                String value=rs.getString(1);
                if (value.matches(headName) && rs.getString(2).matches(wantInfo)) {
                	System.out.printf("%-40s", value);
            		System.out.printf("%-40s", rs.getString(2));
                	System.out.printf("%-40s", rs.getString(3) );
                	System.out.println();
                }
            }
            rs.close();
            stmt.close();
        }catch(Exception exp){
            exp.printStackTrace();
            return numberOfItems;
        }
        return  numberOfItems;
    }
	
	public int failoverBySQLCmd(String sqlCmd){
        int numberOfItems=0;
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCmd);
            ResultSetMetaData rsMetaData=rs.getMetaData();
            Integer totalCols=rsMetaData.getColumnCount();
            while (rs.next()) {
                numberOfItems++;
                for(int i=1;i<=totalCols;i++){
                    String value=rs.getString(i);
                    System.out.printf("%-40s", value);
                }
                System.out.println();
            }
            rs.close();
            stmt.close();
        }catch(Exception exp){
            exp.printStackTrace();
            return numberOfItems;
        }
        return  numberOfItems;
    }
	
	
	public int hostAvailableBySQLCmd(){
		String sqlCmd = "sap_host_available STA";
        int numberOfItems=0;
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCmd);
            ResultSetMetaData rsMetaData=rs.getMetaData();
            Integer totalCols=rsMetaData.getColumnCount();
            while (rs.next()) {
                numberOfItems++;
                for(int i=1;i<=totalCols;i++){
                    String value=rs.getString(i);
                    System.out.print(value+"\t\t\t");
                }
                System.out.println();
            }
            rs.close();
            stmt.close();
        }catch(Exception exp){
            exp.printStackTrace();
            return numberOfItems;
        }
        return  numberOfItems;
    }
	
	public boolean checkTaskEnd (){
		boolean flag = true;
		String sqlCmd = "sap_status";
        int numberOfItems=0;
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCmd);
            ResultSetMetaData rsMetaData=rs.getMetaData();
            Integer totalCols=rsMetaData.getColumnCount();
            String numOfTask = null;
            while (rs.next()) {
                numberOfItems++;
                for(int i=1;i<=totalCols;i++){
                    String value=rs.getString(i);
                    System.out.printf("%-30s", value);
                }
                System.out.println();
                String mark = rs.getString(2).trim();
                if (mark.equals("Task State")) {
                	if (! (rs.getString(3).equals("Completed"))) {
                		flag = false;
                	}
                }
                if (mark.equals("Current Task Number")) {
                	System.out.println(rs.getString(3));
                	numOfTask = rs.getString(3);
                }
                if (mark.equals("Total Number of Tasks")) {
                	if (! (numOfTask.equals(rs.getString(3))) ) {
                		flag = false;
                	};
                }
            }
            rs.close();
            stmt.close();
        }catch(Exception exp){
            exp.printStackTrace();
            System.out.println("Task is Completed ?" + flag);
            return flag;
        }
        System.out.println("Task is Completed ?" + flag);
        return  flag;
    }
	
	public static void failOver(){
		MyTest m = new MyTest("DR_admin", "Sybase123", "10.173.14.125", "4909");
		m.getResultBySQLCmd("sap_failover PRI, STA, 120"); //
		while (true) {
			if (m.checkTaskEnd())
				break;
			try {
				Thread.sleep(30000); //30s
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		m.getResultBySQLCmd("sap_failover_remaining 60");
		while (true) {
			if (m.checkTaskEnd())
				break;
			try {
				Thread.sleep(30000); //30s
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		m.getResultBySQLCmd("sap_host_available PRI");
		System.out.println("--------------failover end------------------");
	}
	
	public static void failBack(){
		MyTest m = new MyTest("DR_admin", "Sybase123", "10.173.14.125", "4909");
		m.getResultBySQLCmd("sap_failover STA, PRI, 120"); //force or no force ??
		while (true) {
			if (m.checkTaskEnd())
				break;
			try {
				Thread.sleep(30000); //30s
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		m.getResultBySQLCmd("sap_failover_remaining 60");
		while (true) {
			if (m.checkTaskEnd())
				break;
			try {
				Thread.sleep(30000); //30s
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		m.getResultBySQLCmd("sap_host_available STA");
		System.out.println("--------------failback end------------------");
	}
	
	public static void checkPath() {
		MyTest m = new MyTest("DR_admin", "Sybase123", "10.173.14.125", "4909");
		m.sapstatusPathBySQLCmd("sap_status path");
	}
	
	public static void modeSync() {
		MyTest m = new MyTest("DR_admin", "Sybase123", "10.173.14.125", "4909");
		m.sapstatusPathBySQLCmd("sap_update_replication synchronization_mode PRI, sync");
		m.sapstatusPathBySQLCmd("sap_update_replication synchronization_mode STA, sync");
	}
	
	public static void modeAsync() {
		MyTest m = new MyTest("DR_admin", "Sybase123", "10.173.14.125", "4909");
		m.sapstatusPathBySQLCmd("sap_update_replication synchronization_mode PRI, async");
		m.sapstatusPathBySQLCmd("sap_update_replication synchronization_mode STA, async");
	}
	
	public static void modeNearsync() {
		MyTest m = new MyTest("DR_admin", "Sybase123", "10.173.14.125", "4909");
		m.sapstatusPathBySQLCmd("sap_update_replication synchronization_mode PRI, nearsync");
		m.sapstatusPathBySQLCmd("sap_update_replication synchronization_mode STA, nearsync");
	}
	
	public static void modeLocal() {
		MyTest m = new MyTest("DR_admin", "Sybase123", "10.173.14.125", "4909");
		m.sapstatusPathBySQLCmd("sap_update_replication distribution_mode PRI, local");
		m.sapstatusPathBySQLCmd("sap_update_replication distribution_mode STA, loca");
	}
	
	public static void modeRemote() {
		MyTest m = new MyTest("DR_admin", "Sybase123", "10.173.14.125", "4909");
		m.sapstatusPathBySQLCmd("sap_update_replication distribution_mode PRI, remote, STA");
		m.sapstatusPathBySQLCmd("sap_update_replication distribution_mode STA, remote, PRI");
	}
	
	private static void changeMode() {
		
	}
	
	
	
	public static void main(String [] args) {
		int n = 3;
		
		switch (n) {
		case 1:
			failOver();
			break;
		case 2:
			failBack();
			break;
		case 3:
			checkPath();
			break;
		case 4:
			changeMode();
			break;
		}
			
		
//		checkPath();
//		MyTest m = new MyTest("DR_admin", "Sybase123", "10.173.14.125", "4909");
//		m.hostAvailableBySQLCmd();
//		m.getResultBySQLCmd("sap_failover STA, PRI, 120");
//		m.checkTaskEnd();
	}

	

}


