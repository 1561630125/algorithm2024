package OD.贪心和动态规划;

/**
 * 考点：Dp
 *
 * @author faming.yang@hand-china.com 2026-09-15 15:09
 */
public class 字符串网格捷径 {

    class Solution {
        /**
         * 计算将字符串 first 转换成字符串 second 所需的「最短路径」长度。
         *
         * 实际上这是一个带权编辑距离问题：
         *   - 删除一个字符（previous[col] -> 当前列）代价 +1
         *   - 插入一个字符（current[col-1] -> 当前列）代价 +1
         *   - 替换一个字符（previous[col-1] -> 当前列）代价 +1
         *
         * 但注意：这里即使字符相同也允许走「替换」路径，
         * 且替换代价为 +1（而不是常见的 0 或 +1）。
         * 因此当字符相同时，取 min(删除/插入路径, 替换路径)。
         * 由于字符相同，替换路径代价其实和跳过路径一样，这里只是把
         * 相同字符的「匹配」也纳入到 +1 的代价计算里。
         *
         * 时间：O(first.length() * second.length())
         * 空间：O(first.length())  —— 滚动数组优化
         */
        int shortestStringPath(String first, String second) {
            // previous 表示上一行（处理 second 的前 row-1 个字符时）的 DP 结果
            // current  表示当前行（处理 second 的前 row 个字符时）的 DP 结果
            // DP 含义：previous[col] = 把 first 的前 col 个字符
            //                      变成 second 的前 (row-1) 个字符所需的最短路径
            int[] previous = new int[first.length() + 1];
            int[] current = new int[first.length() + 1];

            // 边界条件：second 为空串时，first 的前 col 个字符需要 col 次删除
            for (int col = 0; col <= first.length(); col++) {
                previous[col] = col;
            }

            // 逐行处理 second 的每个字符
            for (int row = 1; row <= second.length(); row++) {
                // 边界条件：first 为空串时，需要 row 次插入
                current[0] = row;

                // 逐列处理 first 的每个字符
                for (int col = 1; col <= first.length(); col++) {
                    // 候选 1：删除 first 的第 col 个字符 或 插入 second 的第 row 个字符
                    // previous[col]      -> 删除 first[col-1]
                    // current[col - 1]   -> 插入 second[row-1]
                    current[col] = Math.min(previous[col], current[col - 1]) + 1;

                    // 候选 2：字符相同，走「替换/匹配」路径
                    // previous[col - 1]  -> 替换（或匹配）first[col-1] 与 second[row-1]
                    if (first.charAt(col - 1) == second.charAt(row - 1)) {
                        current[col] = Math.min(current[col], previous[col - 1] + 1);
                    }
                }

                // 滚动数组：交换 previous 和 current，为下一行做准备
                int[] swap = previous;
                previous = current;
                current = swap;
            }

            // 最终结果：把整个 first 变成整个 second 的最短路径
            return previous[first.length()];
        }
    }


    class Solution2 {
        int shortestStringPath(String first, String second) {
            int n = first.length();
            int m = second.length();

            // 有一个字符串为空串，直接返回长度和
            if (n * m == 0) {
                return n + m;
            }

            // DP 数组，用 D[i][j] 表示
            // first 的前 i 个字符 和 second 的前 j 个字符 之间的最短路径
            int[][] D = new int[n + 1][m + 1];

            // 边界状态初始化
            // second 为空：first 的前 i 个字符需要 i 步
            for (int i = 0; i <= n; i++) {
                D[i][0] = i;
            }
            // first 为空：second 的前 j 个字符需要 j 步
            for (int j = 0; j <= m; j++) {
                D[0][j] = j;
            }

            // 计算所有 DP 值
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= m; j++) {
                    // 候选 1：删除 first[i-1]（从上方来）
                    int up = D[i - 1][j] + 1;

                    // 候选 2：插入 second[j-1]（从左方来）
                    int left = D[i][j - 1] + 1;

                    // 候选 3：替换 / 匹配（从左上角来）
                    int up_left = D[i - 1][j - 1];
                    if (first.charAt(i - 1) == second.charAt(j - 1)) {
                        // 本题特殊：字符相同时也要 +1（匹配也计费）
                        up_left += 1;
                    } else {
                        // 字符不同：替换，也 +1
                        up_left += 1;
                    }

                    // 三者取最小
                    D[i][j] = Math.min(up, Math.min(left, up_left));
                }
            }

            return D[n][m];
        }
    }

}
