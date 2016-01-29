package pro1013;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			int num = scanner.nextInt();
			String[][] mark = new String[num][];
			String[] out = new String[num];
			for (int i = 0; i < num; i++) {
				int numerery = scanner.nextInt();
				scanner.nextLine();
				mark[i] = new String[numerery];
				for (int j = 0; j < numerery; j++) {
					mark[i][j] = scanner.nextLine();
				}
				out[i] = deal(mark[i]);
			}
			for (String str : out) {
				System.out.println(str);
			}

		}

	}

	public static String deal(String[] data) {
		StringBuilder strBuilder = new StringBuilder();
		int open = 0, close = 0;
		String[] split = data[0].split("\\s+");
		String strOpen = split[1];
		String strClose = split[2];
		for (int i = 1; i < data.length; i++) {
			String[] tempSplit = data[i].split("\\s+");
			if (tempSplit[1].compareTo(strOpen) < 0)
				open = i;
		}
		for (int i = 1; i < data.length; i++) {
			String[] tempSplit = data[i].split("\\s+");
			if (tempSplit[2].compareTo(strClose) > 0)
				close = i;
		}

		split = data[open].split("\\s+");
		strBuilder.append(split[0]);
		split = data[close].split("\\s+");
		strBuilder.append(" ").append(split[0]);
		return strBuilder.toString();

	}

}

/**************************************************************
 * Problem: 1013 User: langzimaizan Language: Java Result: Accepted Time:80 ms
 * Memory:15580 kb
 ****************************************************************/
