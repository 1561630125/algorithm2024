package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 16:18
 */
public class 两行最长公共片段 {

    class Solution {
        /**
         * 求两个字符串的最长公共子串（Longest Common Substring）
         * 注意：是"子串"（连续），不是"子序列"（可不连续）
         *
         * @param text1 第一个字符串
         * @param text2 第二个字符串
         * @return text1 与 text2 的最长公共子串
         */
        String longestCommonCode(String text1, String text2) {
            // dp[j] 表示：以 text1 当前位置字符结尾、以 text2 第 j 个字符结尾的
            // 公共子串长度（滚动数组优化，只保留一维）
            int[] dp = new int[text2.length() + 1];

            // best 记录目前为止找到的最长公共子串长度
            // end  记录该最长公共子串在 text1 中的结束下标（开区间右端点）
            int best = 0, end = 0;

            // 枚举 text1 的每个字符（i 从 1 开始，方便与 dp 下标对应）
            for (int i = 1; i <= text1.length(); i++) {
                // previous 保存 dp[j-1] 在上一轮（i-1 行）的值，
                // 也就是二维 DP 中 dp[i-1][j-1] 的值
                int previous = 0;

                // 枚举 text2 的每个字符
                for (int j = 1; j <= text2.length(); j++) {
                    // 先把本行即将被覆盖的 dp[j]（即上一行 dp[i-1][j]）暂存起来，
                    // 它会在下一列 j+1 时作为 "previous" 使用
                    int old = dp[j];

                    // 状态转移：
                    // 若两字符相等，则继承左上角 dp[i-1][j-1] + 1（即 previous + 1）
                    // 否则，子串必须连续，一旦断开就归零
                    dp[j] = text1.charAt(i - 1) == text2.charAt(j - 1) ? previous + 1 : 0;

                    // 更新 previous 为当前列在本轮开始前的旧值（即 dp[i-1][j]）
                    previous = old;

                    // 若找到更长的公共子串，记录长度和它在 text1 中的结束位置
                    if (dp[j] > best) {
                        best = dp[j];
                        end = i;   // i 是 1-based，正好等于子串末尾在 text1 中的"开区间"位置
                    }
                }
            }

            // 在 text1 中截取 [end - best, end) 即为最长公共子串
            return text1.substring(end - best, end);
        }
    }


    class Solution2 {
        /**
         * 求两个字符串的最长公共子串（Longest Common Substring，连续）
         * 使用二维 DP 数组实现，便于理解状态转移
         *
         * @param text1 第一个字符串
         * @param text2 第二个字符串
         * @return text1 与 text2 的最长公共子串
         */
        String longestCommonCode(String text1, String text2) {
            int m = text1.length();
            int n = text2.length();

            // dp[i][j] 表示：以 text1 第 i 个字符、text2 第 j 个字符
            // 结尾的最长公共子串长度（1-based，dp[0][*] 和 dp[*][0] 为 0）
            int[][] dp = new int[m + 1][n + 1];

            // best 记录目前为止找到的最长公共子串长度
            // end  记录该最长公共子串在 text1 中的结束下标（1-based，开区间右端点）
            int best = 0, end = 0;

            // 枚举 text1 的每个字符
            for (int i = 1; i <= m; i++) {
                // 枚举 text2 的每个字符
                for (int j = 1; j <= n; j++) {
                    if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                        // 字符相等：继承左上角 + 1（延续公共子串）
                        dp[i][j] = dp[i - 1][j - 1] + 1;

                        // 更新最长公共子串
                        if (dp[i][j] > best) {
                            best = dp[i][j];
                            end = i;   // 记录在 text1 中的结束位置
                        }
                    } else {
                        // 字符不等：子串必须连续，断开归零
                        dp[i][j] = 0;
                    }
                }
            }

            // 在 text1 中截取 [end - best, end) 即为最长公共子串
            return text1.substring(end - best, end);
        }
    }

}
