package OD.基础与模拟.A星算法;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-12 16:50
 */
import java.util.*;

class SPFA {

    /**
     * 求从 source 到所有点的最短距离。
     *
     * @param n      顶点数（编号 0 ~ n-1）
     * @param edges  边数组，每条边为 {from, to, weight}
     * @param source 起点
     * @return 最短距离数组；若存在负环返回 null
     */
    long[] spfa(int n, int[][] edges, int source) {
        // ---------- 1. 建邻接表 ----------
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
        for (int[] e : edges) {
            graph.get(e[0]).add(new int[]{e[1], e[2]});   // 有向边 u→v
            // 若是无向图，再加: graph.get(e[1]).add(new int[]{e[0], e[2]});
        }

        // ---------- 2. 初始化 ----------
        long infinity = Long.MAX_VALUE / 4;   // 防溢出
        long[] dist = new long[n];
        Arrays.fill(dist, infinity);
        dist[source] = 0;

        boolean[] inQueue = new boolean[n];    // 是否在队列中
        int[] count = new int[n];              // 入队次数（判负环）
        Queue<Integer> queue = new ArrayDeque<>();

        queue.add(source);
        inQueue[source] = true;
        count[source] = 1;

        // ---------- 3. SPFA 主循环 ----------
        while (!queue.isEmpty()) {
            int u = queue.remove();
            inQueue[u] = false;

            for (int[] edge : graph.get(u)) {
                int v = edge[0], w = edge[1];

                // 松弛
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;

                    if (!inQueue[v]) {
                        queue.add(v);
                        inQueue[v] = true;
                        count[v]++;

                        // 入队次数 ≥ n → 存在负环
                        if (count[v] >= n) {
                            return null;
                        }
                    }
                }
            }
        }

        return dist;
    }
}
