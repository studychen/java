package ASE;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ExtendAseMain {
	public static void main(String[] args) throws Exception {
		String ip = "10.173.3.46";
		String port = "4901";
		String sid = "NW7";
		if (args.length == 3) {
			ip = args[0];
			port = args[1];
			sid = args[2].toUpperCase();
		} else {
			throw new Exception(
					"wrong agrs, usage: java -jar extendAse <ip> <asePort> <sid>");
		}
		System.out.println("===== extend ASE database & log on " + ip
				+ "start ====");
		String logFile = sid + "_log_002";
		String dataFile = sid + "_data_002";
		String phyFolder = "/sybase/" + sid;
		String alterLog = "disk init name='" + logFile + "',physname='"
				+ phyFolder + "/saplog_1/" + sid + "_log_002.dat',size='20G'";
		String alterData = "disk init name='" + dataFile + "',physname='"
				+ phyFolder + "/sapdata_1/" + sid + "_data_002.dat',size='40G'";
		String alterFinal = "alter database " + sid + " on " + dataFile
				+ "='40G' log on " + logFile + "='20G'";

		System.out
				.println("===== " + alterLog + " on " + ip + " running =====");
		ConnPool connPool = new ConnPool();
		AseExcute ase = new AseExcute("sapsa", "Sybase123", ip, port, sid);
		ase.excuteCom(alterLog, true);
		System.out.println("===== " + alterData + " on " + ip
				+ " running =====");
		ase.excuteCom(alterData, true);
		System.out.println("===== " + alterFinal + " on " + ip
				+ " running =====");
		ase.excuteCom(alterFinal, true);

		System.out.println("===== checkpoint on " + ip + " running =====");
		ase.excuteCom("checkpoint", false);

		String spDboption = "sp_dboption " + sid + ",'trunc log on chkpt',true";
		System.out.println("===== " + spDboption + " on " + ip
				+ " running =====");
		ase.excuteCom(spDboption, true);
		System.out.println("===== extend ASE database & log on " + ip
				+ "succeed ====");
		ase.releaseCon();
		System.exit(0);
		// disk init
		// name='NW7_log_002',physname='/sybase/NW7/saplog_1/PI1_log_002.dat',size='20G'
		// disk init
		// name='NW7_data_002',physname='/sybase/NW7/saplog_1/PI1_data_002.dat',size='40G'
		// alter database NW7 on NW7_data_002='40G' log on NW7_log_002='20G'
		// checkpoint
		// sp_dboption NW7,'trunc log on chkpt',true

	}

}
