package OD.搜索与枚举;

/**
 * 考点：朴素暴力 或者滑动窗口
 *
 * @author faming.yang@hand-china.com 2026-09-12 18:15
 */
public class 方阵选址 {

    class Solution {

        /**
         * 统计矩阵中所有"边长为 sideLength 的正方形子矩阵"里，
         * 元素之和 ≥ minimumPower 的个数。
         *
         * 思路（朴素暴力）：
         *   枚举每一个边长为 sideLength 的正方形（用右下角 (bottom-1, right-1) 定位），
         *   累加该正方形内所有元素，判断是否 ≥ minimumPower。
         *
         * @param matrix       二维矩阵
         * @param sideLength   正方形边长
         * @param minimumPower 功率下限
         * @return 满足条件的正方形个数
         */
        long countPhotovoltaicSites(long[][] matrix, int sideLength, long minimumPower) {

            // 边长非法（负数）→ 无合法正方形
            if (sideLength < 0)
                return 0;

            int rows = matrix.length;
            int columns = rows == 0 ? 0 : matrix[0].length;

            long answer = 0;   // 满足条件的正方形计数

            // ---------- 枚举每个正方形的"右下边界" ----------
            // bottom 表示正方形底边所在的"行号下界"（不含），范围 [sideLength, rows]
            // right  表示正方形右边所在的"列号下界"（不含），范围 [sideLength, columns]
            //
            // 这样 (bottom - sideLength, right - sideLength) 就是正方形的左上角，
            // (bottom - 1, right - 1) 就是右下角。
            for (int bottom = sideLength; bottom <= rows; bottom++) {
                for (int right = sideLength; right <= columns; right++) {

                    // ---------- 累加当前正方形内所有元素 ----------
                    long total = 0;
                    for (int row = bottom - sideLength; row < bottom; row++) {
                        for (int column = right - sideLength; column < right; column++) {
                            total += matrix[row][column];
                        }
                    }

                    // ---------- 判断是否满足功率下限 ----------
                    if (total >= minimumPower)
                        answer++;
                }
            }

            return answer;
        }
    }



    class Solution2 {

        /**
         * 统计矩阵中所有"边长为 sideLength 的正方形子矩阵"里，
         * 元素之和 ≥ minimumPower 的个数。
         *
         * 枚举方式：枚举每个正方形的【左上顶点 (top, left)】。
         *
         * @param matrix       二维矩阵
         * @param sideLength   正方形边长
         * @param minimumPower 功率下限
         * @return 满足条件的正方形个数
         */
        long countPhotovoltaicSites(long[][] matrix, int sideLength, long minimumPower) {

            // 边长非法 → 无合法正方形
            if (sideLength <= 0)
                return 0;

            int rows = matrix.length;
            int columns = rows == 0 ? 0 : matrix[0].length;

            // 边长超过矩阵尺寸 → 放不下任何正方形
            if (sideLength > rows || sideLength > columns)
                return 0;

            long answer = 0;

            // ---------- 枚举左上顶点 (top, left) ----------
            // 条件 top + sideLength <= rows 保证正方形不超出下边界
            // 条件 left + sideLength <= columns 保证不超出右边界
            for (int top = 0; top + sideLength <= rows; top++) {
                for (int left = 0; left + sideLength <= columns; left++) {

                    // ---------- 累加正方形内所有元素 ----------
                    long total = 0;
                    for (int row = top; row < top + sideLength; row++) {
                        for (int column = left; column < left + sideLength; column++) {
                            total += matrix[row][column];
                        }
                    }

                    // ---------- 判断 ----------
                    if (total >= minimumPower)
                        answer++;
                }
            }

            return answer;
        }
    }


    class Solution3 {

        /**
         * 统计矩阵中所有"边长为 sideLength 的正方形子矩阵"里，
         * 元素之和 >= minimumPower 的个数。
         *
         * 优化：用二维前缀和把每个正方形的求和从 O(k²) 降到 O(1)。
         *
         * @param matrix       二维矩阵
         * @param sideLength   正方形边长
         * @param minimumPower 功率下限
         * @return 满足条件的正方形个数
         */
        long countPhotovoltaicSites(long[][] matrix, int sideLength, long minimumPower) {

            // ---------- 1. 边界处理 ----------
            if (sideLength <= 0)
                return 0;

            int rows = matrix.length;
            int columns = rows == 0 ? 0 : matrix[0].length;

            // 边长超过矩阵尺寸 → 放不下任何正方形
            if (sideLength > rows || sideLength > columns)
                return 0;

            // ---------- 2. 构建二维前缀和 ----------
            // prefix[i][j] = 矩阵中 (0,0) 到 (i-1,j-1) 所有元素之和
            //   prefix[0][*] = prefix[*][0] = 0（哨兵，简化边界）
            long[][] prefix = new long[rows + 1][columns + 1];

            for (int i = 1; i <= rows; i++) {
                for (int j = 1; j <= columns; j++) {
                    prefix[i][j] = matrix[i - 1][j - 1]
                            + prefix[i - 1][j]      // 上方
                            + prefix[i][j - 1]      // 左方
                            - prefix[i - 1][j - 1]; // 左上被加了两次，减掉
                }
            }

            // ---------- 3. 枚举每个正方形的左上顶点，O(1) 求和 ----------
            long answer = 0;

            for (int top = 0; top + sideLength <= rows; top++) {
                for (int left = 0; left + sideLength <= columns; left++) {

                    int r1 = top;                       // 左上角行
                    int c1 = left;                      // 左上角列
                    int r2 = top + sideLength - 1;      // 右下角行
                    int c2 = left + sideLength - 1;     // 右下角列

                    // 子矩阵和公式（用前缀和的"右下+1"边界）:
                    //   sum = prefix[r2+1][c2+1]
                    //       - prefix[r1][c2+1]      ← 减去上方矩形
                    //       - prefix[r2+1][c1]      ← 减去左方矩形
                    //       + prefix[r1][c1]        ← 加回被减两次的左上角
                    long total = prefix[r2 + 1][c2 + 1]
                            - prefix[r1][c2 + 1]
                            - prefix[r2 + 1][c1]
                            + prefix[r1][c1];

                    if (total >= minimumPower)
                        answer++;
                }
            }

            return answer;
        }
    }


    class Solution4 {

        /**
         * 统计矩阵中所有"边长为 sideLength 的正方形子矩阵"里，
         * 元素之和 >= minimumPower 的个数。
         *
         * 优化：固定正方形上下边界，用水平滑动窗口维护列和，
         *       每滑动一格只需 O(k) 更新（或 O(1) 增量更新）。
         *
         * @param matrix       二维矩阵
         * @param sideLength   正方形边长
         * @param minimumPower 功率下限
         * @return 满足条件的正方形个数
         */
        long countPhotovoltaicSites(long[][] matrix, int sideLength, long minimumPower) {
            // ---------- 1. 边界处理 ----------
            if (sideLength <= 0)
                return 0;

            int rows = matrix.length;
            int columns = rows == 0 ? 0 : matrix[0].length;

            if (sideLength > rows || sideLength > columns)
                return 0;

            long answer = 0;

            // colSum[c] = 当前正方形"上下边界内"第 c 列的元素之和
            long[] colSum = new long[columns];

            // ---------- 2. 枚举正方形的【上边界 top】 ----------
            for (int top = 0; top + sideLength <= rows; top++) {

                // 每次换了上边界，重新计算每列的窗口和
                //   这里直接重新累加 k 行，是 O(C·k)，总 O(R·C·k)
                //   也可用增量更新优化到 O(C)
                java.util.Arrays.fill(colSum, 0);

                for (int row = top; row < top + sideLength; row++)
                    for (int c = 0; c < columns; c++)
                        colSum[c] += matrix[row][c];

                // ---------- 3. 水平滑动窗口：初始窗口 [0, k) ----------
                long window = 0;
                for (int c = 0; c < sideLength; c++)
                    window += colSum[c];

                if (window >= minimumPower)
                    answer++;

                // 窗口向右滑动：每次右移一格
                for (int left = 1; left + sideLength <= columns; left++) {
                    int outCol = left - 1;                  // 移出的列
                    int inCol = left + sideLength - 1;      // 移入的列

                    window += colSum[inCol] - colSum[outCol];

                    if (window >= minimumPower)
                        answer++;
                }
            }

            return answer;
        }
    }


}
