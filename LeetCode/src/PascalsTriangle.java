import java.util.ArrayList;
import java.util.List;

public class PascalsTriangle {
	public static void main(String[] args) {
		System.out.println(generate(1));
		System.out.println(generate(0));
		System.out.println(generate(2));
		System.out.println(generate(3));
		System.out.println(generate(4));
		System.out.println(generate(5));

	}

	public static List<List<Integer>> generate(int numRows) {
		List<List<Integer>> result = new ArrayList<List<Integer>>(numRows);
		for (int i = 0; i < numRows; i++) {
			List<Integer> thisRow = new ArrayList<Integer>(i);
			thisRow.add(1);
			int temp = 1;
			int row = i;
			for (int j = 1; j <= i; j++) {
				temp = temp * row-- / j ;
				thisRow.add(temp);
			}
			result.add(thisRow);
		}
		return result;
	}

}
