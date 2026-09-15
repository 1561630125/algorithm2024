package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 19:09
 */
public class 沿途接客 {

    public class Solution {
        /**
         * 给定一条数轴 [0, n]，以及若干"乘客"请求，每个请求为 [起点, 终点]。
         * 每次载一位乘客可从起点直达终点，收益 = 终点 - 起点（距离）。
         * 求从位置 0 走到位置 n 能获得的最大总收益。
         *
         * 状态转移：
         *   dp[i] = 从 0 到位置 i 能获得的最大收益
         *   - 不载客前进：dp[i] 可由 dp[i-1] 继承
         *   - 载客直达：若有人在 i 上车、到 end 下车，则 dp[end] 可由 dp[i] + (end - i) 更新
         *
         * @param n          终点位置（数轴范围 0..n）
         * @param passengers 乘客请求列表，每个为 [起点, 终点]
         * @return 从 0 到 n 的最大收益
         */
        public int getMaxProfit(int n, int[][] passengers) {
            // 用邻接表建图：graph[i] 存放"从位置 i 出发的所有乘客的终点"
            java.util.List<java.util.List<Integer>> graph = new java.util.ArrayList<>();
            for (int index = 0; index <= n; index++)
                graph.add(new java.util.ArrayList<>());   // 为每个位置建一个列表

            // 把每个乘客加入它起点对应的列表
            for (int[] passenger : passengers)
                graph.get(passenger[0]).add(passenger[1]);

            // dp[i]：从位置 0 到位置 i 能获得的最大收益
            int[] dp = new int[n + 1];

            // 从左到右推进（DP 遍历顺序）
            for (int position = 0; position < n; position++) {
                // 情况 1：不载客，从 position 走到 position+1，收益不变
                // 用 dp[position] 尝试更新 dp[position+1]
                dp[position + 1] = Math.max(dp[position + 1], dp[position]);

                // 情况 2：在 position 载客，直达其终点 end，收益 += (end - position)
                for (int end : graph.get(position))
                    dp[end] = Math.max(dp[end], dp[position] + end - position);
            }

            return dp[n];   // 到达终点的最大收益
        }
    }

}
