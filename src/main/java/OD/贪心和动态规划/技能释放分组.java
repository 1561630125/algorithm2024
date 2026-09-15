package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 19:25
 */
public class 技能释放分组 {


    public class Solution {
        public int minimumSkillSegments(int k, int m, int w, int[] cooldowns) {
            // limit 表示单个技能段允许的最大冷却值总和
            long limit = k;
            // maximum 表示普通技能段最多可以包含的冷却个数
            int maximum = m, burst = w;
            // 把 int[] 转成 long[]，防止求和时溢出
            long[] values = new long[cooldowns.length];
            for (int i = 0; i < cooldowns.length; i++)
                values[i] = cooldowns[i];

            int inf = 1_000_000_000; // 表示不可达的极大值

            // dp[i][used]:
            //   处理完前 i 个冷却后，最少需要的技能段数
            //   used = 0 表示尚未使用过“爆发”技能
            //   used = 1 表示已经使用过“爆发”技能
            int[][] dp = new int[values.length + 1][2];
            for (int[] row : dp)
                java.util.Arrays.fill(row, inf);

            // 初始状态：处理 0 个冷却，未使用爆发，段数为 0
            dp[0][0] = 0;

            // 枚举当前段的起点 start
            for (int start = 0; start < values.length; start++)
                // 枚举是否已经使用过爆发技能
                for (int used = 0; used < 2; used++)
                    if (dp[start][used] < inf) {
                        // ---------- 情况 1：使用普通技能段 ----------
                        long sum = 0;
                        // 普通段最多覆盖 maximum 个冷却
                        for (int end = start;
                             end < Math.min(values.length, start + maximum);
                             end++) {
                            sum += values[end];
                            if (sum > limit) // 超过单个段的上限，不能继续延长
                                break;
                            // 用 1 个普通段覆盖 [start, end]，更新 dp[end+1][used]
                            dp[end + 1][used] = Math.min(
                                    dp[end + 1][used],
                                    dp[start][used] + 1
                            );
                        }

                        // ---------- 情况 2：使用爆发技能段 ----------
                        // 条件：还没用过爆发，爆发段长度不超过 maximum，且不越界
                        if (used == 0 && burst <= maximum
                                && start + burst <= values.length) {
                            sum = 0;
                            for (int i = start; i < start + burst; i++)
                                sum += values[i];
                            // 爆发段允许的冷却总和是普通段的 2 倍
                            if (sum <= 2 * limit)
                                // 用 1 个爆发段覆盖 [start, start+burst)，标记 used=1
                                dp[start + burst][1] = Math.min(
                                        dp[start + burst][1],
                                        dp[start][used] + 1
                                );
                        }
                    }

            // 答案是覆盖完所有冷却所需的最小段数
            int answer = Math.min(dp[values.length][0], dp[values.length][1]);
            // 如果仍是 inf，说明无解，返回 -1
            return answer >= inf ? -1 : answer;
        }
    }

}
