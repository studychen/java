package pro1160;

import java.util.Scanner;

public class Main {
      public static int apple(int m,int n){
          if (m < n)
              return apple(m,m);
          else if ( n == 1 )
              return 1; 
          else if( m == 0 )
                 return 1;     
          else
              return apple(m,n-1)+apple(m-n,n);
           
      }    
    public static void main(String[] args) {
        Scanner shuru = new Scanner(System.in);
        while(shuru.hasNext())
        {
                int a = shuru.nextInt();
                for (int i = 0; i < a; i++)
                {
                        int m = shuru.nextInt();
                        int n = shuru.nextInt();
                        System.out.println(apple(m, n));
                }
        }
    }
}
 
/**************************************************************
    Problem: 1160
    User: langzimaizan
    Language: Java
    Result: Accepted
    Time:80 ms
    Memory:15464 kb
****************************************************************/