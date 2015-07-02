package pro1095;

import java.util.*;
public class Main {
    public static void main(String args[]) {
        Scanner cin = new Scanner(System.in);
        int a;
        while (cin.hasNext()) {
            a = cin.nextInt(); 
            StringBuilder buffer = new StringBuilder();
            String aString = Integer.toBinaryString(a);
            for(int i = 0; i < aString.length(); i++) {
                if(aString.charAt(i) == '1') {
                    int num = aString.length()-i-1;
                    if(num == 1)
                        buffer.append("2+");
                    else
                    buffer.append("2(" + changeAllNum(num) + ")+");
                }
            }
            String result = buffer.subSequence(0, buffer.length()-1).toString();
            System.out.println(result);
        }
    }
    public static String changeAllNum (int i ) {
        if(i<=7)
            return changeNum(i);
        else {
            StringBuilder buff  = new StringBuilder("2(2+2(0))");
            if(i == 9)
                buff.append("+2(0)");
            if(i>9)
                buff.append("+"+changeAllNum(i-8));
            return buff.toString();
        }
         
    }
    public static String changeNum (int i ) {
        switch (i){
            case 7:return "2(2)+2+2(0)";
            case 6:return "2(2)+2";
            case 5:return "2(2)+2(0)";
            case 4:return "2(2)";
            case 3:return "2+2(0)";
            case 2:return "2";
            case 1:return "";
            case 0:return "0";
        }
        return "";
    }
}
/**************************************************************
    Problem: 1095
    User: langzimaizan
    Language: Java
    Result: Accepted
    Time:80 ms
    Memory:15476 kb
****************************************************************/
