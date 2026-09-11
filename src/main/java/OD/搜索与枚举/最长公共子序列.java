package OD.搜索与枚举;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 19:01
 */
public class 最长公共子序列 {

    class Solution {
        private char[] s, t;      // 两个字符串的字符数组
        private int[][] memo;     // memo[i][j]：s[0..i] 和 t[0..j] 的 LCS 长度，-1 表示没算过

        public int longestCommonSubsequence(String text1, String text2) {
            s = text1.toCharArray();
            t = text2.toCharArray();
            int n = s.length;
            int m = t.length;

            memo = new int[n][m];
            for (int[] row : memo) {
                Arrays.fill(row, -1);  // -1 表示还没计算
            }

            // 从两个串的末尾开始递归
            return dfs(n - 1, m - 1);
        }

        /**
         * 求 s[0..i] 和 t[0..j] 的最长公共子序列长度
         *
         * @param i s 的下标（从后往前）
         * @param j t 的下标（从后往前）
         */
        private int dfs(int i, int j) {
            // 任一串为空，公共子序列长度为 0
            if (i < 0 || j < 0) {
                return 0;
            }

            // 已经算过，直接返回（记忆化）
            if (memo[i][j] != -1) {
                return memo[i][j];
            }

            // 末尾字符相同：这个字符一定在 LCS 里，长度 +1，继续看前面的
            if (s[i] == t[j]) {
                return memo[i][j] = dfs(i - 1, j - 1) + 1;
            }

            // 末尾字符不同：要么去掉 s[i]，要么去掉 t[j]，取较大值
            return memo[i][j] = Math.max(dfs(i - 1, j), dfs(i, j - 1));
        }
    }


    class Solution2 {
        public int longestCommonSubsequence(String text1, String text2) {
            char[] s = text1.toCharArray();
            char[] t = text2.toCharArray();
            int n = s.length;
            int m = t.length;

            // f[i][j]：s 的前 i 个字符 和 t 的前 j 个字符 的 LCS 长度
            // 多开一行一列，方便处理空串的边界
            int[][] f = new int[n + 1][m + 1];

            // 从前往后递推
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {

                    // 比较 s[i] 和 t[j]（注意下标，s 的前 i+1 个字符末尾是 s[i]）
                    if (s[i] == t[j]) {
                        // 末尾相同，LCS 长度 = 去掉这两个字符后的 LCS + 1
                        f[i + 1][j + 1] = f[i][j] + 1;
                    } else {
                        // 末尾不同，取「去掉 s[i]」和「去掉 t[j]」的较大值
                        f[i + 1][j + 1] = Math.max(f[i][j + 1], f[i + 1][j]);
                    }
                }
            }

            // s 全部 n 个字符 和 t 全部 m 个字符 的 LCS
            return f[n][m];
        }
    }

}
