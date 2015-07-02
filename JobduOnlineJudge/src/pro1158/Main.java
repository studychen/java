package pro1158;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner shuru = new Scanner(System.in);
        while(shuru.hasNext())
        {
                int N = shuru.nextInt();
                int K = shuru.nextInt();
                int flag = 0;
                for (int i = 0; i<20; i++) {
                    if (N*(i+1) >= 200 * Math.pow(1.0+K/100.0,i)) {
                        System.out.println(i+1);
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0)
                    System.out.printf("Impossible\n");
        }
    }
}
 
/**************************************************************
    Problem: 1158
    User: langzimaizan
    Language: Java
    Result: Accepted
    Time:90 ms
    Memory:15556 kb
****************************************************************/