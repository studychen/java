package com.chenxb;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author tomchen
 *
 */
public class Utility {
    public static void printLog(String logInfo){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date())+"\t"+logInfo);
    }
}
