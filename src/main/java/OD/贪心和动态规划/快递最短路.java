package OD.贪心和动态规划;

/**
 * 快递员路线规划问题，结合了Floyd-Warshall算法和状态压缩动态规划（TSP问题）
 *
 * @author faming.yang@hand-china.com 2026-09-03 18:43
 */
public class 快递最短路 {


    long minimumCourierRoute(long[][] s, long[][] routes) {
        int n = s.length;
        long[][] g = new long[n + 1][n + 1];
        for (long[] r : g)
            java.util.Arrays.fill(r, -1);
        for (int i = 0; i <= n; i++)
            g[i][i] = 0;
        java.util.Map<Long, Integer> mp = new java.util.HashMap<>();
        for (int i = 0; i < n; i++) {
            mp.put(s[i][0], i + 1);
            g[0][i + 1] = g[i + 1][0] = s[i][1];
        }
        for (long[] e : routes) {
            int a = mp.get(e[0]), b = mp.get(e[1]);
            g[a][b] = g[b][a] = e[2];
        }
        for (int k = 0; k <= n; k++)
            for (int i = 0; i <= n; i++)
                for (int j = 0; j <= n; j++)
                    if (g[i][k] != -1 && g[k][j] != -1)
                        g[i][j] = g[i][j] == -1 ? g[i][k] + g[k][j]
                                : Math.min(g[i][j], g[i][k] + g[k][j]);
        long inf = Long.MAX_VALUE / 4;
        long[][] dp = new long[1 << n][n + 1];
        for (long[] r : dp)
            java.util.Arrays.fill(r, inf);
        dp[0][0] = 0;
        for (int m = 0; m < 1 << n; m++)
            for (int c = 0; c <= n; c++)
                for (int x = 0; x <= n; x++)
                    if (dp[m][c] < inf && g[c][x] != -1) {
                        int nm = x == 0 ? m : m | 1 << (x - 1);
                        dp[nm][x] = Math.min(dp[nm][x], dp[m][c] + g[c][x]);
                    }
        long res = inf;
        for (int i = 0; i <= n; i++)
            if (dp[(1 << n) - 1][i] < inf && g[i][0] != -1)
                res = Math.min(res, dp[(1 << n) - 1][i] + g[i][0]);
        return res;
    }

}
