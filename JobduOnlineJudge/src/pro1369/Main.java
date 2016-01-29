package pro1369;

import java.util.*;

import javax.rmi.CORBA.Util;
public class Main {
    public static void main(String args[]) {
        Scanner cin = new Scanner(System.in);
        String a;
        while (cin.hasNext()) {
            a = cin.next(); 
            char[] aChar = a.toCharArray();
            Arrays.sort(aChar);
            boolean[] bChar = new boolean[a.length()];
            Arrays.fill(bChar, true);
            dfs(0,a.length(),aChar,new char[a.length()],bChar);
        }
    }
     
    private static void dfs(int dep, int maxDep, char[] buf, char[] res,
            boolean[] flagArr) {
        if (dep == maxDep) {
            System.out.println(new String(res));
            return;
        }
        for (int i = 0; i < maxDep; i++) {
            if (flagArr[i]) {
                if (i != 0 && buf[i] == buf[i - 1] && flagArr[i - 1]) {
                    // 当有重复的时候
                    continue;
                }
                flagArr[i] = false;
                res[dep] = buf[i];
                dfs(dep + 1, maxDep, buf, res, flagArr);
                flagArr[i] = true;
            }
        }
    }
     
}
/**************************************************************
    Problem: 1369
    User: langzimaizan
    Language: Java
    Result: Accepted
    Time:2980 ms
    Memory:91744 kb
****************************************************************/
