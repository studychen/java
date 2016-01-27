package db;

/**
 * 工程入口类
 * @author tomchen
 *
 */
public class ExtendAseMain {
	public static void main(String[] args) throws Exception {
		String ip = "";
		String port = "";
		String sid = ""; // for example PI1 NW7
		if (args.length == 3) {
			ip = args[0];
			port = args[1];
			// 转换为大写
			sid = args[2].toUpperCase();
		} else {
			throw new Exception("wrong agrs, usage: java -jar extendDb.jar <ip> <asePort> <sid>");
		}
		System.out.println("===== extend ASE database & log on " + ip + "start ====");

		// 初始的 log 和 data 文件是001结尾，或者不带后缀
		// 对安装完毕的 ASE 进行扩展，扩展的 log 和 data 文件是002结尾
		// log 文件，下面会指定其大小
		String logFile = sid + "_log_002";
		// data 文件，下面会指定其大小
		String dataFile = sid + "_data_002";

		// 文件存放的目录
		String phyFolder = "/sybase/" + sid;

		// 扩展 20G 的 log，40G 的 data
		String alterLog = "disk init name='" + logFile + "',physname='" + phyFolder + "/saplog_1/" + sid
				+ "_log_002.dat',size='20G'";
		String alterData = "disk init name='" + dataFile + "',physname='" + phyFolder + "/sapdata_1/" + sid
				+ "_data_002.dat',size='40G'";
		String alterFinal = "alter database " + sid + " on " + dataFile + "='40G' log on " + logFile + "='20G'";

		// 打印出执行的命令和 ip 信息
		System.out.println("===== " + alterLog + " on " + ip + " running =====");

		// 下面是在 master 数据库执行的命令
		// use master
		AseCommands ase = new AseCommands("**user**", "**passwd**", ip, port, sid);
		ase.excuteCom(alterLog, true);
		System.out.println("===== " + alterData + " on " + ip + " running =====");
		ase.excuteCom(alterData, true);
		System.out.println("===== " + alterFinal + " on " + ip + " running =====");
		ase.excuteCom(alterFinal, true);

		// use sid, 在 NW7 或者 PI1 执行的命令
		System.out.println("===== checkpoint on " + ip + " running =====");
		ase.excuteCom("checkpoint", false);

		// use master
		String spDboption = "sp_dboption " + sid + ",'trunc log on chkpt',true";
		System.out.println("===== " + spDboption + " on " + ip + " running =====");
		ase.excuteCom(spDboption, true);

		// 扩展结束，释放资源
		System.out.println("===== extend ASE database & log on " + ip + "succeed ====");
		ase.releaseCon();

		// return code 设为0，方便其他程序调用本工程
		System.exit(0);
		// origin commands to extend ASE:
		// disk init
		// name='NW7_log_002',physname='/sybase/NW7/saplog_1/PI1_log_002.dat',size='20G'
		// disk init
		// name='NW7_data_002',physname='/sybase/NW7/saplog_1/PI1_data_002.dat',size='40G'
		// alter database NW7 on NW7_data_002='40G' log on NW7_log_002='20G'
		// checkpoint
		// sp_dboption NW7,'trunc log on chkpt',true

	}

}
