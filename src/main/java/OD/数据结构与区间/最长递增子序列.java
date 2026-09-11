package OD.数据结构与区间;

import java.util.ArrayList;
import java.util.List;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 22:20
 */
public class 最长递增子序列 {

    // 记忆化搜索
    class Solution {
        public int lengthOfLIS(int[] nums) {
            int n = nums.length;

            // memo[i]：以 nums[i] 结尾的最长递增子序列长度
            // 初始 0 表示没计算过（因为长度至少为 1，0 不可能是有效结果）
            int[] memo = new int[n];

            int ans = 0;

            // 枚举每个位置作为结尾，取最大值
            for (int i = 0; i < n; i++) {
                ans = Math.max(ans, dfs(i, nums, memo));
            }

            return ans;
        }

        /**
         * 求以 nums[i] 结尾的最长递增子序列长度
         *
         * @param i    当前结尾位置
         * @param nums 原数组
         * @param memo 记忆化数组
         */
        private int dfs(int i, int[] nums, int[] memo) {

            // 之前算过，直接返回
            if (memo[i] > 0) {
                return memo[i];
            }

            int res = 0;

            // 枚举 i 之前的所有 j，看能否接在 j 后面
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    // nums[i] 可以接在 nums[j] 结尾的序列后面
                    // 取所有能接的 j 里，dfs(j) 的最大值
                    res = Math.max(res, dfs(j, nums, memo));
                }
            }

            // 循环结束后 res 是「能接的最长前缀长度」
            // 加上自己，长度 +1
            res++;

            // 记忆化并返回
            return memo[i] = res;
        }
    }

    //递推
    class Solution2 {
        public int lengthOfLIS(int[] nums) {
            int n = nums.length, ans = 0;

            // f[i]：以 nums[i] 结尾的最长递增子序列长度
            int[] f = new int[n];

            for (int i = 0; i < n; i++) {

                // 枚举 i 之前的所有位置 j，看能不能接在 j 后面
                for (int j = 0; j < i; j++) {
                    if (nums[j] < nums[i]) {
                        // nums[i] 可以接在 nums[j] 后面
                        // 取所有能接的 j 里 f[j] 最大的
                        f[i] = Math.max(f[i], f[j]);
                    }
                }

                // 接完后，自己算一个，所以 f[i] 最终 = max(f[j]) + 1
                // 用 ++f[i] 同时完成「+1」和「更新 ans」
                ans = Math.max(ans, ++f[i]);
            }

            return ans;
        }
    }


    // 贪心 + 二分查找
    class Solution3 {
        public int lengthOfLIS(int[] nums) {
            List<Integer> g = new ArrayList<>();
            for (int x : nums) {
                int j = lowerBound(g, x);
                if (j == g.size()) {
                    g.add(x); // >=x 的 g[j] 不存在
                } else {
                    g.set(j, x);
                }
            }
            return g.size();
        }

        // 开区间写法
        private int lowerBound(List<Integer> g, int target) {
            int left = -1, right = g.size(); // 开区间 (left, right)
            while (left + 1 < right) { // 区间不为空
                // 循环不变量：
                // nums[left] < target
                // nums[right] >= target
                int mid = left + (right - left) / 2;
                if (g.get(mid) < target) {
                    left = mid; // 范围缩小到 (mid, right)
                } else {
                    right = mid; // 范围缩小到 (left, mid)
                }
            }
            return right; // 或者 left+1
        }
    }


}
