
public class SingleNumber {
	public static void main(String[] args) {
		System.out.println(singleNumber(new int[] { 1, 1, 2, 3, 2 }));
		System.out.println(singleNumber(new int[] { 1, 1, 2, 3, 2,3,4 }));
		System.out.println(singleNumber(new int[] { 4,3,1, 1, 2, 3, 2 }));
		System.out.println(singleNumber(new int[] { 3, 1, 2, 3, 2 }));
	}

	public static int singleNumber(int[] nums) {
		for (int i = 1; i < nums.length; i++) {
			nums[0] = nums[0] ^ nums[i];
		}
		return nums[0];

	}

}
