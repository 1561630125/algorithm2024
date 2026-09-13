package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-12 16:13
 */
public class 每桶取球数 {

    class Solution {

        /**
         * 计算每个桶中需要移除的球数，使得"保留的球总数"不超过给定上限 limit，
         * 并且每个桶保留的数量尽量多（即移除的数量尽量少）。
         *
         * 核心思路：
         *   选择一个统一的"保留上限" cap，使得每个桶保留 min(cap, buckets[i]) 个球，
         *   所有桶保留数之和 <= limit。
         *   我们要找的是满足该条件的最大 cap（保留得越多，移除得越少）。
         *   然后每个桶移除的球数为 max(0, buckets[i] - cap)。
         *
         * 该问题具有单调性：
         *   cap 越大 → 保留总数 kept 越大。
         *   因此可以用二分查找来定位最大可行 cap。
         *
         * @param limit   允许保留的球总数上限
         * @param buckets 每个桶中球的初始数量
         * @return 每个桶需要移除的球数数组；若无需移除则返回空数组
         */
        long[] minimumBallRemovals(long limit, long[] buckets) {

            // ---------- 边界处理：桶数组为空 ----------
            if (buckets.length == 0) return new long[0];

            long total = 0L;   // 所有桶中球的总数
            long right = 0L;   // 二分查找的右边界：单个桶的最大球数

            // 统计总数 total，并确定 right = max(buckets)
            for (long value : buckets) {
                total += value;
                right = Math.max(right, value);
            }

            // ---------- 若总球数已经不超过 limit，则无需移除任何球 ----------
            if (total <= limit) return new long[0];

            // ---------- 二分查找最大的可行"保留上限" cap ----------
            long left = 0L;   // 二分查找的左边界：cap 最小为 0

            // 在 [left, right] 中查找最大的 cap，使得 sum(min(cap, buckets[i])) <= limit
            while (left < right) {
                // 计算中点，偏向右侧（上取整），避免死循环
                long middle = left + (right - left + 1L) / 2L;

                // 计算当保留上限为 middle 时，所有桶能保留的球总数
                long kept = 0L;
                for (long value : buckets) {
                    kept += Math.min(middle, value);  // 每个桶最多保留 middle 个
                }

                // 若保留总数未超过 limit，说明 middle 可行，尝试更大的 cap
                if (kept <= limit) {
                    left = middle;
                } else {
                    // 否则 middle 过大，减小 cap
                    right = middle - 1L;
                }
            }

            // ---------- 循环结束，left 即为最大的可行保留上限 ----------
            // 每个桶需要移除 max(0, buckets[i] - left) 个球
            long[] result = new long[buckets.length];
            for (int index = 0; index < buckets.length; index++) {
                result[index] = Math.max(0L, buckets[index] - left);
            }

            return result;
        }
    }

}
