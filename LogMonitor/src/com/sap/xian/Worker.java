package com.sap.xian;

import com.jcraft.jsch.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class Worker{
    String ip;
    String userName;
    String password;
    String cmd;
    String sid;
    boolean runStatus;

    public Worker(String ip,String userName,String password,String sid){
        this.ip=ip;
        this.userName=userName;
        this.password=password;
        this.sid=sid;
        this.cmd="tail -f /sybase/"+this.sid.toUpperCase()+"/ASE-16_0/install/"+this.sid.toUpperCase()+".log";
        this.runStatus=true;
    }

    public void runCmd() throws Exception{
        Session session=null;
        try {
            JSch jsch = new JSch();
            String hostAddress = this.ip;
            String user = this.userName;
            String passwd = this.password;

            session = jsch.getSession(user, hostAddress, 22);

            MyUserInfo ui = new MyUserInfo();

            session.setUserInfo(ui);
            session.setPassword(passwd);
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
                System.out.println("***********************************");
                if(outputByCmd.contains("Can't allocate space for object 'syslogs'")){
                    Utility.printLog("Log full on env "+this.ip+" detected, try to dump log");
                    new ASEUser("sapsa","Sybase123",this.ip,"4901",this.sid).dumpTranWithNoLog();
                }
                System.out.println(outputByCmd);
            }
            if (channel.isClosed()) {
                //System.out.println(channel.getExitStatus());
                if(channel.getExitStatus()==-1){
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
