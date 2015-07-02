package pro1133;

import java.util.Scanner;

public class Main {
    public static int point(int num){
        if (num >= 90)
            return 40;
        else if(num >= 85)
            return 37;
        else if(num >= 82)
            return 33;
        else if(num >= 78)
            return 30;
        else if(num >= 75)
            return 27;
        else if(num >= 72)
            return 23;
        else if(num >= 68)
            return 20;
        else if(num >= 64)
            return 15;
        else if(num >= 60)
            return 10;
        else
            return 0;
         
    }    
    public static void main(String[] args) {
        Scanner shuru = new Scanner(System.in);
        while(shuru.hasNext())
        {
                int n = shuru.nextInt();
                int a[] = new int[n+1];
                int b[] = new int[n+1];
                for (int i = 1; i<n+1; i++) {
                    a[i] = shuru.nextInt();
                }
                for (int i = 1; i<n+1; i++) {
                    b[i] = shuru.nextInt();
                }
                int sum = 0;int fen =0;
                for (int i = 1; i<n+1; i++) {
                    fen += a[i];
                    sum += a[ i ] * point(b[i]);
                }
                System.out.printf("%.2f\n",(double)sum/10.0/(double)fen);
        }
    }
}
 
/**************************************************************
    Problem: 1133
    User: langzimaizan
    Language: Java
    Result: Accepted
    Time:130 ms
    Memory:19056 kb
****************************************************************/