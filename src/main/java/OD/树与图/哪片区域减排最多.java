package OD.树与图;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-14 15:42
 */
public class 哪片区域减排最多 {


    public class Solution {

        /**
         * 计算最大碳减排量
         *
         * @param green   每个节点的绿色值（如绿电、绿证等带来的减排量）
         * @param carbon  每个节点的碳排放量
         * @param edges   无向图的边，edges[i] = {u, v} 表示节点 u 和 v 相连
         * @return        最大碳减排量；如果没有边或没有符合条件的连通分量，返回 -1
         */
        public int getMaxCarbonReducetion(int[] green, int[] carbon, int[][] edges) {
            // 没有任何边，说明没有可以合并的连通分量，直接返回 -1
            if (edges.length == 0)
                return -1;

            // 并查集初始化：每个节点一开始都是自己的父节点
            int[] parent = new int[green.length];
            for (int index = 0; index < parent.length; index++)
                parent[index] = index;

            // 遍历所有边，合并两个端点所在的集合
            for (int[] edge : edges) {
                int left = find(edge[0], parent);   // 查找 edge[0] 的根节点
                int right = find(edge[1], parent);  // 查找 edge[1] 的根节点

                // 如果两个端点不在同一集合，则合并
                // 这里让编号较大的根节点指向编号较小的根节点，保证根节点较小
                if (left != right)
                    parent[Math.max(left, right)] = Math.min(left, right);
            }

            // counts[i]      以 i 为根的连通分量中包含多少个节点
            // greenSums[i]   以 i 为根的连通分量中 green 值总和
            // carbonSums[i]  以 i 为根的连通分量中 carbon 值总和
            int[] counts = new int[green.length];
            int[] greenSums = new int[green.length];
            int[] carbonSums = new int[green.length];

            // 遍历所有节点，把信息累加到各自连通分量的根节点上
            for (int index = 0; index < green.length; index++) {
                int root = find(index, parent);   // 找到当前节点所属连通分量的根
                counts[root]++;                   // 该连通分量节点数 +1
                greenSums[root] += green[index];  // 累加 green
                carbonSums[root] += carbon[index];// 累加 carbon
            }

            // answer 记录最终答案，初始为 -1
            int answer = -1;

            // 只考虑节点数 >= 2 的连通分量
            for (int index = 0; index < green.length; index++) {
                if (counts[index] >= 2) {
                    // 计算该连通分量的净减排量：
                    // (green总和 - carbon总和) * 节点数
                    // 如果净减排量为负，则取 0，表示不产生减排也不产生额外排放
                    int reduction = Math.max(0, (greenSums[index] - carbonSums[index]) * counts[index]);

                    // 更新最大值
                    answer = Math.max(answer, reduction);
                }
            }

            // 返回最大碳减排量
            return answer;
        }

        /**
         * 并查集查找函数，带路径压缩
         *
         * @param node   要查找的节点
         * @param parent 并查集父节点数组
         * @return       该节点所在集合的根节点
         */
        private int find(int node, int[] parent) {
            // 如果当前节点不是根节点，则递归查找其父节点的根，并进行路径压缩
            if (parent[node] != node)
                parent[node] = find(parent[node], parent);

            // 返回根节点
            return parent[node];
        }
    }

}
