package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-13 16:43
 */
public class 节点联动区域 {


    public class Solution {

        /**
         * 计算爆炸区域（连通分量）的数量。
         *
         * @param isChainExplosion 邻接矩阵，size × size
         *                         isChainExplosion[i][j] == 1 表示 i 和 j 会连锁爆炸
         * @return 连通分量的个数
         */
        public int countBombField(int[][] isChainExplosion) {
            int size = isChainExplosion.length;

            // ---------- 1. 初始化并查集 ----------
            // 每个节点一开始自成一个集合，父节点指向自己
            int[] parent = new int[size];
            for (int index = 0; index < size; index++)
                parent[index] = index;

            // ---------- 2. 遍历矩阵上三角，合并有连锁关系的节点 ----------
            // 只遍历 column > row 的上三角，因为无向图对称，避免重复处理
            for (int row = 0; row < size; row++) {
                for (int column = row + 1; column < size; column++) {
                    if (isChainExplosion[row][column] == 1) {
                        int left = find(parent, row);       // row 所在集合的根
                        int right = find(parent, column);   // column 所在集合的根
                        if (left != right)
                            parent[right] = left;           // 合并两个集合
                    }
                }
            }

            // ---------- 3. 统计不同的根节点数量 = 连通分量数 ----------
            java.util.Set<Integer> roots = new java.util.HashSet<>();
            for (int index = 0; index < size; index++)
                roots.add(find(parent, index));
            return roots.size();
        }

        /**
         * 查找 value 所在集合的根节点，带路径压缩。
         *
         * @param parent 并查集父数组
         * @param value  要查找的节点
         * @return 根节点编号
         */
        private int find(int[] parent, int value) {
            // 一路向上找根，同时做路径压缩（把沿途节点直接挂到祖父节点）
            while (parent[value] != value) {
                parent[value] = parent[parent[value]];  // 路径压缩：跳过一层
                value = parent[value];
            }
            return value;
        }
    }


    public class Solution2 {
        public int countBombField(int[][] isChainExplosion) {
            int size = isChainExplosion.length;
            boolean[] visited = new boolean[size];
            int count = 0;

            for (int start = 0; start < size; start++) {
                if (visited[start]) continue;      // 已属于某个连通块，跳过

                count++;                            // 发现一个新的连通块
                java.util.ArrayDeque<Integer> queue = new java.util.ArrayDeque<>();
                queue.add(start);
                visited[start] = true;

                while (!queue.isEmpty()) {
                    int cur = queue.remove();
                    // 找所有和 cur 相连且未访问的节点
                    for (int next = 0; next < size; next++) {
                        if (isChainExplosion[cur][next] == 1 && !visited[next]) {
                            visited[next] = true;
                            queue.add(next);
                        }
                    }
                }
            }
            return count;
        }
    }

}
