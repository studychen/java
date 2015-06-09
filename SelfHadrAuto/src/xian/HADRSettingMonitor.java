package xian;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by I302636 on 8/15/14.
 */
public class HADRSettingMonitor extends HADRUser {

    Vector<String[]> oneSiteInfo;
    Vector<String[]> otherSiteInfo;
    Vector<String[]> thirdSiteInfo;
    String oneSiteName;
    String otherSiteName;
    String thirdSiteName;
    String sap_sid;
    String RMAPort;

    String curPrimarySite;
    String curStandbySite;


    String thirdNodeName;
    Map<String,HashMap<String,String>> pathStatus;

    public HADRSettingMonitor(String userName,String password,String serverName,String port,String thirdNodeName){
        super(userName,password,serverName,port);
        this.RMAPort=port;
        this.thirdNodeName=thirdNodeName;
        oneSiteInfo =new Vector<String[]>();
        otherSiteInfo =new Vector<String[]>();
        thirdSiteInfo = new Vector<String[]>();
        //The order below should now be changed.
        getServerSiteInfo();
        updateSysInfo();


//        System.out.println("Pri:"+curPrimarySite);
//        System.out.println("Sec:"+curStandbySite);
//        System.out.println("3rd node:"+thirdNodeName);
        //printSysInfo();
    }

    public String retrieveInfoFromSiteInfo(String keyInfo){
        String value="";

        //Chech from serverInfo to find the info your want.
        //For example: keyInfo=D0, ase_port
        for(int i=0;i<oneSiteInfo.size();i++){
            if(oneSiteInfo.get(i)[0].equals(keyInfo)){
                return oneSiteInfo.get(i)[1];
            }
        }

        for(int i=0;i<otherSiteInfo.size();i++){
            if(otherSiteInfo.get(i)[0].equals(keyInfo)){
                return otherSiteInfo.get(i)[1];
            }
        }

        for(int i=0;i<thirdSiteInfo.size();i++){
            if(thirdSiteInfo.get(i)[0].equals(keyInfo)){
                return thirdSiteInfo.get(i)[1];
            }
        }

        return value;
    }

    public void updateSysInfo(){
        String sqlCmd="sap_status path";
        Vector<String[]> tmpInfo=new Vector<String[]>();
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCmd);
            ResultSetMetaData rsMetaData=rs.getMetaData();
            Integer totalCols=rsMetaData.getColumnCount();
            while (rs.next()) {
                String[] info=new String[totalCols];
                for(int i=1;i<=totalCols;i++){
                    String value=rs.getString(i);
                    info[i-1]=value;
                }
                tmpInfo.add(info);
            }
            rs.close();
            stmt.close();
        }catch(Exception exp){
            exp.printStackTrace();
        }

        HashMap<String,String> statusOfSiteOne=pathStatus.get(oneSiteName);
        HashMap<String,String> statusOfSiteTwo=pathStatus.get(otherSiteName);
        HashMap<String,String> statusOfSiteThree=pathStatus.get(thirdSiteName);

        HashMap<String,String> repStatusOfSiteOne=pathStatus.get(oneSiteName);
        HashMap<String,String> repStatusOfSiteTwo=pathStatus.get(otherSiteName);
        HashMap<String,String> repStatusOfSiteThree=pathStatus.get(thirdSiteName);
        HashMap<String,String> tmpMapC1=pathStatus.get(oneSiteName+"."+otherSiteName+"."+sap_sid);
        HashMap<String,String> tmpMapC2=pathStatus.get(oneSiteName+"."+otherSiteName+"."+"master");
        HashMap<String,String> tmpMapD1=pathStatus.get(otherSiteName+"."+oneSiteName+"."+sap_sid);;
        HashMap<String,String> tmpMapD2=pathStatus.get(otherSiteName+"."+oneSiteName+"."+"master");;


        //Find the Primary and Standby:
        for(int i=2;i<tmpInfo.size();i++){
            if(tmpInfo.get(i)[1].trim().equals("HADR Status")){
                if(tmpInfo.get(i)[2].trim().equals("Primary : Active")){
                    curPrimarySite=tmpInfo.get(i)[0];
                }else if(tmpInfo.get(i)[2].trim().equals("Standby : Inactive")){
                    if(!tmpInfo.get(i)[0].equals(thirdNodeName)){
                        curStandbySite=tmpInfo.get(i)[0];
                    }
                }
            }

            //Update status:
            try{
                pathStatus.get(tmpInfo.get(i)[0]).put(tmpInfo.get(i)[1],tmpInfo.get(i)[2]);
            }catch(Exception exp){
                System.err.println("There are record can't be hanned! exit...");
                throw new RuntimeException("Code error, exit...");
            }
        }
    }

    public void printSysInfo(){
        System.out.println("********************************************************");
        for(String key:pathStatus.keySet()){
            System.out.println(key + "***************************");
            for(String key2:pathStatus.get(key).keySet()){
                System.out.print(key2+"\t\t");
                System.out.println(pathStatus.get(key).get(key2));
            }
        }
    }

    private void getServerSiteInfo(){
        String sqlCmd="sap_set";
        Vector<String[]> tmpInfo=new Vector<String[]>();
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCmd);
            ResultSetMetaData rsMetaData=rs.getMetaData();
            Integer totalCols=rsMetaData.getColumnCount();

            while (rs.next()) {
                String[] info=new String[totalCols];
                for(int i=1;i<=totalCols;i++){

                    String value=rs.getString(i);
                    info[i-1]=value;
                }
                tmpInfo.add(info);
            }
            rs.close();
            stmt.close();
        }catch(Exception exp){
            exp.printStackTrace();
        }

        sap_sid=tmpInfo.get(1)[1].trim();

        int nItem=(tmpInfo.size()-4)/3;     //There are 3 group info, so we divide it into 3 groups
        for(int i=0;i<nItem;i++){
            oneSiteInfo.add(tmpInfo.get(i + 4));
            otherSiteInfo.add(tmpInfo.get(nItem + i + 4));
            thirdSiteInfo.add(tmpInfo.get(nItem * 2 + i + 4));
        }

        oneSiteName = oneSiteInfo.get(0)[0].split(",")[0].trim();
        otherSiteName = otherSiteInfo.get(0)[0].split(",")[0].trim();
        thirdSiteName = thirdSiteInfo.get(0)[0].split(",")[0].trim();       //The thirdSiteName don't mean the "3rd node" in HADR env, just the thirdSite's Name


        constructPathStatus(sap_sid,oneSiteName,otherSiteName,thirdSiteName);

    }

    private void constructPathStatus(String sap_sid,String oneSiteName,String otherSiteName,String thirdSiteName) {
        pathStatus=new HashMap<String,HashMap<String,String>>();
        HashMap<String,String> statsRecordForSiteOne=new HashMap<String,String>();
        HashMap<String,String> statsRecordForSiteTwo=new HashMap<String,String>();
        HashMap<String,String> statsRecordForSiteThree=new HashMap<String,String>();
        ArrayList< HashMap<String,String>> arrayOfHADRStatusMap=new ArrayList<HashMap<String, String>>();
        arrayOfHADRStatusMap.add(statsRecordForSiteOne);
        arrayOfHADRStatusMap.add(statsRecordForSiteTwo);
        arrayOfHADRStatusMap.add(statsRecordForSiteThree);
        for(HashMap<String,String> tmp:arrayOfHADRStatusMap){
            tmp.put("Hostname", "");
            tmp.put("HADR Status", "");
            tmp.put("Synchronization Mode", "");
            tmp.put("Synchronization State", "");
            tmp.put("Distribution Mode", "");
            tmp.put("Hostname", "");
            tmp.put("HADR Status", "");
            tmp.put("Synchronization Mode", "");
            tmp.put("Synchronization State", "");
            tmp.put("Distribution Mode", "");
        }

        pathStatus.put(oneSiteName, statsRecordForSiteOne);
        pathStatus.put(otherSiteName,statsRecordForSiteTwo);
        pathStatus.put(thirdSiteName,statsRecordForSiteThree);
        ArrayList<String> keysForRepStatus=new ArrayList<String>();

        keysForRepStatus.add(oneSiteName+"."+otherSiteName+"."+sap_sid);
        keysForRepStatus.add(oneSiteName+"."+thirdSiteName+"."+sap_sid);
        keysForRepStatus.add(oneSiteName+"."+otherSiteName+".master");
        keysForRepStatus.add(oneSiteName+"."+thirdSiteName+".master");

        keysForRepStatus.add(otherSiteName+"."+oneSiteName+"."+sap_sid);
        keysForRepStatus.add(otherSiteName+"."+thirdSiteName+"."+sap_sid);
        keysForRepStatus.add(otherSiteName+"."+oneSiteName+".master");
        keysForRepStatus.add(otherSiteName+"."+thirdSiteName+".master");

        keysForRepStatus.add(thirdSiteName+"."+oneSiteName+"."+sap_sid);
        keysForRepStatus.add(thirdSiteName+"."+otherSiteName+"."+sap_sid);
        keysForRepStatus.add(thirdSiteName+"."+oneSiteName+".master");
        keysForRepStatus.add(thirdSiteName+"."+otherSiteName+".master");

        for(String keyForOneRepStatus:keysForRepStatus){
            HashMap<String,String> oneRepStatus=new HashMap<String,String>();
            oneRepStatus.put("State","");
            oneRepStatus.put("Latency Time","");
            oneRepStatus.put("Latency","");
            oneRepStatus.put("Commit Time","");
            oneRepStatus.put("Distribution Path","");
            pathStatus.put(keyForOneRepStatus, oneRepStatus);
        }

    }

    public void toSync() {
        updateSysInfo();
        String cmdChangePri="sap_update_replication synchronization_mode "+curPrimarySite+", sync";
        String cmdChangeSec="sap_update_replication synchronization_mode "+curStandbySite+", sync";
        this.getResultBySQLCmd(cmdChangePri);
        this.getResultBySQLCmd(cmdChangeSec);
        waitForToken();
    }

    private void waitForToken() {
        //Run updateSysInfo() before run this command.
        //Current Pri=curPrimarySite
        HADRUser tmpPriDRAdmin=new HADRUser("DR_admin","Sybase123",pathStatus.get(curPrimarySite).get("Hostname"),this.retrieveInfoFromSiteInfo(curPrimarySite+", dr_plugin_port"));
        HADRUser tmpSecSapsa=new HADRUser("sapsa","Sybase123",pathStatus.get(curStandbySite).get("Hostname"),this.retrieveInfoFromSiteInfo(curStandbySite+", ase_port"));
        String sendTraceCmd="sap_send_trace "+curPrimarySite;
        //First clean <SID>.rs_ticket_history
        tmpSecSapsa.getResultOfDBTable("use "+sap_sid,"truncate table rs_ticket_history","NORET");
        tmpPriDRAdmin.getResultBySQLCmd(sendTraceCmd);
        //Check whether the trace is get or not

        boolean tokenGet=false;
        int countOfToken=0;
        while(!tokenGet){
            try{
                Thread.sleep(3000);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            if(tmpSecSapsa.getResultOfDBTable("use "+sap_sid,"select * from rs_ticket_history","HASRET")>0){
                System.out.println("Token get from standby,the route is active");
                tokenGet=true;
            }
            countOfToken++;
            if(countOfToken>10){
                System.out.println("Error!Can't get token from standby");
                throw new RuntimeException("Error!Can't get token from standby");
            }
        }
    }

    public void toAsync() {
        checkHADRStatus();  //Check status to make sure the env is OK before we do test.
        String cmdChangePri="sap_update_replication synchronization_mode "+curPrimarySite+", async";
        String cmdChangeSec="sap_update_replication synchronization_mode "+curStandbySite+", async";
        this.getResultBySQLCmd(cmdChangePri);
        this.getResultBySQLCmd(cmdChangeSec);
        waitForToken();
        checkHADRStatus();  //Check status to make sure the env is OK after we do test.
    }

    public void toNearSync() {
        checkHADRStatus();
        String cmdChangePri="sap_update_replication synchronization_mode "+curPrimarySite+", nearsync";
        String cmdChangeSec="sap_update_replication synchronization_mode "+curStandbySite+", nearsync";
        this.getResultBySQLCmd(cmdChangePri);
        this.getResultBySQLCmd(cmdChangeSec);
        waitForToken();
        checkHADRStatus();
    }

    public void toRemote() {
        checkHADRStatus();
        String cmdChangePri="sap_update_replication distribution_mode "+curPrimarySite+", remote, "+curStandbySite;
        String cmdChangeSec="sap_update_replication distribution_mode "+curStandbySite+", remote, "+curPrimarySite;
        this.getResultBySQLCmd(cmdChangePri);
        this.getResultBySQLCmd(cmdChangeSec);
        waitForToken();
        checkHADRStatus();
    }

    public void toLocal() {
        checkHADRStatus();
        String cmdChangePri="sap_update_replication distribution_mode "+curPrimarySite+", local";
        String cmdChangeSec="sap_update_replication distribution_mode "+curStandbySite+", local";
        this.getResultBySQLCmd(cmdChangePri);
        this.getResultBySQLCmd(cmdChangeSec);
        checkHADRStatus();
    }

    public void failover(String curPrimary, String curStandby, String timeout,String otherParam) {
        checkHADRStatus();  //Make sure env is OK before failover, the env is not OK after failover, so we should not check env status after failover
        String cmd="";
        if(otherParam==null||otherParam.toLowerCase().equals("")){
            cmd="sap_failover "+curPrimary+","+curStandby+","+timeout;
        }else if(otherParam.toLowerCase().equals("unplanned")){
            cmd="sap_failover "+curPrimary+","+curStandby+","+timeout+" unplanned";
        }else{
            cmd="sap_failover "+curPrimary+","+curStandby+","+timeout+","+otherParam;
        }
        this.getResultBySQLCmd(cmd);
        this.waitUntilFinished();
        this.getResultBySQLCmd(cmd);
    }

    public void sapHostRemaining() {
        String cmd="sap_failover_remaining 120";
        this.getResultBySQLCmd(cmd);
        waitForToken();
    }

    public void sapHostAvailable(String curPrimary) {
        String cmd="sap_host_available "+curPrimary;
        this.getResultBySQLCmd(cmd);
    }

    public void checkHADRStatus(){
        updateSysInfo();
        String primary=this.curPrimarySite;
        String standby=this.curStandbySite;
        String thirdNode=this.thirdNodeName;
        String sid=this.sap_sid.toUpperCase();
        ArrayList<String> repShouldActive=new ArrayList<String>();
        ArrayList<String> repShouldSuspended=new ArrayList<String>();
        repShouldActive.add(primary+"."+standby+".master");
        repShouldActive.add(primary+"."+thirdNode+".master");
        repShouldActive.add(primary+"."+standby+"."+sid);
        repShouldActive.add(primary+"."+thirdNode+"."+sid);

        repShouldSuspended.add(standby+"."+primary+".master");
        repShouldSuspended.add(standby+"."+thirdNode+".master");
        repShouldSuspended.add(standby+"."+primary+"."+sid);
        repShouldSuspended.add(standby+"."+thirdNode+"."+sid);

        repShouldSuspended.add(thirdNode+"."+primary+".master");
        repShouldSuspended.add(thirdNode+"."+standby+".master");
        repShouldSuspended.add(thirdNode+"."+primary+"."+sid);
        repShouldSuspended.add(thirdNode+"."+standby+"."+sid);

        for(String key:repShouldActive){
            if(!pathStatus.get(key).get("State").trim().equals("Active")){
                throw new RuntimeException("HADR status is WRONG! Check the env before continue!");
            }
        }

        for(String key:repShouldSuspended){
            if(!pathStatus.get(key).get("State").trim().equals("Suspended")){
                throw new RuntimeException("HADR status is WRONG! Check the env before continue!");
            }
        }
    }

}
