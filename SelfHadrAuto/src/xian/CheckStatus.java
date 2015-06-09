package xian;

public class CheckStatus {
	
	public static void main(String [] args) {
		MyTest m = new MyTest("DR_admin", "Sybase123", rmaIp, "4909");
		checkPath(m);
//		changeMode();
		new Remind();
	}
	
	private final static String rmaIp =  "127.0.0.1" ;
	private static void changeMode() {
		MyTest m = new MyTest("DR_admin", "Sybase123", rmaIp, "4909");
		int mode = 3;
		System.out.println("++++++++++++++++old+++++++++++++++++++++++");
//		checkPath(m);
		switch (mode) {
		case 1:
			modeSync(m);
			break;
		case 2:
			modeAsync(m);
			break;
		case 3:
			modeNearsync(m);
			break;
		case 4:
			modeLocal(m);
			break;
		case 5:
			modeRemote(m);
			break;
		}
		System.out.println("---------------new---------------------");
		checkPath(m);
		System.out.println("---------------change mode end---------------------");
	}
	
	public static void checkPath(MyTest m) {
		m.sapstatusPathBySQLCmd("sap_status path");
	}
	
	
	public static void modeSync(MyTest m) {
		m.sapstatusPathBySQLCmd("sap_update_replication synchronization_mode PRI, sync");
		m.sapstatusPathBySQLCmd("sap_update_replication synchronization_mode STA, sync");
	}
	
	public static void modeAsync(MyTest m) {
		m.sapstatusPathBySQLCmd("sap_update_replication synchronization_mode PRI, async");
		m.sapstatusPathBySQLCmd("sap_update_replication synchronization_mode STA, async");
	}
	
	public static void modeNearsync(MyTest m) {
		m.sapstatusPathBySQLCmd("sap_update_replication synchronization_mode PRI, nearsync");
		m.sapstatusPathBySQLCmd("sap_update_replication synchronization_mode STA, nearsync");
	}
	
	public static void modeLocal(MyTest m) {
		m.sapstatusPathBySQLCmd("sap_update_replication distribution_mode PRI, local");
		m.sapstatusPathBySQLCmd("sap_update_replication distribution_mode STA, loca");
	}
	
	public static void modeRemote(MyTest m) {
		m.sapstatusPathBySQLCmd("sap_update_replication distribution_mode PRI, remote, STA");
		m.sapstatusPathBySQLCmd("sap_update_replication distribution_mode STA, remote, PRI");
	}

}
