package com.sap.xian;

import java.nio.file.*;

/**
 * Created by I302636 on 2015/7/1.
 */
public class LogMoniMain {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage:\nRun: java -jar TargetPrimaryHost userName Password SID");
            throw new RuntimeException("Wrong input parameters");
        }else{
            try{
                Worker test=new Worker(args[0],args[1],args[2],args[3]);
                test.runCmd();
            }catch (Exception exp){
                throw new RuntimeException("Start SAP failed!");
            }
        }
    }

}