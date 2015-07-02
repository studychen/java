package pro1059;

public class Main {
    public static void main(String[] args) {
        int a=0,b=0,c=0;
        while (a<10) {
            while (b<10) {
                while (c<10) {
                    if ((a*10 + b + b*10 +c)*10 + c*2 == 532) 
                        System.out.printf("%d %d %d\n",a,b,c);
                    c++;
                }
                b++;
                c = 0;
            }
            a++;
            b=0;
        }
    }
}
 
/**************************************************************
    Problem: 1059
    User: langzimaizan
    Language: Java
    Result: Accepted
    Time:70 ms
    Memory:14760 kb
****************************************************************/