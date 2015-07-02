package pro1490;

import java.util.Scanner;

public class Main{
    public static char[] MyStrcat(String strone,String strtwo){
        char[] catStr = new char[strone.length()+strtwo.length()];
        for (int i = 0; i < strone.length(); i++) 
            catStr[i] = strone.charAt(i);
        for (int i =  strone.length(); i < catStr.length; i++) 
            catStr[i] = strtwo.charAt(i - strone.length()); 
        return catStr;
    }    
     
    public static void main(String[] args) {
     
        Scanner input = new Scanner(System.in);
         
        while(input.hasNext())
        {
            String strone = input.next();   //从缓存中读出数据
            String strtwo = input.next();   //从缓存中读出数据
            char[] strcat = MyStrcat(strone,strtwo);
            System.out.println(strcat);
        }
    }
}
/**************************************************************
    Problem: 1490
    User: langzimaizan
    Language: Java
    Result: Accepted
    Time:80 ms
    Memory:15560 kb
****************************************************************/