package connect;

import com.jcraft.jsch.*;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JOptionPane;

/**
 * 更多信息查看这篇文章
 * http://www.programcreek.com/java-api-examples/index.php?api=com.jcraft.jsch.
 * ChannelExec
 * 
 * @author tomchen
 *
 */
public class WinMachine {
	// ssh 端口通常为22
	// windows 默认不会安装 SSH 服务
	// windows 打开ssh需要利用 freessh 或者 openssh
	private static final int port = 22;
	private String user;
	private String passwd;
	private String ip;

	public WinMachine(String user, String passwd, String ip) {
		this.user = user;
		this.passwd = passwd;
		this.ip = ip;
	}

	public void runCmd(String cmd) throws Exception {
		Session session = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(user, ip, port);
			SSHUserInfo ui = new SSHUserInfo();
			session.setUserInfo(ui);
			session.setPassword(passwd);
			session.connect();
			String sudo_pass = "";
			executeCmd(cmd, session, sudo_pass);
			session.disconnect();
		} catch (Exception e) {
			System.out.println("Error happened in remote command exe phase!");
			System.out.println(e);
			session.disconnect();
		}
	}

	private void executeCmd(String command, Session session, String sudo_pass) throws JSchException, IOException {
		Channel channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand(command);

		InputStream in = channel.getInputStream();
		OutputStream out = channel.getOutputStream();
		((ChannelExec) channel).setErrStream(System.err);

		channel.connect();

		out.write((sudo_pass + "\n").getBytes());
		out.flush();
		out.write("ls -al".getBytes());

		byte[] tmp = new byte[10240];
		while (true) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 10240);
				if (i < 0)
					break;
				String outputByCmd = new String(tmp, 0, i).trim();
				System.out.println(outputByCmd);
			}
			if (channel.isClosed()) {
				if (channel.getExitStatus() != 0) {
					System.out.println("Remote command execute failed!!!");
					channel.disconnect();
					throw new RuntimeException("Remote command execute failed!!!");
				} else {
				}
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (Exception ee) {
			}
		}
		channel.disconnect();
	}

	public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {

		String passwd;

		public String getPassword() {
			return passwd;
		}

		public boolean promptYesNo(String str) {
			return true;
		}

		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return true;
		}

		public boolean promptPassword(String message) {
			return true;
		}

		@Override
		public String[] promptKeyboardInteractive(String arg0, String arg1, String arg2, String[] arg3,
				boolean[] arg4) {
			return null;
		}

		@Override
		public void showMessage(String arg0) {
		}

	}
}
