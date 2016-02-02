public class MajorityElement {
	public static void main(String[] args) {
		System.out.println(majorityElement(new int[]{1,2,3,1,1,1,3,4}));
		System.out.println(majorityElement(new int[]{4,4,1}));
		System.out.println(majorityElement(new int[]{4,4,1,2,4}));
	}

	public static int majorityElement(int[] nums) {
		int count = 1;
		int result = nums[0];
		for (int i = 1; i < nums.length; i++) {
			if (count == 0) {
				result = nums[i];
				count = 1;
			} else {
				if (nums[i] == result) {
					count++;
				} else {
					count--;
				}
			}
		}
		return result;
	}
}
