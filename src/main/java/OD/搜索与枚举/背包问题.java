package OD.搜索与枚举;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 18:49
 */
public class 背包问题 {

    public static void main(String[] args) {
        int[] w = new int[]{2,3,4,7};
        int[] v = new int[]{1,3,5,9};

        int n = w.length;
        int C = 10;

        int[][] dp = new int[n + 1][C + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= C; j++) {
                dp[i][j] = dp[i - 1][j];  // 不选第 i 件
                if (j >= w[i - 1])
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - w[i - 1]] + v[i - 1]);
            }
        }

        System.out.println(dp[n][C]);


        int[] dp2 = new int[C + 1];
        for (int i = 0; i < n; i++) {
            for (int j = C; j >= w[i]; j--) {   // 倒序！
                dp2[j] = Math.max(dp2[j], dp2[j - w[i]] + v[i]);
            }
        }

        System.out.println(dp2[C]);

    }


    class Solution {
        private int[] w, v;
        private int[][] memo;

        int knapsack(int[] weights, int[] values, int capacity) {
            w = weights;
            v = values;
            int n = w.length;

            // memo[i][j]：从前 i 件物品中选、容量 j 的最大价值
            // -1 表示没算过
            memo = new int[n][capacity + 1];
            for (int[] row : memo)
                java.util.Arrays.fill(row, -1);

            return dfs(n - 1, capacity);
        }

        // 从前 i 件物品中选，容量为 j 的最大价值
        private int dfs(int i, int j) {
            if (i < 0) return 0;  // 没有物品了

            if (memo[i][j] != -1) return memo[i][j];  // 算过直接用

            // 不选第 i 件
            int res = dfs(i - 1, j);

            // 选第 i 件
            if (j >= w[i]) {
                res = Math.max(res, dfs(i - 1, j - w[i]) + v[i]);
            }

            return memo[i][j] = res;
        }
    }


    class Solution3 {
        private int[] w, v;
        private int[][] memo;

        int completeKnapsack(int[] weights, int[] values, int capacity) {
            w = weights;
            v = values;
            int n = w.length;

            // memo[i][j]：从前 i 件物品中选、容量 j 的最大价值
            // -1 表示没算过
            memo = new int[n][capacity + 1];
            for (int[] row : memo)
                java.util.Arrays.fill(row, -1);

            return dfs(n - 1, capacity);
        }

        /**
         * 从前 i 件物品中选（每件可重复选），容量为 j 的最大价值
         */
        private int dfs(int i, int j) {
            if (i < 0) return 0;  // 没有物品了

            if (memo[i][j] != -1) return memo[i][j];  // 算过直接用

            // 不选第 i 件
            int res = dfs(i - 1, j);

            // 选第 i 件（容量够的话）
            if (j >= w[i]) {
                // 注意：选完后剩下的容量，仍然可以继续选第 i 件 → dfs(i, ...)
                res = Math.max(res, dfs(i, j - w[i]) + v[i]);
            }

            return memo[i][j] = res;
        }
    }

}
