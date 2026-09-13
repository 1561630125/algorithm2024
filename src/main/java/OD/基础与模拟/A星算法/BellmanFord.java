package OD.基础与模拟.A星算法;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-12 16:49
 */
class BellmanFord {

    /**
     * @param n     顶点数（编号 0 ~ n-1）
     * @param edges 边数组，每条边为 {from, to, weight}
     * @param source 起点
     * @return 从 source 到各点的最短距离；若存在负环返回 null
     */
    long[] bellmanFord(int n, int[][] edges, int source) {
        long infinity = Long.MAX_VALUE / 4;   // 防溢出
        long[] dist = new long[n];
        java.util.Arrays.fill(dist, infinity);
        dist[source] = 0;

        // ---------- 第 1 步：松弛 V-1 轮 ----------
        for (int round = 0; round < n - 1; round++) {
            boolean updated = false;          // 小优化：本轮无更新可提前退出

            for (int[] edge : edges) {
                int u = edge[0], v = edge[1], w = edge[2];

                // 只处理"已可达"的点，避免 infinity + w 溢出
                if (dist[u] == infinity) continue;

                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    updated = true;
                }
            }

            if (!updated) break;              // 已收敛，提前结束
        }

        // ---------- 第 2 步：第 V 轮检测负环 ----------
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            if (dist[u] == infinity) continue;

            if (dist[u] + w < dist[v]) {
                return null;                  // 存在负环
            }
        }

        return dist;
    }
}