package sap.xian;

public class Main {

	public static void main(String[] args) {
//		if (args.length < 1) {
//			System.out
//					.println("Usage:\nRun: java -jar runRemoteWin <cmd>");
//			throw new RuntimeException("Wrong input parameters");
//		} else {
			try {
				String user = "i316736";
				String passwd = "Sybase123";
				String ip = "10.173.0.44";
				String cmd = "C:/Python26/python.exe C:/sumScripts/sumSapgui.py 10.173.0.158 PI1 00";
				Worker test = new Worker(user, passwd, ip, cmd);
				test.runCmd();
			} catch (Exception exp) {
				throw new RuntimeException("Cmd run failed!!");
			}
//		}
	}
}