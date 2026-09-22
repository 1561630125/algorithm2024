package OD.贪心和动态规划;

/**
 * 快递员路线规划问题，结合了Floyd-Warshall算法和状态压缩动态规划（TSP问题）
 *
 * @author faming.yang@hand-china.com 2026-09-03 18:43
 */
public class 快递最短路 {


    class Solution {
        long minimumCourierRoute(long[][] s, long[][] routes) {
            // n：站点数量
            int n = s.length;

            // g：邻接矩阵，大小为 (n+1) × (n+1)
            // 下标 0 表示仓库，下标 1..n 表示 n 个站点
            long[][] g = new long[n + 1][n + 1];

            // 初始化邻接矩阵，全部置为 -1，表示不可达
            for (long[] r : g)
                java.util.Arrays.fill(r, -1);

            // 自己到自己的距离为 0
            for (int i = 0; i <= n; i++)
                g[i][i] = 0;

            // mp：把题目的原始站点编号映射成连续下标 1..n
            java.util.Map<Long, Integer> mp = new java.util.HashMap<>();

            // 处理每个站点 s[i] = {站点编号, 到仓库的直达距离}
            for (int i = 0; i < n; i++) {
                mp.put(s[i][0], i + 1);                  // 原始编号 → 下标 i+1
                g[0][i + 1] = g[i + 1][0] = s[i][1];     // 仓库 0 ↔ 站点 i+1 的距离
            }

            // 处理站点之间的路线 routes[k] = {起点编号, 终点编号, 距离}
            for (long[] e : routes) {
                int a = mp.get(e[0]);   // 起点映射成下标
                int b = mp.get(e[1]);   // 终点映射成下标
                g[a][b] = g[b][a] = e[2];  // 无向边，双向赋值
            }

            // ============ Floyd-Warshall 求任意两点间最短距离 ============
            // 三重循环，k 必须放最外层（枚举中转点）
            for (int k = 0; k <= n; k++)
                for (int i = 0; i <= n; i++)
                    for (int j = 0; j <= n; j++)
                        // 只有 i→k 和 k→j 都可达，才能用 k 作为中转
                        if (g[i][k] != -1 && g[k][j] != -1)
                            g[i][j] = g[i][j] == -1
                                    ? g[i][k] + g[k][j]                          // 原来不可达，直接赋值
                                    : Math.min(g[i][j], g[i][k] + g[k][j]);      // 原来可达，取更小值

            // ============ 状态压缩 DP（TSP 旅行商问题） ============
            // inf：一个足够大的数，防止加法溢出（不用 Long.MAX_VALUE 是因为加法会溢出）
            long inf = Long.MAX_VALUE / 4;

            // dp[mask][c]：已访问站点集合为 mask，当前停留在节点 c 的最小总路程
            // mask 用二进制表示，第 i 位为 1 表示站点 i+1 已访问
            long[][] dp = new long[1 << n][n + 1];
            for (long[] r : dp)
                java.util.Arrays.fill(r, inf);   // 初始化为无穷大

            // 初始状态：什么都没访问，站在仓库 0，代价 0
            dp[0][0] = 0;

            // 枚举所有状态 mask
            for (int m = 0; m < 1 << n; m++)
                // 枚举当前所在的节点 c
                for (int c = 0; c <= n; c++)
                    // 枚举下一步要去的节点 x
                    for (int x = 0; x <= n; x++)
                        // 当前状态必须可达，且 c→x 有路
                        if (dp[m][c] < inf && g[c][x] != -1) {
                            // 如果 x 是仓库（0），mask 不变；否则把 x-1 这一位标记为已访问
                            int nm = x == 0 ? m : m | 1 << (x - 1);
                            // 状态转移：从 (m, c) 走到 (nm, x)
                            dp[nm][x] = Math.min(dp[nm][x], dp[m][c] + g[c][x]);
                        }

            // ============ 收集答案：所有站点都访问完，最后回到仓库 ============
            long res = inf;

            // 枚举最后停留在哪个节点 i
            for (int i = 0; i <= n; i++)
                // 要求：所有站点都访问过（mask 全 1），且 i 能回到仓库 0
                if (dp[(1 << n) - 1][i] < inf && g[i][0] != -1)
                    // 从 i 回到仓库，加上 g[i][0]
                    res = Math.min(res, dp[(1 << n) - 1][i] + g[i][0]);

            // 返回最短总路程（如果无解，res 仍为 inf）
            return res;
        }
    }


    class Solution2 {

        private long best;
        private int n;
        private long[][] g;
        private static final long INF = Long.MAX_VALUE / 4;

        long minimumCourierRoute(long[][] s, long[][] routes) {
            n = s.length;

            // ---------- 1. 建图 ----------
            g = new long[n + 1][n + 1];
            for (long[] r : g) {
                java.util.Arrays.fill(r, -1);
            }
            for (int i = 0; i <= n; i++) {
                g[i][i] = 0;
            }

            java.util.Map<Long, Integer> mp = new java.util.HashMap<>();
            for (int i = 0; i < n; i++) {
                mp.put(s[i][0], i + 1);              // 投递点编号 -> 1..n
                g[0][i + 1] = g[i + 1][0] = s[i][1]; // 仓库 0 与投递点 i+1 的直达距离
            }
            for (long[] e : routes) {
                int a = mp.get(e[0]);
                int b = mp.get(e[1]);
                g[a][b] = g[b][a] = e[2];
            }

            // ---------- 2. Floyd 预处理任意两点最短路 ----------
            for (int k = 0; k <= n; k++) {
                for (int i = 0; i <= n; i++) {
                    if (g[i][k] == -1) continue;
                    for (int j = 0; j <= n; j++) {
                        if (g[k][j] == -1) continue;
                        long nd = g[i][k] + g[k][j];
                        if (g[i][j] == -1 || nd < g[i][j]) {
                            g[i][j] = nd;
                        }
                    }
                }
            }

            // ---------- 3. DFS 枚举访问顺序 ----------
            best = INF;
            boolean[] visited = new boolean[n + 1];
            visited[0] = true;                        // 仓库已访问
            dfs(0, 0, 0, visited);

            return best == INF ? -1 : best;           // 不可达返回 -1（按题目要求可改）
        }

        /**
         * @param cur          当前所在节点
         * @param visitedCount 已访问的投递点数量（不含仓库 0）
         * @param cost         当前累计路程
         * @param visited      访问标记
         */
        private void dfs(int cur, int visitedCount, long cost, boolean[] visited) {
            // 剪枝：当前花费已经不小于已知最优解，没必要继续
            if (cost >= best) return;

            // 所有投递点都访问完了，尝试回仓库
            if (visitedCount == n) {
                if (g[cur][0] != -1) {
                    best = Math.min(best, cost + g[cur][0]);
                }
                return;
            }

            // 枚举下一个未访问的投递点
            for (int nxt = 1; nxt <= n; nxt++) {
                if (visited[nxt]) continue;
                if (g[cur][nxt] == -1) continue;      // 不可达

                visited[nxt] = true;
                dfs(nxt, visitedCount + 1, cost + g[cur][nxt], visited);
                visited[nxt] = false;                 // 回溯
            }
        }
    }
}
