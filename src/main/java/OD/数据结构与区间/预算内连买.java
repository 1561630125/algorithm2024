package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 14:08
 */
public class 预算内连买 {


    int maxGemCount(int[] prices, int budget) {

        int left = 0;
        int res =  0;

        int[] sum = new int[prices.length];
        sum[0] = prices[0];
        for(int i = 1; i < prices.length; i++) {
            sum[i] = prices[i] + sum[i - 1];
        }

        for(int right = 0; right < prices.length; right++) {
            while (sum[right] - sum[left] > budget) {
                left++;
            }
            res = Math.max(right - left, res);
        }

        return res + 1;
    }


    int maxGemCount2(int[] prices, int budget) {
        int left = 0;
        int best = 0;
        long current = 0;
        for (int right = 0; right < prices.length; right++) {
            current += prices[right];
            while (current > budget && left <= right) {
                current -= prices[left++];
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

}
