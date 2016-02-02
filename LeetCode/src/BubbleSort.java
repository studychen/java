import java.util.Arrays;

public class BubbleSort {

	public static void main(String[] args) {

		int[] test = new int[] { 1, 3, 2, 0, 4 };
		System.out.println(Arrays.toString(test));
		bubbleSort(test);
		System.out.println(Arrays.toString(test));

		test = new int[] { 1, 3, 2, 0, 4, 5, 10, 2 };
		System.out.println(Arrays.toString(test));
		insertSort(test);
		System.out.println(Arrays.toString(test));

	}

	public static void bubbleSort(int[] nums) {
		boolean flag = true;
		for (int i = 1; i < nums.length; i++) {
			flag = true;
			for (int j = nums.length - 1; j >= i; j--) {
				if (nums[j] < nums[j - 1]) {
					int temp = nums[j - 1];
					nums[j - 1] = nums[j];
					nums[j] = temp;
					flag = false;
				}
			}
			if (flag) {
				break;
			}
		}
	}

	public static void insertSort(int[] nums) {

		for (int i = 1; i < nums.length; i++) {
			// 如果 nums[i]>nums[i - 1] 直接继续
			if (nums[i] < nums[i - 1]) {
				int temp = nums[i];
				int j = i - 1;
				while (j >= 0 && nums[j] > temp) {
					nums[j + 1] = nums[j];
					j--;
				}
				nums[j + 1] = temp;
			}
		}

	}

}
