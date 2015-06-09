package xian;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Properties;

import command.AseMasterCommand;
import command.RmaCommand;

public class CheckSendTrace {
	public boolean checkSendTrace(RmaCommand rma, String priName, AseMasterCommand staAse) { 
		
		int originalCount = staAse.getSendTraceCount();
		System.out.println("----------originalCount " + originalCount + " ---------------");
		String cmd = "sap_send_trace " + priName;
		rma.getResultBySQLCmd(cmd);
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int laterCount = staAse.getSendTraceCount();
		System.out.println("----------laterCount " + laterCount + " ---------------");
		if(laterCount == originalCount + 1) {
			System.out.println("-----------sap_send_trace OK--------------");
			return true;
		}
		else {
			System.out.println("Error-----------sap_send_trace not OK--------------");
			return false;
		}
	}

}
