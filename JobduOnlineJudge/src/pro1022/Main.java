package pro1022;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
 
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] dataS = new int[101];
        int[] dataE = new int[101];
        int[] flag = new int[101];
        List<String> outList = new ArrayList<String> ();
        for (int i =0; i < 101; i ++) {
            flag[i] = dataS[i] = dataE[i] = 0;
        }
        while(scanner.hasNext()) {
            String str = scanner.nextLine();
            if(str.equals("-1")) {
                break;
            } else {
                if(str.contains("0 ")) {
                    int sum = 0, count =0;
                    for(int i = 1; i < 101; i++) {
                        if(flag[i] == 2) {
                            sum += dataE[i] - dataS[i];
                            count++;
                        }
                    }
                    if(count == 0) {
                        String temp = "0 0";
                        outList.add(temp);
                    } else {
                        String temp = count+" "+(int)((double)sum/count + 0.5);
                        outList.add(temp);
                    }
                    for (int i =0; i < 101; i ++) {
                        flag[i] = dataS[i] = dataE[i] = 0;
                    }
                } else {
                    String[] split = str.split(" ");
                    flag[Integer.valueOf(split[0])]++;
                    if(split[1].equals("S")) {
                        dataS[Integer.valueOf(split[0])]=toFen(split[2]);
                    } else {
                        dataE[Integer.valueOf(split[0])]=toFen(split[2]);
                    }
                }
            }
        }
         
        for(String s : outList) 
            System.out.println(s);
 
    }
     
    public static int toFen(String data) {
        String[] split = data.split(":");
        int x = 60 * Integer.valueOf(split[0]) + 
                Integer.valueOf(split[1]);
        return x;
    }
 
}
 
/**************************************************************
    Problem: 1022
    User: langzimaizan
    Language: Java
    Result: Accepted
    Time:80 ms
    Memory:15496 kb
****************************************************************/