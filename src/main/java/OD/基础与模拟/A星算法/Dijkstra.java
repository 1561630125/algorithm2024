package OD.基础与模拟.A星算法;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 20:33
 */
public class Dijkstra {

    /**
     * 图的边
     */
    static class Edge {
        int target;      // 目标节点
        int weight;      // 边的权重

        public Edge(int target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    /**
     * 节点状态（用于优先队列）
     */
    static class NodeState implements Comparable<NodeState> {
        int node;        // 节点编号
        int distance;    // 从起点到该节点的距离

        public NodeState(int node, int distance) {
            this.node = node;
            this.distance = distance;
        }

        @Override
        public int compareTo(NodeState other) {
            return Integer.compare(this.distance, other.distance);
        }
    }


    /**
     * Dijkstra 算法（使用优先队列优化）
     *
     * @param graph 邻接表表示的图
     * @param start 起点节点编号
     * @return dist 数组，dist[i] = 从起点到节点 i 的最短距离
     */
    public static int[] dijkstra(List<List<Edge>> graph, int start) {
        int n = graph.size();           // 节点总数
        int[] dist = new int[n];        // 距离数组
        boolean[] visited = new boolean[n];  // 是否已确定最短路径

        // 初始化：起点距离为 0，其他为无穷大
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;

        // 优先队列（按距离升序）
        PriorityQueue<NodeState> pq = new PriorityQueue<>();
        pq.offer(new NodeState(start, 0));

        while (!pq.isEmpty()) {
            // 取出当前距离最小的节点
            NodeState current = pq.poll();
            int u = current.node;
            int d = current.distance;

            // 如果该节点已处理，跳过（防止重复处理）
            if (visited[u]) {
                continue;
            }
            visited[u] = true;

            // 如果当前距离已经不是最新值，跳过
            if (d > dist[u]) {
                continue;
            }

            // 松弛：遍历所有邻居
            for (Edge edge : graph.get(u)) {
                int v = edge.target;
                int w = edge.weight;

                // 如果通过 u 到 v 的距离更短，更新
                if (!visited[v] && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.offer(new NodeState(v, dist[v]));
                }
            }
        }

        return dist;
    }


    public static void main(String[] args) {
        // 构建图
        int n = 6;
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
        }

        // 添加边（无向图）
        // 0-1: 4,  0-2: 2
        // 1-2: 1,  1-3: 5
        // 2-3: 8,  2-4: 10
        // 3-4: 2,  3-5: 6
        // 4-5: 3
        addUndirectedEdge(graph, 0, 1, 4);
        addUndirectedEdge(graph, 0, 2, 2);
        addUndirectedEdge(graph, 1, 2, 1);
        addUndirectedEdge(graph, 1, 3, 5);
        addUndirectedEdge(graph, 2, 3, 8);
        addUndirectedEdge(graph, 2, 4, 10);
        addUndirectedEdge(graph, 3, 4, 2);
        addUndirectedEdge(graph, 3, 5, 6);
        addUndirectedEdge(graph, 4, 5, 3);

        // 从节点 0 开始
        int start = 0;
        int[] dist = dijkstra(graph, start);

        System.out.println("从节点 " + start + " 到各节点的最短距离：");
        for (int i = 0; i < dist.length; i++) {
            System.out.printf("节点 %d: %s\n", i,
                    dist[i] == Integer.MAX_VALUE ? "∞" : dist[i]);
        }
    }

    private static void addUndirectedEdge(List<List<Edge>> graph, int u, int v, int w) {
        graph.get(u).add(new Edge(v, w));
        graph.get(v).add(new Edge(u, w));
    }

}
