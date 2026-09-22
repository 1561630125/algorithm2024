package OD.贪心和动态规划;

/**
 * 考点：一维dp + 状态压缩
 *
 * @author faming.yang@hand-china.com 2026-09-15 14:54
 */
public class 游玩通票最低价 {

    class Solution {
        // 计算在 Wonderland 出行所需的最小花费
        // costs[0] = 1 天票价格
        // costs[1] = 3 天票价格
        // costs[2] = 7 天票价格
        // costs[3] = 30 天票价格
        // days = 需要出行的日子（升序排列）
        long minimumWonderlandCost(long[] costs, long[] days) {
            // 如果没有需要出行的日子，花费为 0
            if (days.length == 0)
                return 0;

            // maximum = 最后一个需要出行的日子
            // index 用来遍历 days 数组，指向当前需要处理的那一天
            int maximum = (int) Math.max(days[days.length - 1], 0), index = 0;

            // dp[day] 表示：覆盖到第 day 天为止，所需要的最小花费
            // 数组长度是 maximum + 1，因为下标从 0 到 maximum
            long[] dp = new long[maximum + 1];

            // 从第 1 天遍历到最后一天
            for (int day = 1; day <= maximum; day++) {
                // 情况 A：今天不是需要出行的日子
                // 条件：days 已经遍历完，或者当前 day 不等于 days[index]
                if (index >= days.length || day != days[index]) {
                    // 今天不用出行，花费和昨天一样
                    dp[day] = dp[day - 1];
                } else {
                    // 情况 B：今天是需要出行的日子，必须被票覆盖
                    // 从四种票里选一种，使得总花费最小：
                    // 1. 买 1 天票：昨天的最小花费 + costs[0]
                    // 2. 买 3 天票：3 天前的最小花费 + costs[1]
                    // 3. 买 7 天票：7 天前的最小花费 + costs[2]
                    // 4. 买 30 天票：30 天前的最小花费 + costs[3]
                    // Math.max(0, day - k) 防止下标变成负数
                    dp[day] =
                            Math.min(
                                    // 前两种方案取较小值
                                    Math.min(dp[day - 1] + costs[0], dp[Math.max(0, day - 3)] + costs[1]),
                                    // 后两种方案取较小值
                                    Math.min(dp[Math.max(0, day - 7)] + costs[2],
                                            dp[Math.max(0, day - 30)] + costs[3])
                            );

                    // 这一天处理完了，index 指向下一个需要出行的日子
                    index++;
                }
            }

            // 返回覆盖到最后一天的最小花费
            return dp[maximum];
        }
    }


    class Solution2 {
        long minimumWonderlandCost(long[] costs, long[] days) {
            if (days.length == 0)
                return 0;

            int maximum = (int) Math.max(days[days.length - 1], 0);

            // 四种票的覆盖天数
            int[] cover = {1, 3, 7, 30};

            // dp[i][day] = 只考虑前 i 种票，覆盖到第 day 天的最小花费
            // i 从 0 到 4，day 从 0 到 maximum
            long[][] dp = new long[5][maximum + 1];

            // 初始化：用 0 种票时，只有第 0 天花费为 0，其他天无法覆盖，设为无穷大
            long INF = Long.MAX_VALUE / 2;
            for (int day = 0; day <= maximum; day++) {
                dp[0][day] = INF;
            }
            dp[0][0] = 0;

            // 标记哪些天需要出行
            boolean[] need = new boolean[maximum + 1];
            for (long d : days) {
                if (d >= 1 && d <= maximum) {
                    need[(int) d] = true;
                }
            }

            // 遍历四种票
            for (int i = 1; i <= 4; i++) {
                int c = cover[i - 1];
                long price = costs[i - 1];

                for (int day = 0; day <= maximum; day++) {
                    // 先继承上一种票的结果（不买第 i 种票）
                    dp[i][day] = dp[i - 1][day];

                    // 如果第 day 天需要出行，必须被覆盖
                    // 但这里我们统一处理：只要 day >= c，就可以尝试买第 i 种票
                    if (day >= c) {
                        dp[i][day] = Math.min(dp[i][day], dp[i][day - c] + price);
                    }

                    // 如果第 day 天需要出行，且还没被覆盖，那这个状态非法
                    // 不过上面的转移已经保证：只要买了票，就会被覆盖
                    // 这里做一个额外检查：如果需要出行但 dp[i][day] 还是 INF，说明无解
                    // 但题目一般保证有解（1 天票总能覆盖）
                    if (need[day] && dp[i][day] >= INF) {
                        dp[i][day] = INF;
                    }
                }
            }

            return dp[4][maximum];
        }
    }

}
