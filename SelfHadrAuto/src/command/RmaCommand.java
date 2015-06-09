package command;


import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Properties;

import xian.MyTest;

public class RmaCommand {
	Connection connection;
	public RmaCommand(String userName,String password,String serverName,String port){
		//for example serverName = 10.173.1.193
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
	
	public  int getResultBySQLCmd(String sqlCmd){
		System.out.println("+++++++++++++++cmd " + sqlCmd + " start++++++++++++++++");
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
	
	
	public void failOverHostAvaiPlan (String priName, String staName, boolean isThreeNode){
		System.out.println("+++++++++++++++failOverPlan " + priName + " " + staName + "&HostAvailable start++++++++++++++++");
		String cmd = "sap_failover " + priName + ", " + staName + ", 120, force";
		getResultBySQLCmd(cmd); //
		isTaskEnd(3000);
		
		if(isThreeNode) {
			getResultBySQLCmd("sap_failover_remaining 60");
			isTaskEnd(2000);
		}
		hostAvailable(priName);
	}
	
	public void failOverUnplan (String priName, String staName, boolean isThreeNode){
		System.out.println("+++++++++++++++failOverUnplan " + priName + " " + staName + "&HostAvailable start++++++++++++++++");
		String cmd = "sap_failover " + priName + ", " + staName + ", 120" + " unplanned";
		getResultBySQLCmd(cmd); //
		isTaskEnd(3000);
		
		if(isThreeNode) {
			getResultBySQLCmd("sap_failover_remaining 60");
			isTaskEnd(2000);
		}
		System.out.println("--------------failOverUnplan end, please start "+ priName + " Ase ------------------");
		System.out.println("--------------need run sap_host_available "+ priName + " ------------------");
//		hostAvailablePRI(priName);
	}
	
	
	public void hostAvailable(String machineName){
		String cmd = "sap_host_available " + machineName; //STA or PRI
		System.out.println(cmd + " running......");
		getResultBySQLCmd("sap_host_available STA");
		System.out.println("-------------- "+ cmd +" end------------------");
	}
	
	
	private void isTaskEnd(int checkTime){
		while (true) {
			if (checkTaskEnd())
				return;
			try {
				Thread.sleep(checkTime); //30s
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private boolean checkTaskEnd (){
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
                	//System.out.println(rs.getString(3));
                	numOfTask = rs.getString(3);
                }
//                if (mark.equals("Total Number of Tasks")) {
//                	if (! (numOfTask.equals(rs.getString(3))) ) {
//                		flag = false;
//                	};
//                }
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
}
