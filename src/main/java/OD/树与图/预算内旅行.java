package OD.树与图;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-14 15:56
 */
public class 预算内旅行 {


    public class Solution {

        /**
         * 计算从节点 1 到节点 n 的最小旅行开销
         *
         * @param n      节点数量（节点编号从 1 到 n）
         * @param m      边的数量（本题中未直接使用，可通过 roads.length 得到）
         * @param budget 预算上限
         * @param roads  道路数组，roads[i] = {u, v, w} 表示 u 到 v 有一条权重为 w 的边
         * @return       如果 1 到 n 的最短距离 <= budget，返回该最短距离；否则返回 -1
         */
        public int minimumTravelCost(int n, int m, int budget, int[][] roads) {

            // 邻接表：graph.get(u) 保存从 u 出发的所有边，每条边用 int[]{v, w} 表示
            java.util.List<java.util.List<int[]>> graph = new java.util.ArrayList<>();

            // 节点编号从 1 到 n，所以申请 n+1 个列表，下标 0 不用
            for (int i = 0; i <= n; i++)
                graph.add(new java.util.ArrayList<>());

            // 构建无向图（这里默认 roads 中的边是双向的，所以两个方向都要加）
            for (int[] road : roads)
                graph.get(road[0]).add(new int[] {
                        road[1], road[2]   // 从 road[0] 到 road[1]，权重为 road[2]
                });

            // 注意：如果 roads 中的边是单向的，上面只加了一个方向；
            // 如果是双向边，通常还需要再加一行：
            // graph.get(road[1]).add(new int[] { road[0], road[2] });

            // distance[i] 表示从起点 1 到节点 i 的当前最短距离
            long[] distance = new long[n + 1];

            // 初始化为一个很大的值（Long.MAX_VALUE / 4 避免加法溢出）
            java.util.Arrays.fill(distance, Long.MAX_VALUE / 4);

            // 起点 1 到自己的距离为 0
            distance[1] = 0;

            // 优先队列（小顶堆），存放 long[]{当前距离, 节点编号}
            // 按距离从小到大排序，保证每次取出的是当前距离最小的节点
            java.util.PriorityQueue<long[]> queue = new java.util.PriorityQueue<>(
                    (a, b) -> Long.compare(a[0], b[0])
            );

            // 把起点加入队列
            queue.add(new long[] {
                    0, 1
            });

            // Dijkstra 主循环
            while (!queue.isEmpty()) {
                long[] current = queue.remove();     // 取出当前距离最小的节点
                int node = (int) current[1];         // 当前节点编号

                // 如果取出的距离不是最新记录，说明是过期数据，跳过
                if (current[0] != distance[node])
                    continue;

                // 遍历当前节点的所有邻居
                for (int[] edge : graph.get(node)) {
                    // 候选距离 = 当前节点距离 + 这条边的权重
                    long candidate = current[0] + edge[1];

                    // 如果找到更短的路径，则更新距离并加入队列
                    if (candidate < distance[edge[0]]) {
                        distance[edge[0]] = candidate;
                        queue.add(new long[] {
                                candidate, edge[0]
                        });
                    }
                }
            }

            // 如果 1 到 n 的最短距离不超过预算，返回该距离（转成 int）；否则返回 -1
            return distance[n] <= budget ? (int) distance[n] : -1;
        }
    }

}
