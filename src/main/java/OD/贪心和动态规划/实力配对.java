package OD.贪心和动态规划;

/**
 * 考点：线性DP
 *
 * @author faming.yang@hand-china.com 2026-09-03 18:11
 */
public class 实力配对 {
    class Solution {
        int minOpponentDifference(int[] strengths, int maxDifference) {
            // 复制原数组，避免修改输入
            int[] values = strengths.clone();
            // 排序，让相邻元素差值最小，便于配对
            java.util.Arrays.sort(values);

            // pairs[i]：只考虑前 i 个元素时，最多能配成多少对
            // sums[i] ：在配对数最多的前提下，这些配对的最小差值总和
            // 下标从 0 开始用，但这里用 size 表示“前 size 个元素”
            int[] pairs = new int[values.length + 1];
            long[] sums = new long[values.length + 1];

            // 从小到大枚举“考虑前 size 个元素”
            for (int size = 2; size <= values.length; size++) {
                // 默认继承前 size-1 个元素的最优结果
                // 即：第 size 个元素（values[size-1]）不参与配对
                pairs[size] = pairs[size - 1];
                sums[size] = sums[size - 1];

                // 尝试把 values[size-2] 和 values[size-1] 配成一对
                long difference = (long) values[size - 1] - values[size - 2];

                // 只有差值 <= maxDifference 才能配对
                if (difference <= maxDifference) {
                    // 如果这对配对，那么前 size-2 个元素可以自由配对
                    int candidatePairs = pairs[size - 2] + 1;
                    long candidateSum = sums[size - 2] + difference;

                    // 择优：
                    // 1. 配对数更多，优先选
                    // 2. 配对数相同，差值总和更小，优先选
                    if (candidatePairs > pairs[size] ||
                            (candidatePairs == pairs[size] && candidateSum < sums[size])) {
                        pairs[size] = candidatePairs;
                        sums[size] = candidateSum;
                    }
                }
            }

            // 如果一对都配不成，返回 -1；否则返回最小差值总和
            return pairs[values.length] > 0 ? (int) sums[values.length] : -1;
        }
    }
}
