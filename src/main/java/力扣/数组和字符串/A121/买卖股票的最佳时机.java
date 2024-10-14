package 力扣.数组和字符串.A121;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-14 10:19
 */
public class 买卖股票的最佳时机 {
    //暴力法【超时】
    public int maxProfit(int[] prices) {
        int maxprofit = 0;
        for (int i = 0; i < prices.length - 1; i++) {
            for (int j = i + 1; j < prices.length; j++) {
                int profit = prices[j] - prices[i];
                if (profit > maxprofit) {
                    maxprofit = profit;
                }
            }
        }
        return maxprofit;
    }

    public static int maxProfit2(int prices[]) {
        int minprice = Integer.MAX_VALUE;
        int maxprofit = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] < minprice) {
                minprice = prices[i];
            } else if (prices[i] - minprice > maxprofit) {
                maxprofit = prices[i] - minprice;
            }
        }
        return maxprofit;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{7, 6, 5, 8, 4, 2};
        System.out.println(maxProfit2(nums));

    }

}
