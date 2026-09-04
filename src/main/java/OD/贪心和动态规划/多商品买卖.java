package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-03 16:35
 */
public class 多商品买卖 {
    long maxMerchantProfit(long[] limits, long[][] prices) {
        long result = 0;
        int products = Math.min(limits.length, prices.length);
        for (int product = 0; product < products; product++)
            for (int day = 1; day < prices[product].length; day++)
                result +=
                        Math.max(prices[product][day] - prices[product][day - 1], 0L) * limits[product];
        return result;
    }
}
