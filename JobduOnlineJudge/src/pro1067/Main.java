package pro1067;

import java.util.Scanner;

public class Main {
    public static long count(int num){
        if (num == 1)
            return 1; 
        else
            return num*count(num-1);
    }    
     
    public static void main(String[] args) {
        Scanner shuru = new Scanner(System.in);
        while(shuru.hasNext())
        {
                int n = shuru.nextInt();
                System.out.println(count(n));
        }
    }
}
 
/**************************************************************
    Problem: 1067
    User: langzimaizan
    Language: Java
    Result: Accepted
    Time:90 ms
    Memory:15480 kb
****************************************************************/
