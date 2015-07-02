package pro1000;

import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException {
		Scanner reader = new Scanner(System.in);
		while (reader.hasNextInt()) {
			System.out.println(reader.nextInt() + reader.nextInt());
		}
	}
}

/**************************************************************
 * Problem: 1000 User: langzimaizan Language: Java Result: Accepted Time:80 ms
 * Memory:15468 kb
 ****************************************************************/
