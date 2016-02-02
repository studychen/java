public class CountPrimes {

	public static void main(String[] args) {
		System.out.println(countPrimes(1));
		System.out.println(countPrimes(100));
		System.out.println(countPrimes(1500000));
	}

	public static int countPrimes(int n) {
		if (n == 0 || n == 1 || n == 2)
			return 0;
		boolean[] origin = new boolean[n];
		for (int i = 0; i < n; i++)
			origin[i] = true;

		for (int i = 2; i < n; i++) {
			if (origin[i]) {
				int temp = i + i;
				while (temp < n) {
					origin[temp] = false;
					temp = temp + i;
				}
			}
		}
		int result = 0;
		for (int i = 2; i < n; i++) {
			if (origin[i]) {
				result++;
			}
		}
		return result;
	}

}
