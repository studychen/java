package ASE;

public class TestMain {
	public static void main(String[] args) throws Exception {
		String ip = "10.173.15.65";
		String port = "4901";
		String sid = "NW7";
		AseExcute ase = new AseExcute("sapsa", "Sybase123", ip, port, sid);
		ase.excuteCom("select * from rs_ticket_history", true);
		Thread.sleep(300000);
		for (int i = 1; i < 10000; i++) {
			
		}
		ase.excuteCom("select * from rs_ticket_history", false);
	}

}
