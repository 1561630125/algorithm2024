package 力扣.多维动态规划.A121;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-11-01 16:16
 */
public class 买卖股票的最佳时机3 {

}


class Solution {
    public int maxProfit(int[] prices) {
        int n = prices.length;
        int buy1 = -prices[0], sell1 = 0;
        int buy2 = -prices[0], sell2 = 0;
        for (int i = 1; i < n; ++i) {
            buy1 = Math.max(buy1, -prices[i]); //第一次买入
            sell1 = Math.max(sell1, buy1 + prices[i]); //第一次卖出
            buy2 = Math.max(buy2, sell1 - prices[i]);//完成第一次交易后，再次买入
            sell2 = Math.max(sell2, buy2 + prices[i]);//完成两次交易
        }
        return sell2;
    }
}
