public class SingleNumberII {
	public static void main(String[] args) {
		System.out.println(singleNumber(new int[] { 1, 3, 3, 3 }));
		System.out.println(singleNumber(new int[] { 1, 4, 3, 4, 3, 4, 3 }));
		System.out.println(singleNumber(new int[] { 3, 3, 1, 3 }));
		System.out.println(singleNumber(new int[] { 3, 1, 3, 3 }));
		System.out.println(singleNumber(new int[] { 3, 1, 3, 3 }));
		System.out.println(singleNumber(new int[] { 3, 1, 4, 4, 3, 4, 3 }));

		System.out.println(singleNumber(new int[] { 5, 3, 2, 5, 4, 5, 4, 3, 4, 3 }));
		System.out.println(singleNumber(new int[] { 2, 2, 3, 2 }));

	}

	public static int singleNumber(int[] nums) {
		int one = 0, two = 0, three = 0;
		for (int i = 0; i < nums.length; i++) {
			two |= nums[i] & one;
			one = nums[i] ^ one;
			three = ~(one & two);
			one &= three;
			two &= three;
		}
		return one;
	}

}
