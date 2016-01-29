package pro1510;

import java.util.*;
public class Main {
    public static void main(String args[]) {
        Scanner cin = new Scanner(System.in);
        String a;
        while (cin.hasNextLine()) {
            a = cin.nextLine(); 
            System.out.println(a.replaceAll("\\s", "%20"));
        }
    }
     
     
}
/**************************************************************
    Problem: 1510
    User: langzimaizan
    Language: Java
    Result: Accepted
    Time:950 ms
    Memory:47488 kb
****************************************************************/