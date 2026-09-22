package OD.贪心和动态规划;

/**
 * 考点：01背包
 *
 * @author faming.yang@hand-china.com 2026-09-15 17:46
 */
public class 工时选报酬 {


    /*long maximumReward(int timeLimit, long[][] jobs) {
        // write code here

         // 前i工作耗时j获得的最大报酬;
         int[][] dp = new int[jobs.length][timeLimit+1];
        
        for(int i = 0; i < jobs.length; i++) {
            dp[i][j] = dp[i-1][j];
            long time = jobs[i][0];
            long val = jobs[i][1];


         }
        return 0;
    }*/


    class Solution {
        long maximumReward(int timeLimit, long[][] jobs) {
            timeLimit = Math.max(timeLimit, 0);
            int n = jobs.length;

            // dp[i][c]：前 i 个工作中，时间上限 c 内的最大收益
            long[][] dp = new long[n + 1][timeLimit + 1];

            // 外层：枚举前 i 个物品（1-based，dp[0][*] 是边界=0）
            for (int i = 1; i <= n; i++) {
                int duration = (int) jobs[i - 1][0];   // 第 i 个工作的耗时
                long reward  = jobs[i - 1][1];         // 第 i 个工作的收益

                // 内层：枚举容量 c（0..timeLimit）
                for (int c = 0; c <= timeLimit; c++) {
                    if (c >= duration) {
                        // 选：dp[i-1][c-duration] + reward
                        // 不选：dp[i-1][c]
                        dp[i][c] = Math.max(dp[i - 1][c], dp[i - 1][c - duration] + reward);
                    } else {
                        // 容量不够，只能不选
                        dp[i][c] = dp[i - 1][c];
                    }
                }
            }

            return dp[n][timeLimit];
        }
    }



    class Solution2 {
        /**
         * 在时间上限 timeLimit 内选择若干个工作，使总收益最大。
         * 每个工作 job = [duration, reward]：耗时 duration，收益 reward。
         * 每种工作最多做一次（0-1 背包）。
         *
         * ⚠️ 注意：代码里对 duration 为负数的处理存在可疑之处（见下方分析）。
         *
         * @param timeLimit 时间上限
         * @param jobs      工作列表，每个元素是 [耗时, 收益]
         * @return 在时间上限内能获得的最大收益
         */
        long maximumReward(int timeLimit, long[][] jobs) {
            // 时间上限不能为负，负数则按 0 处理
            timeLimit = Math.max(timeLimit, 0);

            // extra 用于统计"负耗时"工作带来的额外容量
            // （负耗时工作相当于"时间反而增加"，理论上可以扩大可容纳的时间上限）
            int extra = 0;
            for (long[] job : jobs)
                if (job.length >= 2 && job[0] < 0)
                    extra = Math.max(extra, (int) -job[0]);

            // dp 数组：容量为 timeLimit + extra + 2，预留余量防越界
            // dp[c] 表示"用不超过 c 的时间"能获得的最大收益
            long[] dp = new long[timeLimit + extra + 2];

            // 遍历每个工作（0-1 背包的外层循环）
            for (long[] job : jobs)
                if (job.length >= 2) {
                    int duration = (int) job[0];   // 该工作耗时
                    long reward = job[1];          // 该工作收益

                    // 内层：从大到小枚举时间（0-1 背包关键，防止同一工作被重复使用）
                    for (int current = timeLimit; current >= 0; current--)
                        if (current >= duration)
                            // 选或不选：取较大值
                            dp[current] = Math.max(dp[current], dp[current - duration] + reward);
                        else
                            // current < duration 时无法选这个工作，
                            // 而 current 继续减小更不可能满足，故直接 break
                            break;
                }

            // 返回时间上限为 timeLimit 时的最大收益
            return dp[timeLimit];
        }
    }
}
