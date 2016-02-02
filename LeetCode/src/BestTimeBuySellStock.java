
public class BestTimeBuySellStock {
	public static void main(String[] args) {
		System.out.println(maxProfit(new int[] { 1, 3, 4, 0, 1, 3, 2 }));
		System.out.println(maxProfit(new int[] { 3, 3, 1, 5, 10, 3, 4, 0, 1, 3, 2 }));

		System.out.println(maxProfit1(new int[] { 1, 3, 4, 0, 1, 3, 2 }));
		System.out.println(maxProfit1(new int[] { 3, 3, 1, 5, 10, 3, 4, 0, 1, 3, 2 }));

	}

	public static int maxProfit1(int[] prices) {
		int result = 0;
		for (int i = 1; i < prices.length; i++) {
			if (prices[i] > prices[i - 1]) {
				result += prices[i] - prices[i - 1];
			}
		}
		return result;

	}

	public static int maxProfit(int[] prices) {
		int result = 0;
		if (prices.length == 0) {
			return 0;
		}
		int startPrice = prices[0];
		int endPrice = prices[0];
		for (int i = 1; i < prices.length; i++) {
			while (i < prices.length && startPrice < prices[i]) {
				if (prices[i] < endPrice) {
					break;
				} else {
					endPrice = prices[i];
				}
				i++;
			}
			result += endPrice - startPrice;
			if (i < prices.length) {
				startPrice = prices[i];
				endPrice = prices[i];
			}

		}
		return result;

	}

}
