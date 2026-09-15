package 力扣.多维动态规划.A72;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-11-01 16:05
 */
public class 编辑距离 {

    class Solution {
        private char[] s, t;
        private int[][] memo;

        public int minDistance(String text1, String text2) {
            s = text1.toCharArray();
            t = text2.toCharArray();
            int n = s.length;
            int m = t.length;
            memo = new int[n][m];
            for (int[] row : memo) {
                Arrays.fill(row, -1); // -1 表示还没有计算过
            }
            return dfs(n - 1, m - 1);
        }

        private int dfs(int i, int j) {
            if (i < 0) {
                return j + 1;
            }
            if (j < 0) {
                return i + 1;
            }
            if (memo[i][j] != -1) { // 之前算过了
                return memo[i][j];
            }
            if (s[i] == t[j]) {
                return memo[i][j] = dfs(i - 1, j - 1);
            }
            return memo[i][j] = Math.min(Math.min(dfs(i - 1, j), dfs(i, j - 1)), dfs(i - 1, j - 1)) + 1;
        }
    }


    class Solution2 {
        public int minDistance(String text1, String text2) {
            char[] s = text1.toCharArray();
            char[] t = text2.toCharArray();
            int n = s.length;
            int m = t.length;
            int[][] f = new int[n + 1][m + 1];
            for (int j = 0; j < m; j++) {
                f[0][j + 1] = j + 1;
            }
            for (int i = 0; i < n; i++) {
                f[i + 1][0] = i + 1;
                for (int j = 0; j < m; j++) {
                    f[i + 1][j + 1] = s[i] == t[j] ? f[i][j] :
                            Math.min(Math.min(f[i][j + 1], f[i + 1][j]), f[i][j]) + 1;
                }
            }
            return f[n][m];
        }
    }

}


class Solution {
    public int minDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();

        // 有一个字符串为空串
        if (n * m == 0) {
            return n + m;
        }

        // DP 数组，用 D[i][j] 表示 A 的前 i 个字母和 B 的前 j 个字母之间的编辑距离
        int[][] D = new int[n + 1][m + 1];

        // 边界状态初始化
        for (int i = 0; i < n + 1; i++) {
            D[i][0] = i;
        }
        for (int j = 0; j < m + 1; j++) {
            D[0][j] = j;
        }

        // 计算所有 DP 值
        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < m + 1; j++) {
                int left = D[i - 1][j] + 1;
                int down = D[i][j - 1] + 1;
                int left_down = D[i - 1][j - 1];
                if (word1.charAt(i - 1) != word2.charAt(j - 1)) {
                    left_down += 1;
                }
                D[i][j] = Math.min(left, Math.min(down, left_down));
            }
        }
        return D[n][m];
    }
}
