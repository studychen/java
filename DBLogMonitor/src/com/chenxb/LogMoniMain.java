package com.chenxb;

import java.nio.file.*;

/**
 * 工程入口类
 * @author tomchen
 *
 */
public class LogMoniMain {
	/**
	 * 
	 * @param args 远程机器ip，用户名，密码，sid
	 */
	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("Usage:\nRun: java -jar TargetPrimaryHost userName Password SID");
			throw new RuntimeException("Wrong input parameters");
		} else {
			try {
				Worker test = new Worker(args[0], args[1], args[2], args[3]);
				test.runCmd();
			} catch (Exception exp) {
				throw new RuntimeException("Start Monitor Log failed!");
			}
		}
	}

}