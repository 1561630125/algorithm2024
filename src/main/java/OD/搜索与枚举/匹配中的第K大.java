package OD.搜索与枚举;

/**
 * 考点：二分答案+递归; 太难了呀
 *
 * @author faming.yang@hand-china.com 2026-09-12 13:22
 */
public class 匹配中的第K大 {
    class Solution {

        /**
         * 返回第 k 大的"最小匹配阈值"。
         * <p>
         * 问题本质：
         * 在矩阵中找出一个"行到列"的匹配（每行最多匹配一列，每列最多匹配一行），
         * 匹配的边要求 matrix[row][col] <= threshold。
         * 对于一个给定的 threshold，能匹配上的行数记为 matchingCount。
         * 我们要找最小的 threshold，使得 matchingCount >= (rows - k + 1)，
         * 即至少有 (rows - k + 1) 行能匹配。
         * <p>
         * 用二分答案：threshold 越大，能用的边越多，匹配数单调不减，
         * 所以可以在"去重后的值域"上二分。
         */
        int minKthLargestMatch(int[][] matrix, int k) {

            // 边界检查：空矩阵 / k 越界，返回 0
            if (matrix.length == 0 || matrix[0].length == 0 || k < 1 || k > matrix.length) return 0;

            // 1. 把所有元素摊平到一维数组
            int[] values = new int[matrix.length * matrix[0].length];
            int size = 0;
            for (int[] row : matrix)
                for (int value : row)
                    values[size++] = value;

            // 2. 排序，为去重和二分做准备
            java.util.Arrays.sort(values);

            // 3. 原地去重，unique 是去重后元素个数
            int unique = 0;
            for (int value : values)
                if (unique == 0 || value != values[unique - 1]) values[unique++] = value;

            // 4. 目标匹配行数：
            //    要找第 k 大的阈值，等价于让 (rows - k + 1) 行能匹配。
            //    例如 rows=5, k=1（最大），target=5，即所有行都要匹配；
            //    rows=5, k=5（最小），target=1，只要 1 行能匹配。
            int target = matrix.length - k + 1;

            // 5. 在去重后的值域 [0, unique-1] 上二分最小可行阈值
            int left = 0, right = unique - 1;
            while (left < right) {
                int middle = left + (right - left) / 2;
                // 以 values[middle] 作为阈值，看能匹配多少行
                if (matchingCount(matrix, values[middle]) >= target) right = middle;          // 可行，尝试更小的阈值
                else left = middle + 1;       // 不可行，必须放大阈值
            }
            return values[left];             // 最小可行的阈值
        }

        /**
         * 计算以 threshold 为上限时，最多能匹配多少行。
         * 使用匈牙利算法（二分图最大匹配）：
         * 左部 = 行，右部 = 列，
         * 边 (row, col) 存在当且仅当 matrix[row][col] <= threshold。
         * 每行最多配一列，每列最多配一行。
         */
        private int matchingCount(int[][] matrix, int threshold) {

            // match[col] = 与第 col 列匹配的行号，-1 表示未匹配
            int[] match = new int[matrix[0].length];
            java.util.Arrays.fill(match, -1);

            int count = 0;

            // 依次为每一行尝试找增广路
            for (int row = 0; row < matrix.length; row++) {

                // visited 记录本轮 DFS 中访问过的列，避免重复访问形成环
                boolean[] visited = new boolean[matrix[0].length];

                // 找到一条增广路，匹配数 +1
                if (augment(row, threshold, matrix, match, visited)) count++;
            }
            return count;
        }

        /**
         * 匈牙利算法的增广路搜索（DFS）。
         * <p>
         * 目标：为第 row 行找一个可用的列 col：
         * 1) matrix[row][col] <= threshold （边存在）
         * 2) col 尚未被访问过
         * 如果 col 已经被别人（match[col]）占了，就尝试让 match[col] 那一行
         * 去换一列（递归 augment），把 col 让出来。
         *
         * @return true 表示成功为 row 找到匹配
         */
        private boolean augment(int row, int threshold, int[][] matrix, int[] match, boolean[] visited) {
            for (int col = 0; col < matrix[row].length; col++) {

                // 边不存在（值超过阈值），或本轮已访问过该列，跳过
                if (matrix[row][col] > threshold || visited[col]) continue;

                visited[col] = true;

                // 该列空闲，或者可以让原占用者换一列 → 匹配成功
                if (match[col] == -1 || augment(match[col], threshold, matrix, match, visited)) {
                    match[col] = row;   // 把 col 配给 row
                    return true;
                }
            }
            // 所有可用列都试过了，无法为 row 找到匹配
            return false;
        }
    }

}
