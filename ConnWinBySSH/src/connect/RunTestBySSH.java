package connect;

/**
 * 本工程入口类
 * 
 * @author tomchen
 *
 */
public class RunTestBySSH {

	/**
	 * 参数也可以包含远程机器的账号和密码
	 * 
	 * @param args
	 *            待执行的命令
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage:\nRun: java -jar runTestOnWinBySSH <cmd>");
			throw new RuntimeException("Wrong input parameters");
		} else {
			String para = args[0];
			try {
				// windows 机器的账号和密码也可以从参数中获取
				// windows 机器的账号
				String user = "**user**";
				// windows 机器的密码
				String passwd = "**passwd**";
				// ip 信息，10.173.**.**
				String ip = "10.173.**.***";
				// 命令信息，调用 python 执行脚本，可以hardcode 或者给参数
				String cmd = "C:/Python26/python.exe C:/runTestScripts/testOne.py ip databseName " + para;

				WinMachine test = new WinMachine(user, passwd, ip);

				test.runCmd(cmd);
			} catch (Exception exp) {
				throw new RuntimeException("Cmd run failed!!");
			}
		}
	}
}
