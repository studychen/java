package pro1074;

public class Main {
    public static void main(String[] args) {
        int i=0;
        while (i<256){
            int flag = 0,temp=0;
            int ii = i*i;
            while (ii != 0) {
                temp = temp*10 + ii%10;
                ii = ii/10;
            }
            if (temp == i*i ) {
                flag = 1;
            }
            if(flag == 1) {
                System.out.println(i);
            }
            i++;
        }       
    }
}
/**************************************************************
    Problem: 1074
    User: langzimaizan
    Language: Java
    Result: Accepted
    Time:70 ms
    Memory:14512 kb
****************************************************************/