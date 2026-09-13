package OD.基础与模拟.A星算法;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-12 16:50
 */
class Floyd {

    /**
     * @param n     顶点数（编号 0 ~ n-1）
     * @param edges 边数组，每条边为 {from, to, weight}
     * @return 两两最短距离矩阵 dist[i][j]
     */
    long[][] floyd(int n, int[][] edges) {
        long infinity = Long.MAX_VALUE / 4;   // 防溢出

        // ---------- 1. 初始化 ----------
        long[][] dist = new long[n][n];
        for (int i = 0; i < n; i++) {
            java.util.Arrays.fill(dist[i], infinity);
            dist[i][i] = 0;                   // 自己到自己距离为 0
        }

        // 填边（注意：重边取最小，有向图只填一个方向）
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            dist[u][v] = Math.min(dist[u][v], w);

            // 若是无向图，再加一句:
            // dist[v][u] = Math.min(dist[v][u], w);
        }

        // ---------- 2. 三重循环（k 必须在最外层！）----------
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                // 小优化: 若 i 到 k 不可达，跳过
                if (dist[i][k] == infinity) continue;

                for (int j = 0; j < n; j++) {
                    // 防溢出: 不可达时跳过
                    if (dist[k][j] == infinity) continue;

                    long through = dist[i][k] + dist[k][j];
                    if (through < dist[i][j]) {
                        dist[i][j] = through;
                    }
                }
            }
        }

        return dist;
    }
}
