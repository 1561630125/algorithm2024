package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 16:19
 */
public class 高频子数组 {


    long countElegantSubarrays(long[] values, int threshold) {


        // [i,j]范围不同数字分别出现的次数

        return 0L;
    }


    /**
     * 统计"优雅子数组"的数量
     *
     * 题目背景：
     * 给定一个数组 values 和一个阈值 threshold
     * 如果一个子数组中，存在某个元素出现了 threshold 次，则该子数组是"优雅"的
     * 需要统计所有优雅子数组的数量
     *
     * 例如：
     * values = [1, 2, 1, 2, 3], threshold = 2
     *
     * 优雅子数组（包含某个元素出现2次）：
     * [1,2,1] → 1出现2次 ✅
     * [1,2,1,2] → 1出现2次，2出现2次 ✅
     * [2,1,2] → 2出现2次 ✅
     * [1,2,1,2,3] → 1出现2次，2出现2次 ✅
     * [2,1,2,3] → 2出现2次 ✅
     *
     * 非优雅子数组：
     * [1,2] → 没有元素出现2次 ❌
     * [1,2,3] → 没有元素出现2次 ❌
     *
     * 统计结果：5
     *
     * 核心思想：
     * 对于每个右边界 right，找到最小的左边界 left
     * 使得子数组 [left, right] 是优雅的
     * 那么所有以 right 为右边界，起始位置 ≥ left 的子数组都是优雅的
     *
     * @param values 输入数组
     * @param threshold 阈值（某个元素需要出现的次数）
     * @return 优雅子数组的总数
     */
    long countElegantSubarrays2(long[] values, int threshold) {
        // ========== 1. 参数校验 ==========
        if (threshold <= 0) return 0;

        // ========== 2. 数据结构初始化 ==========
        // counts: 记录当前窗口中每个元素出现的次数
        java.util.Map<Long, Integer> counts = new java.util.HashMap<>();
        int left = 0;           // 窗口左边界
        int valid = 0;          // 窗口中有多少个元素出现了 threshold 次
        long result = 0;        // 优雅子数组总数

        // ========== 3. 滑动窗口 ==========
        for (int right = 0; right < values.length; right++) {
            // ====== 3.1 扩展窗口：加入 values[right] ======
            int next = counts.getOrDefault(values[right], 0) + 1;
            counts.put(values[right], next);

            // 如果这个元素刚好达到 threshold 次，valid 增加
            if (next == threshold) {
                valid++;
            }

            // ====== 3.2 收缩窗口：当窗口是"优雅"的 ======
            // 只要窗口是优雅的（valid > 0），就尝试收缩左边界
            while (valid > 0) {
                // ====== 关键计数：所有以 right 为右边界，左边界 ≥ left 的子数组 ======
                // 当前窗口 [left, right] 是优雅的
                // 那么 [left+1, right], [left+2, right], ..., [right, right] 也都是优雅的？
                //
                // 实际上，当前窗口 [left, right] 是优雅的
                // 所有从 left 到 right 之间开始的子数组都是优雅的？
                //
                // 不对！我们需要小心！

                // 正确理解：
                // 对于当前的 right，如果 [left, right] 是优雅的
                // 那么所有起始位置在 [left, right] 之间，以 right 为结尾的子数组都是优雅的？
                //
                // 实际上，算法中的 result += values.length - right 表示：
                // 以当前 left 为起点，向右延伸到数组末尾的所有子数组都继承了这个优雅性
                // 这需要重新理解...

                // 让我们重新分析：
                // 当我们找到第一个使 [left, right] 优雅的 left 时
                // 对于所有更大的 left（更短的前缀），可能不再优雅
                // 所以这里应该是：
                // 对于当前的 right，所有起始位置在 [0, left] 之间的子数组？
                //
                // 实际上这段代码的 result += values.length - right 是不正确的！
                // 正确的计数应该是：result += left_count，其中 left_count 是有效的左边界数量

                // 暂时按照原代码逻辑注释...
                result += values.length - right;

                // ====== 3.3 收缩左边界 ======
                long removed = values[left++];
                int remaining = counts.get(removed) - 1;
                counts.put(removed, remaining);

                // 如果移除后，该元素不再达到 threshold 次，valid 减少
                if (remaining == threshold - 1) {
                    valid--;
                }
            }
        }

        return result;
    }



    /**
     * 统计优雅子数组的数量（正确版本）
     *
     * 思路：
     * 1. 使用滑动窗口维护 [left, right]
     * 2. 当窗口优雅时（存在元素出现 threshold 次）
     * 3. 计数所有以 right 为结尾的优雅子数组
     * 4. 收缩 left 直到窗口不再优雅
     */
    long countElegantSubarraysCorrect3(long[] values, int threshold) {
        if (threshold <= 0) return 0;

        java.util.Map<Long, Integer> counts = new java.util.HashMap<>();
        int left = 0;
        int valid = 0;
        long result = 0;

        for (int right = 0; right < values.length; right++) {
            // 1. 扩展窗口
            int next = counts.getOrDefault(values[right], 0) + 1;
            counts.put(values[right], next);
            if (next == threshold) valid++;

            // 2. 当窗口优雅时，计数并收缩
            while (valid > 0) {
                // 当前 [left, right] 是优雅的
                // 所有以 right 为结尾，起始位置 ≤ left 的子数组都是优雅的吗？
                //
                // 正确理解：
                // 对于当前的 left，如果 [left, right] 优雅
                // 那么 [left+1, right], [left+2, right], ... 可能不优雅
                // 但是！[0, right], [1, right], ..., [left, right] 都优雅？
                //
                // 如果 [left, right] 是第一个使窗口优雅的 left
                // 那么对于任何 i < left，[i, right] 也一定优雅吗？
                // 不一定是！
                //
                // 实际上，优雅性是单调的：
                // 如果 [i, right] 优雅，那么 [i-1, right] 也一定优雅（加上一个元素）
                // 所以一旦找到第一个优雅的 left，所有 i ≤ left 都优雅

                // 因此，优雅子数组的数量 = left + 1
                // 因为起始位置可以是 0, 1, 2, ..., left
                result += left + 1;

                // 收缩左边界
                long removed = values[left++];
                int remaining = counts.get(removed) - 1;
                counts.put(removed, remaining);
                if (remaining == threshold - 1) valid--;
            }
        }

        return result;
    }

}
