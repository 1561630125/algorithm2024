package OD.贪心和动态规划;

/**
 * 考点：贪心
 *
 * @author faming.yang@hand-china.com 2026-09-03 16:35
 */
public class 多商品买卖 {
    class Solution {
        /**
         * 计算商人的最大利润。
         *
         * @param limits 每种商品的每日交易数量上限（limits[i] 表示第 i 种商品每天最多交易多少件）
         * @param prices prices[i][d] 表示第 i 种商品在第 d 天的价格
         * @return 能获得的最大总利润
         * <p>
         * 逻辑：对每种商品，逐日比较"今天买、明天卖"的差价，
         * 只要差价为正（涨价）就赚这个差价 × 数量上限，
         * 差价 ≤ 0 就不操作（用 max(..., 0) 过滤）。
         */
        long maxMerchantProfit(long[] limits, long[][] prices) {
            long result = 0;

            // 商品种类数 = limits 和 prices 长度的较小值，防止越界
            int products = Math.min(limits.length, prices.length);

            // 遍历每种商品
            for (
                    int product = 0; product < products; product++
            )
                // 遍历该商品的每一天，从第 1 天开始（和前一天比较）
                for (int day = 1; day < prices[product].length; day++)
                    // 累加：max(今日价 - 昨日价, 0) × 该商品的数量上限
                    //  - 涨价：赚差价 × limits[product]
                    //  - 跌价/持平：赚 0（不交易）
                    result += Math.max(prices[product][day] - prices[product][day - 1], 0L)
                            * limits[product];

            return result;
        }
    }
}
