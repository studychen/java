package xian;

import command.AseMasterCommand;
import command.RmaCommand;

public class TwoNodeTest {
	private final static String priIp = "10.173.2.92";
	private final static String staIp = "10.173.2.131";
	private final static String priName = "PRI";
	private final static String staName = "STA";
	private final static boolean isThreeNode = false;
	private final static String passwd = "Sybase123";
	private final static String asePort = "4901";
	private final static String rmaPort = "4909";
	
	static AseMasterCommand priAse = new AseMasterCommand("sapsa", passwd, priIp, asePort);
	static AseMasterCommand staAse = new AseMasterCommand("sapsa", passwd, staIp, asePort);
	static RmaCommand rma = new RmaCommand("DR_admin", passwd, priIp, rmaPort);
	
	public static void main(String [] args) {
		boolean failoverOrBack = true; //this will change
//		boolean failoverOrBack = false; //this will change
		int n = 12345 ;
		switch (n) {
			case 1:
				failoverOrBackPlan(failoverOrBack);
				break;
			case 12:
				failoverOrBackUnplan(failoverOrBack);
				break;
			case 123:
				hostAvailPriSta(failoverOrBack);
				break;
			case 1234:
				rma.getResultBySQLCmd("sap_status path");
				break;
			case 12345:
				checkSendTrace(failoverOrBack);
				break;
		}
	}
	
	public static boolean checkSendTrace(boolean failoverOrBack) {
		if (failoverOrBack) //is failover
			return new CheckSendTrace().checkSendTrace(rma, priName, staAse);
		else //failback
			return new CheckSendTrace().checkSendTrace(rma, staName, priAse);
	}
	
	public static void failoverOrBackPlan(boolean failoverOrBack) {
//		checkSendTrace(failoverOrBack)
		if (true) {
			if (failoverOrBack) //is failover
				rma.failOverHostAvaiPlan(priName, staName, isThreeNode);
			else
				rma.failOverHostAvaiPlan(staName, priName, isThreeNode);
		} else {
			System.out.println("-----------Hadr Env SendTrace not OK--------------");
		}
	}
	
	public static void failoverOrBackUnplan(boolean failoverOrBack) {
		if (checkSendTrace(failoverOrBack)) {
			if (failoverOrBack) //is failover
				rma.failOverUnplan(priName, staName, isThreeNode);
			else
				rma.failOverUnplan(staName, priName, isThreeNode);
		} else {
			System.out.println("-----------Hadr Env SendTrace not OK--------------");
		}
	}
	
	public static void hostAvailPriSta(boolean failoverOrBack) {
		if (failoverOrBack) //is failover
			rma.hostAvailable (priName);
		else
			rma.hostAvailable (staName);
	}

}
