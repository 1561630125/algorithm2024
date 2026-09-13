package OD.树与图;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-13 17:45
 */
public class 接触链排查 {

    class Solution {

        /**
         * 统计需要检测的"精确目标"人数。
         *
         * @param confirmed 已确诊人员编号列表
         * @param contacts  接触矩阵，contacts[i][j] == 1 表示 i 和 j 有接触
         * @return 与确诊者同属一个接触圈、但自身未确诊的人数
         */
        int countPreciseTestTargets(int[] confirmed, int[][] contacts) {
            int count = contacts.length;

            // ---------- 1. 初始化并查集 ----------
            // 每个节点自成一个集合
            int[] parent = new int[count];
            for (int index = 0; index < count; index++)
                parent[index] = index;

            // ---------- 2. 遍历接触矩阵的下三角，合并有接触的人 ----------
            // 只遍历 col < row 的下三角（矩阵对称，避免重复）
            for (int row = 0; row < count; row++)
                for (int col = 0; col < row; col++)
                    if (contacts[row][col] == 1)
                        union(row, col, parent);

            // ---------- 3. 标记确诊者，并收集他们所在的接触圈根节点 ----------
            boolean[] isConfirmed = new boolean[count];
            java.util.HashSet<Integer> roots = new java.util.HashSet<>();

            for (int value : confirmed) {
                if (value >= 0 && value < count) {          // 边界检查
                    isConfirmed[value] = true;
                    roots.add(find(value, parent));          // 记录该确诊者所在圈的根
                }
            }

            // ---------- 4. 统计：未确诊 且 与确诊者同圈 的人 ----------
            int result = 0;
            for (int index = 0; index < count; index++) {
                if (!isConfirmed[index]                          // 自己没确诊
                        && roots.contains(find(index, parent)))   // 但和确诊者同圈
                    result++;
            }
            return result;
        }

        /**
         * 查找 value 所在集合的根节点，带路径压缩。
         */
        private int find(int value, int[] parent) {
            while (parent[value] != value) {
                parent[value] = parent[parent[value]];   // 路径折半压缩
                value = parent[value];
            }
            return value;
        }

        /**
         * 合并 first 和 second 所在的两个集合。
         */
        private void union(int first, int second, int[] parent) {
            int firstRoot = find(first, parent);
            int secondRoot = find(second, parent);
            if (firstRoot != secondRoot)
                parent[secondRoot] = firstRoot;   // 把 second 的根挂到 first 的根下
        }
    }

}
