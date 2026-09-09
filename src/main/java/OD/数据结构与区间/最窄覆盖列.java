package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 16:30
 */
public class 最窄覆盖列 {

    int minMatrixWidth(int[][] matrix, int[] required) {

        // 统计required数字出现次数
        //

        return -1;
    }


    /**
     * 寻找最小矩阵宽度（包含指定数字的最小列区间）
     * <p>
     * 题目背景：
     * 给定一个矩阵，每一行代表一个序列，需要在矩阵中找到一个宽度最小的列区间，
     * 使得每一行在这个区间内都包含了 required 数组中指定的所有数字。
     * <p>
     * 通俗理解：
     * 矩阵的每一行都有一系列数字，我们需要从列的角度截取一个连续区间，
     * 让每一行都至少包含 required 里的所有数字。
     * <p>
     * 例如：
     * matrix = [
     * [1, 2, 3, 4],
     * [2, 3, 1, 5],
     * [3, 1, 2, 6]
     * ]
     * required = [1, 2, 3]
     * <p>
     * 列区间 [0, 2] 宽度 3：
     * 第0行：包含 1,2,3 ✅
     * 第1行：包含 2,3,1 ✅
     * 第2行：包含 3,1,2 ✅
     * <p>
     * 列区间 [1, 3] 宽度 3：
     * 第0行：包含 2,3,4 → 缺少1 ❌
     * <p>
     * 所以最小宽度为 3
     *
     * @param matrix   输入矩阵
     * @param required 每行需要包含的数字列表
     * @return 最小列区间宽度，如果不存在返回 -1
     */
    int minMatrixWidth2(int[][] matrix, int[] required) {
        // ========== 1. 参数校验 ==========
        if (required == null || required.length == 0)
            return 0;  // 不需要任何数字，宽度为 0

        if (matrix == null || matrix.length == 0 || matrix[0] == null || matrix[0].length == 0)
            return -1;

        int columns = matrix[0].length;

        // 检查矩阵是否规整
        for (int[] row : matrix)
            if (row == null || row.length != columns)
                return -1;

        // ========== 2. 统计需求 ==========
        // need: required 中每个数字需要的次数
        java.util.Map<Integer, Integer> need = new java.util.HashMap<>();
        for (int value : required) {
            need.put(value, need.getOrDefault(value, 0) + 1);
        }

        // ========== 3. 滑动窗口 ==========
        // have: 当前窗口中各数字出现的总次数（所有行累加）
        java.util.Map<Integer, Integer> have = new java.util.HashMap<>();
        int matched = 0;    // 已匹配的需求总数
        int left = 0;       // 窗口左边界
        int best = columns + 1;  // 最小宽度（初始为不可能的值）

        // 遍历每一列作为右边界
        for (int right = 0; right < columns; right++) {
            // ====== 3.1 扩展窗口：加入第 right 列 ======
            // 遍历每一行，将当前列的值加入窗口
            for (int[] row : matrix) {
                int value = row[right];
                int count = have.getOrDefault(value, 0);

                // 如果该数字还没达到需求，匹配数 +1
                if (count < need.getOrDefault(value, 0)) {
                    matched++;
                }
                have.put(value, count + 1);
            }

            // ====== 3.2 收缩窗口：当所有需求都满足时 ======
            // 当 matched == required.length 时，说明窗口中包含了所有需要的数字
            while (matched == required.length) {
                // 更新最小宽度
                best = Math.min(best, right - left + 1);

                // ====== 收缩左边界：移除第 left 列 ======
                for (int[] row : matrix) {
                    int value = row[left];
                    int count = have.get(value) - 1;
                    have.put(value, count);

                    // 如果移除后该数字不再满足需求，匹配数 -1
                    if (count < need.getOrDefault(value, 0)) {
                        matched--;
                    }
                }
                left++;
            }
        }

        // ========== 4. 返回结果 ==========
        return best <= columns ? best : -1;
    }

    int minMatrixWidthCorrect(int[][] matrix, int[] required) {
        if (required == null || required.length == 0) return 0;
        if (matrix == null || matrix.length == 0) return -1;

        int rows = matrix.length;
        int cols = matrix[0].length;

        // need: 每个数字需要的次数（单行需求）
        java.util.Map<Integer, Integer> need = new java.util.HashMap<>();
        for (int value : required) {
            need.put(value, need.getOrDefault(value, 0) + 1);
        }

        // 每行独立的窗口状态
        java.util.Map<Integer, Integer>[] rowHave = new java.util.HashMap[rows];
        for (int i = 0; i < rows; i++) {
            rowHave[i] = new java.util.HashMap<>();
        }

        int[] rowMatched = new int[rows];  // 每行已匹配的数字数
        int left = 0;
        int best = cols + 1;
        int rowsFullyMatched = 0;  // 完全匹配的行数

        for (int right = 0; right < cols; right++) {
            // 扩展窗口
            for (int row = 0; row < rows; row++) {
                int value = matrix[row][right];
                int count = rowHave[row].getOrDefault(value, 0);
                rowHave[row].put(value, count + 1);

                // 如果该数字达到需求且之前未达到，匹配数+1
                if (count + 1 == need.getOrDefault(value, 0)) {
                    rowMatched[row]++;
                }

                // 如果该行所有需求都匹配了
                if (rowMatched[row] == need.size()) {
                    rowsFullyMatched++;
                }
            }

            // 收缩窗口
            while (rowsFullyMatched == rows) {
                // 更新最小宽度
                best = Math.min(best, right - left + 1);

                // 移除左边界
                for (int row = 0; row < rows; row++) {
                    int value = matrix[row][left];
                    int count = rowHave[row].get(value) - 1;
                    rowHave[row].put(value, count);

                    // 如果该数字不再满足需求，匹配数-1
                    if (count == need.getOrDefault(value, 0) - 1) {
                        rowMatched[row]--;
                    }

                    // 如果该行不再完全匹配
                    if (rowMatched[row] == need.size() - 1) {
                        rowsFullyMatched--;
                    }
                }
                left++;
            }
        }

        return best <= cols ? best : -1;
    }
}
