package pro1072;

public class Main {
    public static void main(String[] args) {
        double a=0.0,b=0.0,c=1.0;
        double[] s= new double[200];
        for (int i=0; i<200; i++)
            s[i] = 0;
        int x =0,num=0;
     
        while (a<6) {
            while (b<5) {
                while (c<7) {
                    int flag = 0;
                    for (int i=0; i< 200;i++){
                        if ( s[i] == c*18 + b*10 +a*8 )
                            flag = 1;
                    }
                    if( flag == 0 )
                        if ( x < 200 )
                            s[x++] = c*18 + b*10 +a*8 ;
                     
                    c++;
                }
                b++;
                c = 0.0;
            }
            a++;
            b=0.0;
        }
        System.out.println(x);
    }
}
/**************************************************************
    Problem: 1072
    User: langzimaizan
    Language: Java
    Result: Accepted
    Time:80 ms
    Memory:15864 kb
****************************************************************/