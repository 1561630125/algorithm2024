package 力扣.一维动态规划.A300;

/**
 * A2407,TODO:线段树
 *
 * @author faming.yang@hand-china.com 2024-10-29 15:06
 */
public class 最长递增子序列2 {

    class Solution {
        int[] max;

        public int lengthOfLIS(int[] nums, int k) {
            var u = 0;
            for (var x : nums) u = Math.max(u, x);
            max = new int[u * 4];
            for (var x : nums) {
                if (x == 1) modify(1, 1, u, 1, 1);
                else {
                    var res = 1 + query(1, 1, u, Math.max(x - k, 1), x - 1);
                    modify(1, 1, u, x, res);
                }
            }
            return max[1];
        }

        private void modify(int o, int l, int r, int idx, int val) {
            if (l == r) {
                max[o] = val;
                return;
            }
            var m = (l + r) / 2;
            if (idx <= m) modify(o * 2, l, m, idx, val);
            else modify(o * 2 + 1, m + 1, r, idx, val);
            max[o] = Math.max(max[o * 2], max[o * 2 + 1]);
        }

        // 返回区间 [L,R] 内的最大值
        private int query(int o, int l, int r, int L, int R) { // L 和 R 在整个递归过程中均不变，将其大写，视作常量
            if (L <= l && r <= R) return max[o];
            var res = 0;
            var m = (l + r) / 2;
            if (L <= m) res = query(o * 2, l, m, L, R);
            if (R > m) res = Math.max(res, query(o * 2 + 1, m + 1, r, L, R));
            return res;
        }
    }


    class Solution2 {
        // 线段树数组，max[o] 表示节点 o 管辖区间内的最大值
        // 大小开 4 * u，是线段树的标准做法，防止越界
        int[] max;

        public int lengthOfLIS(int[] nums, int k) {
            // ---------- 第 1 步：求数值上界 u ----------
            // 线段树要维护区间 [1, u]，u 是所有数的最大值
            var u = 0;
            for (var x : nums) u = Math.max(u, x);

            // ---------- 第 2 步：建树 ----------
            // 初始全 0，表示所有 dp[x] = 0
            max = new int[u * 4];

            // ---------- 第 3 步：逐个处理 nums 里的数 ----------
            for (var x : nums) {
                if (x == 1) {
                    // 特殊情况：x == 1
                    // 没有比 1 更小的数（数值从 1 开始）
                    // 所以以 1 结尾的 LIS 长度只能是 1
                    // 直接更新 dp[1] = 1
                    modify(1, 1, u, 1, 1);
                } else {
                    // 一般情况：x > 1
                    // 查询区间 [max(x-k, 1), x-1] 内的最大 dp 值
                    // 左端点不能小于 1，所以用 Math.max(x-k, 1)
                    // 右端点是 x-1，保证严格递增（不含 x 自己）
                    var res = 1 + query(1, 1, u, Math.max(x - k, 1), x - 1);

                    // 更新 dp[x] = res
                    modify(1, 1, u, x, res);
                }
            }

            // ---------- 第 4 步：返回答案 ----------
            // max[1] 是线段树根节点，维护整个 [1, u] 的最大值
            // 也就是所有 dp[x] 的最大值
            return max[1];
        }

        /**
         * 单点修改：把位置 idx 的值改为 val
         *
         * @param o   当前节点编号
         * @param l   当前节点管辖区间的左端点
         * @param r   当前节点管辖区间的右端点
         * @param idx 要修改的位置
         * @param val 新值
         */
        private void modify(int o, int l, int r, int idx, int val) {
            // 递归到叶子节点，直接赋值
            if (l == r) {
                max[o] = val;
                return;
            }

            // 取中点，判断 idx 在左半边还是右半边
            var m = (l + r) / 2;
            if (idx <= m)
                modify(o * 2, l, m, idx, val);          // 递归左子节点
            else
                modify(o * 2 + 1, m + 1, r, idx, val);  // 递归右子节点

            // 回溯时更新父节点：取两个子节点的较大值
            max[o] = Math.max(max[o * 2], max[o * 2 + 1]);
        }

        /**
         * 区间查询：返回区间 [L, R] 内的最大值
         *
         * @param o 当前节点编号
         * @param l 当前节点管辖区间的左端点
         * @param r 当前节点管辖区间的右端点
         * @param L 查询区间的左端点（递归过程中不变）
         * @param R 查询区间的右端点（递归过程中不变）
         */
        private int query(int o, int l, int r, int L, int R) {
            // 当前节点区间完全被查询区间包含，直接返回
            if (L <= l && r <= R) return max[o];

            // res 初始为 0，因为 dp 值非负，0 是合法下界
            var res = 0;
            var m = (l + r) / 2;

            // 左子节点区间 [l, m] 与查询区间有交集，就查左边
            if (L <= m)
                res = query(o * 2, l, m, L, R);

            // 右子节点区间 [m+1, r] 与查询区间有交集，就查右边
            // 取两边较大值
            if (R > m)
                res = Math.max(res, query(o * 2 + 1, m + 1, r, L, R));

            return res;
        }
    }

}
