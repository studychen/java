
public class NumberOfBits {
	public static void main(String[] args) {
		System.out.println(hammingWeight(1));
		System.out.println(hammingWeight(11));
		System.out.println(hammingWeight(10));
		System.out.println(hammingWeight(4));
		System.out.println(hammingWeight(8));
		System.out.println(hammingWeight(3));
		System.out.println(hammingWeight(0));
		System.out.println(hammingWeight(Integer.MIN_VALUE));

	}

	// you need to treat n as an unsigned value
	public static int hammingWeight(int n) {
		int result = 0;
		while (n != 0) {
			if ((n & 1) != 0) {
				result++;
			}
			n = n >> 1;
			 
		}
		return result;
	}

}
