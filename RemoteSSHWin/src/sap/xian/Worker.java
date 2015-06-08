package sap.xian;

import com.jcraft.jsch.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class Worker {
    String user;
    String passwd;
    String ip;
    String cmd;

    public Worker(String user,String passwd,String ip, String cmd){
        this.user=user;
        this.passwd=passwd;
        this.ip=ip;
        this.cmd=cmd;
    }

    public void runCmd() throws Exception{
        Session session=null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(this.user, this.ip, 22);
            MyUserInfo ui = new MyUserInfo();
            session.setUserInfo(ui);
            session.setPassword(this.passwd);
            session.connect();
            String sudo_pass = "";
            executeCmd(this.cmd,session,sudo_pass);

            session.disconnect();
        } catch (Exception e) {
            System.out.println("Error happened in remote command exe phase!");
            System.out.println(e);
            session.disconnect();
        }
    }
    
    public void runCmd(String cmd) throws Exception{
        Session session=null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(this.user, this.ip, 22);
            MyUserInfo ui = new MyUserInfo();
            session.setUserInfo(ui);
            session.setPassword(this.passwd);
            session.connect();
            String sudo_pass = "";
            executeCmd(cmd,session,sudo_pass);

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
                if (i < 0) break;
                String outputByCmd = new String(tmp, 0, i).trim();
                System.out.println(outputByCmd);
            }
            if (channel.isClosed()) {
                if(channel.getExitStatus()!=0){
                    System.out.println("Remote command execute failed!!!");
                    channel.disconnect();
                    throw new RuntimeException("Remote command execute failed!!!");
                }else{
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

        public void showMessage(String message) {
            JOptionPane.showMessageDialog(null, message);
        }

        final GridBagConstraints gbc =
                new GridBagConstraints(0, 0, 1, 1, 1, 1,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0);
        private Container panel;

        public String[] promptKeyboardInteractive(String destination,
                                                  String name,
                                                  String instruction,
                                                  String[] prompt,
                                                  boolean[] echo) {
            return null;
        }
    }
}
