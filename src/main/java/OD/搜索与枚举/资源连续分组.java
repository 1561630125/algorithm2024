package OD.搜索与枚举;

/**
 * 考点：二分答案
 *
 * @author faming.yang@hand-china.com 2026-09-13 17:01
 */
public class 资源连续分组 {

    public class Solution {

        /**
         * 把 nums 分成 k 个连续子数组，使最大子数组和最小。
         *
         * @param nums 非负整数数组
         * @param k    要分成的子数组个数
         * @return 最小的"最大子数组和"
         */
        public int minimizeMaximumAllocation(int[] nums, int k) {

            // ---------- 1. 确定二分搜索的上下界 ----------
            long left = 0, right = 0;
            for (int value : nums) {
                left = Math.max(left, value);   // 下界：至少是单个最大元素
                right += value;                 // 上界：整个数组全放一组
            }
            // 二分答案的范围：[left, right]

            // ---------- 2. 二分搜索最小可行的"最大子数组和" ----------
            while (left < right) {
                long middle = (left + right) / 2;   // 猜测的"最大子数组和"上限

                // ---------- 3. 贪心验证：以 middle 为上限，最少能分几组 ----------
                long total = 0;
                int groups = 1;                     // 至少一组
                for (int value : nums) {
                    if (total + value > middle) {   // 当前组放不下 value
                        groups++;                   // 开新组
                        total = 0;
                    }
                    total += value;
                }

                // ---------- 4. 根据分组数调整二分边界 ----------
                if (groups <= k) {
                    // 能在 k 组内完成 → middle 可行，尝试更小的上限
                    right = middle;
                } else {
                    // 需要超过 k 组 → middle 太小，必须增大
                    left = middle + 1;
                }
            }

            return (int) left;   // 收敛到最小可行的"最大子数组和"
        }
    }

}
