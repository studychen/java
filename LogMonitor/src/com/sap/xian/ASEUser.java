package com.sap.xian;

import java.sql.Statement;

/**
 * Created by I302636 on 2015/6/10.
 */
public class ASEUser extends HADRUser {
    String sid;
    public ASEUser(String userName, String password, String serverName, String port,String sid){
        super(userName,password,serverName,port);
        this.sid=sid;
    }

    public void dumpTranWithNoLog(){
        try{
            Statement stmt = connection.createStatement();
            stmt.execute("use master");
            stmt.execute("dump tran "+this.sid.toUpperCase()+" with no_log");
            stmt.close();
        }catch(Exception exp){
            exp.printStackTrace();
        }
    }

    public void promoteASEtoPrimary(){
        try{
            Statement stmt = connection.createStatement();
            stmt.execute("use master");
            stmt.execute("sp_hadr_admin primary,'force'");
            stmt.execute("sp_hadr_admin activate");
            stmt.close();
        }catch(Exception exp){
            exp.printStackTrace();
        }
    }

    public void startRAT(String database) {
        try{
            Statement stmt = connection.createStatement();
            stmt.execute("use master");
            stmt.execute("sp_start_rep_agent "+database);
            stmt.close();
        }catch(Exception exp){
            exp.printStackTrace();
        }
    }

    public void degradeASEtoStandby() {
        try{
            Statement stmt = connection.createStatement();
            stmt.execute("use master");
            stmt.execute("sp_hadr_admin 'deactivate','120','test','force'");
            stmt.execute("sp_hadr_admin standby");
            stmt.close();
        }catch(Exception exp){
            exp.printStackTrace();
        }
    }

    public void shutdown() {
        try{
            Statement stmt = connection.createStatement();
            stmt.execute("shutdown");
        }catch(Exception exp){
            //exp.printStackTrace();
        }
    }

    public void stopRat(String database) {
        try{
            Statement stmt = connection.createStatement();
            stmt.execute("use master");
            stmt.execute("sp_stop_rep_agent "+database);
            stmt.close();
        }catch(Exception exp){
            exp.printStackTrace();
        }
    }
}
