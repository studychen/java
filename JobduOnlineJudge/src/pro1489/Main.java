package pro1489;

import java.util.Scanner;

public class Main{
    public static int MulThree(int a[],int b[]){
        int mul=0;
        for(int i = 1; i < 4; i++) {
            mul += a[i] * b[i];
        }
        return mul;
    }    
     
    public static void main(String[] args) {
     
        Scanner input = new Scanner(System.in);
         
        while(input.hasNext())
        {
            int[][] a3 = new int[3][4];
            for (int j = 1; j <3 ;j++)
                for(int i = 1; i < 4; i++)
                    a3[j][i] = input.nextInt();
            int[][] b2 = new int[3][4];
            for (int j = 1; j < 4 ;j++)
                for(int i = 1; i < 3; i++)
                    b2[i][j] = input.nextInt(); 
             
            System.out.print(MulThree(a3[1],b2[1])+" ");
            System.out.print(MulThree(a3[1],b2[2])+" ");
            System.out.println();
            System.out.print(MulThree(a3[2],b2[1])+" ");
            System.out.print(MulThree(a3[2],b2[2])+" ");
            System.out.println();
        }
    }
}
 
/**************************************************************
    Problem: 1489
    User: langzimaizan
    Language: Java
    Result: Accepted
    Time:120 ms
    Memory:19120 kb
****************************************************************/