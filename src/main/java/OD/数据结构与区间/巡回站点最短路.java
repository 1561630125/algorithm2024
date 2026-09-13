package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-12 14:28
 */
public class 巡回站点最短路 {

    class Solution {

        /**
         * 求从 0 号点出发，经过所有点恰好一次，最后回到 0 号点的最短路径长度。
         * 这就是经典的「旅行商问题（TSP）」，用状压 DP 求解。
         *
         * @param distances 邻接矩阵，distances[i][j] 表示 i 到 j 的距离
         * @return 最短的巡回路径长度；输入不合法时返回 0
         */
        long minimumMaintenanceTour(long[][] distances) {

            int count = distances.length;   // 点的数量

            // 1. 合法性检查：0 个点，或超过 10 个点（状压 DP 最多支持约 10~20 个点），直接返回 0
            if (count == 0 || count > 10)
                return 0L;

            // 检查是否为方阵（每行长度都等于点数）
            for (long[] row : distances)
                if (row.length != count)
                    return 0L;

            // 2. 状压 DP 初始化
            //    states = 2^count，每个二进制位表示某个点是否已访问
            int states = 1 << count;

            // 用一个大数表示「无穷大」，除以 4 是为了相加时不溢出
            long infinity = Long.MAX_VALUE / 4;

            // dp[mask][last]：
            //   已访问的点集合为 mask，且当前停在 last 号点时，所走的最短距离
            long[][] dp = new long[states][count];

            // 全部初始化为无穷大
            for (long[] row : dp)
                java.util.Arrays.fill(row, infinity);

            // 起点：只访问了 0 号点（mask = 1），且当前在 0 号点，距离为 0
            dp[1][0] = 0L;

            // 3. 状压 DP 状态转移
            for (int mask = 1; mask < states; mask++) {

                // 只处理包含起点 0 的状态（mask 的最低位为 1）
                if ((mask & 1) != 0)

                    // 枚举当前所在的点 last
                    for (int last = 0; last < count; last++)

                        // 该状态必须可达
                        if (dp[mask][last] != infinity)

                            // 枚举下一个要去的点 next（从 1 开始，因为 0 是起点）
                            for (int next = 1; next < count; next++)

                                // next 必须还没访问过
                                if ((mask & (1 << next)) == 0) {

                                    // 新状态：把 next 加入已访问集合
                                    int nextMask = mask | (1 << next);

                                    // 尝试用「当前距离 + last 到 next 的距离」更新新状态
                                    dp[nextMask][next] = Math.min(
                                            dp[nextMask][next],
                                            dp[mask][last] + distances[last][next]);
                                }
            }

            // 4. 特判：只有一个点时，直接返回 distances[0][0]
            if (count == 1)
                return distances[0][0];

            // 5. 收尾：所有点都访问完了（mask = states - 1），
            //    从最后一个点 last 回到起点 0，取最小总距离
            long result = infinity;
            for (int last = 1; last < count; last++)
                result = Math.min(result, dp[states - 1][last] + distances[last][0]);

            return result;
        }
    }


}
