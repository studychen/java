package com.sap.xian;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by I302636 on 2015/7/1.
 */
public class Utility {
    public static void printLog(String logInfo){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date())+"\t"+logInfo);
    }
}
