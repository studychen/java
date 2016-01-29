package pro1515;

import java.util.*;
public class Main {
    public static void main(String args[]) {
        Scanner cin = new Scanner(System.in);
        int a;
        while (cin.hasNextInt()) {
            a = cin.nextInt(); 
            for(int i =1; i < Math.pow(10, a); i++)
                System.out.println(i);
        }
    }
     
     
}
 
/**************************************************************
    Problem: 1515
    User: langzimaizan
    Language: Java
    Result: Accepted
    Time:1640 ms
    Memory:39148 kb
****************************************************************/
