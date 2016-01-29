package pro1173;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner shuru = new Scanner(System.in);
        while(shuru.hasNext())
        {
                int n = shuru.nextInt();
                int a[] = new int[n+1];
                for (int i = 1; i<n+1; i++) {
                    a[i] = shuru.nextInt();
                }
                int m = shuru.nextInt();
                int b[] = new int[m+1];
                for (int i = 1; i<m+1; i++) {
                    b[i] = shuru.nextInt();
                }
                for (int i = 1; i<m+1; i++) {
                    int j;
                    for (j = 1; j<n+1; j++) {
                        if (b[i] == a[j]) {
                            System.out.printf("YES\n");
                            break;
                        }
                    }
                    if (j == n+1 )
                        System.out.printf("NO\n");
                }
        }
    }
}
 
/**************************************************************
    Problem: 1173
    User: langzimaizan
    Language: Java
    Result: Accepted
    Time:80 ms
    Memory:15484 kb
****************************************************************/
