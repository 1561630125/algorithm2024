package OD.搜索与枚举;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 19:30
 */
public class 分隔等和子集 {

    // 记忆化搜索
    class Solution {
        public boolean canPartition(int[] nums) {
            int s = 0;
            for (int x : nums) {
                s += x;
            }
            if (s % 2 != 0) {
                return false;
            }

            int n = nums.length;
            int[][] memo = new int[n][s / 2 + 1];
            for (int[] row : memo) {
                Arrays.fill(row, -1); // -1 表示没有计算过
            }

            return dfs(n - 1, s / 2, nums, memo);
        }

        private boolean dfs(int i, int j, int[] nums, int[][] memo) {
            if (i < 0) {
                return j == 0;
            }
            if (memo[i][j] != -1) { // 之前计算过
                return memo[i][j] == 1;
            }

            boolean res;
            if (j < nums[i]) {
                res = dfs(i - 1, j, nums, memo); // 只能不选
            } else {
                res = dfs(i - 1, j - nums[i], nums, memo) || dfs(i - 1, j, nums, memo); // 选或不选
            }
            memo[i][j] = res ? 1 : 0; // 记忆化
            return res;
        }
    }

    class Solution2 {
        public boolean canPartition(int[] nums) {
            int s = 0;
            for (int x : nums) {
                s += x;
            }
            if (s % 2 != 0) {
                return false;
            }

            int n = nums.length;
            int[][] memo = new int[n][s / 2 + 1];
            for (int[] row : memo) {
                Arrays.fill(row, -1); // -1 表示没有计算过
            }

            return dfs(n - 1, s / 2, nums, memo);
        }

        private boolean dfs(int i, int j, int[] nums, int[][] memo) {
            if (i < 0) {
                return j == 0;
            }
            if (memo[i][j] != -1) { // 之前计算过
                return memo[i][j] == 1;
            }

            boolean res = j >= nums[i] && dfs(i - 1, j - nums[i], nums, memo) || dfs(i - 1, j, nums, memo);
            memo[i][j] = res ? 1 : 0; // 记忆化
            return res;
        }
    }

    // f[i][j] 的定义和 dfs(i,j) 的定义是一样的，都表示能否从 nums[0] 到 nums[i] 中选出一个和恰好等于 j 的子序列
    class Solution3 {
        public boolean canPartition(int[] nums) {
            int s = 0;
            for (int x : nums) {
                s += x;
            }
            if (s % 2 != 0) {
                return false;
            }
            s /= 2; // 注意这里把 s 减半了

            int n = nums.length;
            boolean[][] f = new boolean[n + 1][s + 1];
            f[0][0] = true;
            for (int i = 0; i < n; i++) {
                int x = nums[i];
                for (int j = 0; j <= s; j++) {
                    f[i + 1][j] = j >= x && f[i][j - x] || f[i][j];
                }
            }
            return f[n][s];
        }
    }


    class Solution4 {
        public boolean canPartition(int[] nums) {
            // 1. 求总和
            int s = 0;
            for (int x : nums) {
                s += x;
            }

            // 2. 总和必须是偶数，否则无法分成两个等和子集
            if (s % 2 != 0) {
                return false;
            }

            // 3. 目标和：只需从 nums 中选出一些数，使其和等于 s/2
            s /= 2;

            int n = nums.length;

            // f[i][j]：从前 i 个数中选，能否凑出和恰好为 j
            boolean[][] f = new boolean[n + 1][s + 1];

            // 边界：从前 0 个数中选，能凑出和 0（什么都不选）
            f[0][0] = true;

            for (int i = 0; i < n; i++) {
                int x = nums[i];

                for (int j = 0; j <= s; j++) {

                    // f[i+1][j]：考虑前 i+1 个数（下标 0..i），能否凑出 j
                    // 两种情况：
                    //   1. 选 nums[i]：需要 j >= x，且前 i 个数能凑出 j - x
                    //   2. 不选 nums[i]：前 i 个数能凑出 j
                    f[i + 1][j] = (j >= x && f[i][j - x]) || f[i][j];
                }
            }

            // 从前 n 个数中选，能否凑出 s（即总和一半）
            return f[n][s];
        }
    }


}
