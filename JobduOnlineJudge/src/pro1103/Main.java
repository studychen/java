package pro1103;

import java.util.Scanner;
import java.math.*;
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  
 
public class Main{
    public static void MulThree(int a,int b,int c){
        int derta = b*b - 4*a*c;
        if (derta < 0)
            System.out.println("No Solution");
        else {
            float x1 = (float) ((-b + Math.sqrt(derta)) / (float)(2*a));
            float x2 = (float) ((-b - Math.sqrt(derta)) / (float)(2*a));
            if(x1 > x2) {
                float temp = x1;
                x1 = x2;
                x2 = temp;
            }
            System.out.printf("%.2f %.2f\n",x1,x2);
        }
    }    
     
    public static int Countx2(String x2){
        int a;
        if ( x2.equals("x^2") || x2.equals("+x^2") || x2.equals("=x^2") )
            a = 1;
        else if (x2.equals("-x^2"))
            a = -1;
        else {
            String newx2 = x2.substring(0, x2.length()-3);
            boolean isNum = newx2.matches("\\d+");
            if (isNum) 
                a = Integer.parseInt(newx2);
            else  {
                char judge = newx2.charAt(0);
                int mulJud = 1;
                if (judge == '-')
                    mulJud = -1;
                String newnewx2 = newx2.substring( 1, newx2.length()) ;
                 
                a = mulJud * Integer.parseInt(newnewx2);
            }
        }
        return a;
    } 
     
    public static int Countx(String x){
        int b;
        if ( x.equals("x") || x.equals("+x") || x.equals("=x") )
            b = 1;
        else if (x.equals("-x"))
            b = -1;
        else {
            String newx = x.substring(0, x.length()-1);
            boolean isNum = newx.matches("\\d+");
            if (isNum) 
                b = Integer.parseInt(newx);
            else  {
                char judge = newx.charAt(0);
                int mulJud = 1;
                if (judge == '-')
                    mulJud = -1;
                String newnewx2 = newx.substring( 1, newx.length()) ;   
                b = mulJud * Integer.parseInt(newnewx2);
            }
        }
        return b;
    }
     
    public static int CountConstant(String con){
        int c;
        boolean isNum = con.matches("\\d+");
        if (isNum) 
            c = Integer.parseInt(con);
        else  {
            char judge = con.charAt(0);
            int mulJud = 1;
            if (judge == '-')
                mulJud = -1;
            String newc = con.substring(1, con.length());
            c = mulJud * Integer.parseInt(newc);
        }
        return c;
    }
     
    public static void main(String[] args) {
     
        Scanner input = new Scanner(System.in);
         
        while(input.hasNext())
        {
            int a=0;int b=0;int c=0;
            String giveString = input.next();
            String[] splitString = giveString.split("=");
            String textString = splitString[0];
            String removeAString = textString ; 
            String removeBString = textString;
             Pattern apt=Pattern.compile("[+|\\-|=]{0,1}\\d*x\\^2");  
             Matcher  amatcher = apt.matcher(textString);
             while (amatcher.find()) {     
                 String sensitive = amatcher.group();  
                 removeAString = removeAString.replaceFirst("[+|\\-|=]{0,1}\\d*x\\^2", "");
                 a += Countx2(sensitive);
             }  
              
             removeBString = removeAString;
              
             Pattern bpt=Pattern.compile("[+|\\-|=]{0,1}\\d*x");  
             Matcher  bmatcher = bpt.matcher(removeAString);
             while (bmatcher.find()) {     
                 String sensitive = bmatcher.group();  
                 removeBString = removeBString.replaceFirst("[+|\\-|=]{0,1}\\d*x", "");
                 b += Countx(sensitive);
             }   
              
             Pattern cpt=Pattern.compile("[+|\\-|=]{0,1}\\d+");  
             Matcher  cmatcher = cpt.matcher(removeBString);
             while (cmatcher.find()) {     
                 String sensitive = cmatcher.group();  
                 c += CountConstant(sensitive);
             }   
              
            textString = splitString[1];
            removeAString = textString ; 
            removeBString = textString;
              
            amatcher = apt.matcher(textString);
            while (amatcher.find()) {     
                  String sensitive = amatcher.group();  
                  removeAString = removeAString.replaceFirst("[+|\\-|=]{0,1}\\d*x\\^2", "");
                  a -= Countx2(sensitive);
              }  
               
              removeBString = removeAString;
              bmatcher = bpt.matcher(removeAString);
              while (bmatcher.find()) {     
                  String sensitive = bmatcher.group();  
                  removeBString = removeBString.replaceFirst("[+|\\-|=]{0,1}\\d*x", "");
      
                  b -= Countx(sensitive);
              }   
               
            
              cmatcher = cpt.matcher(removeBString);
              while (cmatcher.find()) {     
                  String sensitive = cmatcher.group();  
                  c -= CountConstant(sensitive);
              }   
               
             //System.out.println("a="+a+";"+"b="+b+";"+"c="+c);
             MulThree(a,b,c);
        }
    }
}
 
/**************************************************************
    Problem: 1103
    User: langzimaizan
    Language: Java
    Result: Accepted
    Time:90 ms
    Memory:15732 kb
****************************************************************/