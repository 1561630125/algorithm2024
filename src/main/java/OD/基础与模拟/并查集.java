package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 14:37
 */
public class 并查集 {

    class UnionFind {
        private int[] parent;  // 父节点数组
        private int[] rank;    // 秩数组（记录树的高度）
        private int count;     // 连通分量个数（有多少个独立的圈子）

        // 1. 初始化
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            count = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i;  // 初始时，每个元素的父节点是自己
                rank[i] = 1;    // 初始高度为1
            }
        }

        // 2. 查找（带路径压缩）
        public int find(int x) {
            if (parent[x] != x) {
                // 递归路径压缩：直接让当前节点指向根节点
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        // 3. 合并（按秩合并）
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return; // 已经在同一集合，无需合并

            // 按秩合并：将矮树挂到高树下，保持平衡
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                // 高度相等时，随便选一个作为根，并将高度+1
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            count--; // 合并成功，连通分量减1
        }

        // 4. 判断两个节点是否连通
        public boolean connected(int x, int y) {
            return find(x) == find(y);
        }

        // 5. 返回当前的连通分量个数
        public int getCount() {
            return count;
        }
    }

}
