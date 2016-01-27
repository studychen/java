package db;

/**
 * 调试类
 * @author tomchen
 *
 */
public class Test {
	public static void main(String[] args) throws Exception {
		String ip = "10.173.**";
		String port = "49**";
		String sid = "**7";
		AseCommands ase = new AseCommands("**user**", "**passwd**", ip, port, sid);
		ase.excuteCom("select * from rs_ticket_history", true);
		Thread.sleep(300000);
		ase.excuteCom("select * from rs_ticket_history", false);
	}

}
