package OD.贪心和动态规划;

/**
 * 考点：01背包
 *
 * @author faming.yang@hand-china.com 2026-09-15 19:42
 */
public class 原料加工安排 {

    public class Solution {
        public long maximumMushroomValue(
                int count,       // 蘑菇数量
                int total,       // 总时间（或总预算）
                long[] values,   // 每个蘑菇的初始价值
                long[] decays    // 每个蘑菇的衰减系数
        ) {
            // 把每个蘑菇的 (价值, 衰减) 打包成二维数组
            long[][] nodes = new long[count][2];
            for (int index = 0; index < count; index++) {
                nodes[index][0] = values[index];
                nodes[index][1] = decays[index];
            }

            // 按衰减系数从大到小排序（衰减快的先处理）
            java.util.Arrays.sort(nodes,
                    (left, right) -> Long.compare(right[1], left[1]));

            // 最多能选的蘑菇数量：
            //   - 不超过 count
            //   - 不超过 total / 5（每个蘑菇至少消耗 5 个时间单位）
            int maximum = Math.min(count, total / 5);

            // dp[j] = 选择 j 个蘑菇时能获得的最大总价值
            long[] dp = new long[maximum + 1];
            java.util.Arrays.fill(dp, -1);  // -1 表示该状态不可达
            dp[0] = 0;                      // 选 0 个蘑菇，价值为 0

            long answer = 0;

            // 依次考虑每个蘑菇（按衰减从大到小）
            for (long[] node : nodes) {
                long value = node[0];   // 该蘑菇的初始价值
                long decay = node[1];   // 该蘑菇的衰减系数

                // 0/1 背包逆序遍历，避免同一个蘑菇被重复选择
                for (int processed = maximum - 1; processed >= 0; processed--) {
                    if (dp[processed] < 0)  // 该状态不可达，跳过
                        continue;

                    // 关键公式：该蘑菇作为第 (processed+1) 个被采摘时的实际收益
                    //   采摘时机越晚，衰减越多：已经采了 processed 个，
                    //   每个占用 5 个时间单位，所以延迟了 processed * 5 的时间
                    long gain = value - decay * processed * 5L;

                    if (gain > 0) {  // 只有正收益才值得选
                        dp[processed + 1] = Math.max(
                                dp[processed + 1],
                                dp[processed] + gain
                        );
                        // 实时更新全局最大答案
                        answer = Math.max(answer, dp[processed + 1]);
                    }
                }
            }
            return answer;
        }
    }


    public class Solution2 {
        public long maximumMushroomValue(
                int count,
                int total,
                long[] values,
                long[] decays
        ) {
            // dp[i][t]:
            //   考虑前 i 个蘑菇，累计花费时间为 t 时，能获得的最大总收益
            //   -1 表示该状态不可达
            long[][] dp = new long[count + 1][total + 1];
            for (long[] row : dp)
                java.util.Arrays.fill(row, -1);
            dp[0][0] = 0;   // 0 个蘑菇、0 时间，收益为 0

            long answer = 0;

            // 枚举每个蘑菇
            for (int i = 1; i <= count; i++) {
                long value = values[i - 1];
                long decay = decays[i - 1];

                // 枚举当前累计时间 t
                for (int t = 0; t <= total; t++) {
                    // 不选第 i 个蘑菇：直接从上一层继承
                    if (dp[i - 1][t] >= 0) {
                        dp[i][t] = Math.max(dp[i][t], dp[i - 1][t]);
                    }

                    // 选第 i 个蘑菇：需要花费 5 个时间单位
                    // 采摘它时，已经采了 (t / 5) 个蘑菇（因为每个占 5 时间）
                    if (t >= 5 && dp[i - 1][t - 5] >= 0) {
                        long alreadyPicked = (t - 5) / 5;   // 之前已采数量
                        long gain = value - decay * alreadyPicked * 5L;
                        if (gain > 0) {
                            dp[i][t] = Math.max(
                                    dp[i][t],
                                    dp[i - 1][t - 5] + gain
                            );
                        }
                    }

                    answer = Math.max(answer, dp[i][t]);
                }
            }
            return answer;
        }
    }
}
