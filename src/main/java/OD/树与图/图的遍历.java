package OD.树与图;

import java.util.*;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-22 14:25
 */
public class 图的遍历 {

    static public class Solution {
        public int[] dfsTraversal(int n, int m, int[][] graph) {
            // write code here

            HashMap<Integer, List<Integer>> nei = new HashMap<>();
            for(int i = 0; i < graph.length; i++) {
                nei.computeIfAbsent(graph[i][0], key -> new ArrayList<>()).add(graph[i][1]);
            }

            List<Integer> res = new ArrayList<>();

            dfs(nei, 1, res);
            return res.stream()
                    .mapToInt(Integer::intValue)
                    .toArray();
        }

        void dfs(HashMap<Integer, List<Integer>> nei, int start, List<Integer> res){
            res.add(start);
            List<Integer> child = nei.get(start);
            if (child != null) {
                Collections.sort(child);
                for(int i = 0; i < child.size(); i++) {
                    dfs(nei, child.get(i), res);
                }
            }

        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        int n = 6;
        int m = 5;
        int[][] graph = new int[][]{{1,2},{1,3},{2,4},{3,5},{3,6}};
        System.out.println(Arrays.toString(solution.dfsTraversal(n, m, graph)));
    }


    /**
     * 从顶点 1 开始，优先访问较小编号邻点的深度优先遍历。
     */
    public class Solution2 {
        /**
         * @param n     顶点数量（编号 1 ~ n）
         * @param m     边的数量（本实现未直接使用）
         * @param graph 边数组，graph[i] = {u, v} 表示 u 与 v 之间有一条无向边
         * @return      从顶点 1 出发的 DFS 访问顺序
         */
        public int[] dfsTraversal(int n, int m, int[][] graph) {
            // 邻接矩阵：adjacent[u][v] = true 表示 u 与 v 相邻
            // 用 n+1 是为了让下标直接对应顶点编号 1~n
            boolean[][] adjacent = new boolean[n + 1][n + 1];

            // 构建无向图：每条边两个方向都要标记
            for (int[] edge : graph) {
                adjacent[edge[0]][edge[1]] = true;
                adjacent[edge[1]][edge[0]] = true;
            }

            // seen[u] = true 表示顶点 u 已被访问过
            boolean[] seen = new boolean[n + 1];

            // 保存 DFS 访问顺序
            int[] result = new int[n];
            int count = 0;

            // 用栈模拟递归 DFS（ArrayDeque 作为栈，push/pop 操作栈顶）
            ArrayDeque<Integer> pending = new ArrayDeque<>();
            pending.push(1); // 从顶点 1 开始

            while (!pending.isEmpty()) {
                int u = pending.pop();

                // 可能在入栈后被重复压入，出栈时若已访问则跳过
                if (seen[u])
                    continue;

                // 标记访问并记录到结果
                seen[u] = true;
                result[count++] = u;

                // 关键：为了让较小编号的邻点先被访问，
                // 按编号从大到小入栈（栈是后进先出）
                // 例如邻点为 2、3、5，则依次 push 5、3、2，
                // 出栈顺序就是 2、3、5，即小编号优先。
                for (int v = n; v >= 1; v--)
                    if (adjacent[u][v] && !seen[v])
                        pending.push(v);
            }

            // 图可能不连通，count 可能小于 n，这里截取实际访问长度
            return Arrays.copyOf(result, count);
        }
    }


    // 正确版本
    public class Solution3 {
        public int[] dfsTraversal(int n, int m, int[][] graph) {
            // 1. 建无向邻接表（双向）
            HashMap<Integer, List<Integer>> nei = new HashMap<>();
            for (int[] edge : graph) {
                int u = edge[0], v = edge[1];
                nei.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
                nei.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
            }

            // 2. 建完图后统一排序，保证小编号优先
            for (List<Integer> list : nei.values()) {
                Collections.sort(list);
            }

            boolean[] seen = new boolean[n + 1];
            List<Integer> res = new ArrayList<>();
            dfs(nei, 1, seen, res);

            return res.stream().mapToInt(Integer::intValue).toArray();
        }

        private void dfs(HashMap<Integer, List<Integer>> nei, int u,
                         boolean[] seen, List<Integer> res) {
            seen[u] = true;          // 进入时标记
            res.add(u);

            List<Integer> child = nei.get(u);
            if (child == null) return;

            for (int v : child) {
                if (!seen[v]) {       // 关键：防止重复访问
                    dfs(nei, v, seen, res);
                }
            }
        }
    }
}
