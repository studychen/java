package pro1151;

import java.util.Scanner;

public class Main {
    public static char left(char num, int n){
        return (char)(num<<n | num>>(16-n));
    }
     
    public static void main(String[] args) {
        Scanner shuru = new Scanner(System.in);
        while(shuru.hasNext())
        {
                int n = shuru.nextInt();
                
                for (int i = 1; i<n+1; i++) {
                    char a = (char)shuru.nextInt();
                    char b = (char)shuru.nextInt();
                    int j;
                    for (j =0; j < 16; j++) 
                        if (a == left(b, j)) {
                            System.out.printf("YES\n");
                            break;
                        }
                    if (j == 16)
                        System.out.printf("NO\n");
                }
        }
    }
}
 
/**************************************************************
    Problem: 1151
    User: langzimaizan
    Language: Java
    Result: Accepted
    Time:90 ms
    Memory:15500 kb
****************************************************************/