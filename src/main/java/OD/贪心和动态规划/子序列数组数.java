package OD.贪心和动态规划;

/**
 * 考点：贪心 + dp
 *
 * @author faming.yang@hand-china.com 2026-09-03 17:22
 */
public class 子序列数组数 {
    static class Solution {
        /**
         * 计算 source 中最多能组成多少个完整的 target 子序列（不要求连续）
         * <p>
         * 核心思想：贪心 + 状态计数
         * 从左到右扫描 source，用 counts[i] 表示当前已经匹配到 target 第 i 个字符的"半成品"数量。
         * 每当遇到可以推进某个匹配进度的字符时，就尽量把已有的半成品向前推进一步。
         */
        // abccd abc
        int maxSubsequenceCopies(String source, String target) {
            // 空 target 视为可以组成无限个（返回 0 是边界约定）
            if (target.isEmpty())
                return 0;

            // 记录 target 中每个字符出现的位置（若 target 有重复字符，只保留最后一次出现的位置）
            java.util.Map<Character, Integer> positions = new java.util.HashMap<>();
            for (int index = 0; index < target.length(); index++)
                positions.put(target.charAt(index), index);

            // counts[i]：已经成功匹配到 target 前 i+1 个字符（即匹配到下标 i）的序列个数
            // 例如 counts[0] 表示已经匹配了 target[0] 的序列数
            // 最终 counts[length-1] 即为完整匹配 target 的序列数
            int[] counts = new int[target.length()];

            // 遍历 source 的每个字符
            for (int index = 0; index < source.length(); index++) {
                Integer position = positions.get(source.charAt(index));
                // 该字符不在 target 中，直接跳过
                if (position == null)
                    continue;

                // 情况 1：该字符匹配 target 的第一个字符
                // 直接开启一条新的匹配序列
                if (position == 0)
                    counts[0]++;
                    // 情况 2：该字符匹配 target 中某个非首位字符
                    // 若前面已经有匹配到 position-1 的序列，就把其中一个推进到 position
                else if (counts[position - 1] > 0) {
                    counts[position - 1]--;   // 消耗一个"半成品"
                    counts[position]++;       // 生成一个进度更深的"半成品"
                }
            }


            /*int[] dp = new int[m + 1];   // dp[j] = 匹配到 target 前 j 位的序列数
            for (char ch : source) {
                for (int j = m - 1; j >= 0; j--) {
                    if (ch == target.charAt(j)) {
                        if (j == 0) dp[1]++;
                        else if (dp[j] > 0) { dp[j]--; dp[j + 1]++; }
                    }
                }
            }
            return dp[m];*/


            // 匹配到 target 最后一个字符的序列数，就是能组成的完整 target 个数
            return counts[counts.length - 1];
        }
    }


    static class Solution2 {
        int maxSubsequenceCopies(String source, String target) {
            if (target.isEmpty())
                return 0;
            int m = target.length();
            int[] dp = new int[m + 1];   // dp[j] = 匹配到 target 前 j 位的序列数

            for (char ch : source.toCharArray()) {
                // 逆序：保证同一个 source 字符只被用一次
                for (int j = m - 1; j >= 0; j--) {
                    if (ch == target.charAt(j)) {
                        if (j == 0) {
                            dp[1]++;                 // 开启一条新序列
                        } else if (dp[j] > 0) {
                            dp[j]--;                 // 消耗一个"半成品"
                            dp[j + 1]++;             // 推进一格
                        }
                    }
                }
            }
            return dp[m];
        }
    }


    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.maxSubsequenceCopies("aabcbc", "abcc"));

        Solution2 solution2 = new Solution2();
        System.out.println(solution2.maxSubsequenceCopies("aabcbc", "abcc"));
    }
}
