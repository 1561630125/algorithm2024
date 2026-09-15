package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 17:58
 */
public class 满车组数 {


    long receptionPlanCount(long[] groups, int capacity) {
        // write code here

        // dp[i][j] 前i组容量方案数

        long[][] dp = new long[groups.length+1][capacity+1];
        for(int i = 1; i <= groups.length; i++) {
            for(int j = 1; j <= capacity; j++) {

                dp[i][j] = dp[i-1][j];
                dp[i][j] = dp[i][(int)(j-groups[i])];
            }
        }

        return 0;
    }


    class Solution3 {
        long receptionPlanCount(long[] groups, int capacity) {
            int n = groups.length;

            // dp[i][s]：前 i 个团体中，凑出总人数恰好为 s 的方案数
            long[][] dp = new long[n + 1][capacity + 1];

            // 边界：前 0 个团体凑出 0 人 = 1 种方案（什么都不选）
            dp[0][0] = 1;

            // 外层：枚举前 i 个团体（1-based）
            for (int i = 1; i <= n; i++) {
                int group = (int) groups[i - 1];   // 第 i 个团体的人数

                // 内层：枚举目标人数 s（正序即可）
                for (int s = 0; s <= capacity; s++) {
                    // 不选第 i 个团体：方案数继承自"前 i-1 个凑出 s"
                    dp[i][s] = dp[i - 1][s];

                    // 选第 i 个团体：需 s >= group，
                    // 方案数 += "前 i-1 个凑出 s-group" 的方案数
                    if (s >= group)
                        dp[i][s] += dp[i - 1][s - group];
                }
            }

            return dp[n][capacity];
        }
    }


    class Solution {
        /**
         * 给定若干"团体"（每个团体人数为 groups[i]），以及一个容量为 capacity 的场地，
         * 求有多少种"选若干团体、使其总人数恰好等于 capacity"的方案数。
         *
         * 本质：0-1 背包的"方案计数"变体
         *   - 物品重量 = group（团体人数）
         *   - 背包容量 = capacity
         *   - 目标：凑出恰好 capacity 的方案数
         *   - 每个团体最多用一次（0-1）
         *
         * @param groups   每个团体的人数
         * @param capacity 场地容量（目标总人数）
         * @return 总人数恰好为 capacity 的方案数
         */
        long receptionPlanCount(long[] groups, int capacity) {
            // plans[s] 表示"选出若干团体、总人数恰好为 s"的方案数
            long[] plans = new long[capacity + 1];

            // 边界：凑出 0 人只有一种方案——什么都不选
            plans[0] = 1;

            // 外层：枚举每个团体（0-1 背包）
            for (long raw : groups) {
                int group = (int) raw;   // 团体人数作为"物品重量"

                // 内层：容量从大到小遍历（0-1 背包关键，保证每个团体只用一次）
                for (int seats = capacity; seats >= group; seats--)
                    plans[seats] += plans[seats - group];
            }

            // 返回恰好凑出 capacity 的方案数
            return plans[capacity];
        }
    }
}
