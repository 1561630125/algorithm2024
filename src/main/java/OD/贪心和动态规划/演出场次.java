package OD.贪心和动态规划;

/**
 * 区间贪心
 *
 * @author faming.yang@hand-china.com 2026-09-15 19:04
 */
public class 演出场次 {

    long maximumPerformances(long[][] shows) {
        // write code here

        // dp【i】 前i场演出可以观看的最大场数
        int[] dp = new int[shows.length];
        return 0;
    }



    class Solution {
        /**
         * 给定若干演出 shows，每个演出为 [开始时间, 持续时间]，
         * 每个演出还额外需要一个 15 分钟的缓冲（收尾/换场），
         * 求最多能安排多少场互不冲突的演出。
         *
         * 本质：区间调度问题（Activity Selection）——贪心选最早结束的区间。
         *
         * @param shows 每个元素 = [开始时间, 持续时间]
         * @return 最多能安排的演出场数
         */
        long maximumPerformances(long[][] shows) {
            int n = shows.length;

            // intervals[i] = [开始时间, 结束时间]
            // 结束时间 = 开始时间 + 持续时间 + 15（缓冲）
            long[][] intervals = new long[n][2];
            for (int i = 0; i < n; i++) {
                intervals[i][0] = shows[i][0];                          // 开始
                intervals[i][1] = shows[i][0] + shows[i][1] + 15;       // 结束（含 15 分钟缓冲）
            }

            // 贪心关键：按"结束时间"升序排序
            // 若结束时间相同，再按开始时间升序（次要排序，保证稳定性/确定性）
            java.util.Arrays.sort(intervals, (a, b) -> {
                int result = Long.compare(a[1], b[1]);   // 先比结束时间
                return result != 0 ? result : Long.compare(a[0], b[0]);   // 相同则比开始时间
            });

            long lastEnd = 0;     // 上一场选中演出的结束时间
            long answer = 0;      // 已选演出数量

            // 按结束时间从早到晚遍历，能接上就选
            for (long[] interval : intervals) {
                if (interval[0] >= lastEnd) {   // 当前演出开始 >= 上一场结束 → 不冲突
                    answer++;                    // 选中这场
                    lastEnd = interval[1];       // 更新"最后结束时间"
                }
                // 否则跳过（与已选演出冲突）
            }

            return answer;
        }
    }



    class Solution2 {
        long maximumPerformances(long[][] shows) {
            int n = shows.length;
            if (n == 0) return 0;

            // 构造区间 [开始, 结束（含 15 缓冲）]
            long[][] intervals = new long[n][2];
            for (int i = 0; i < n; i++) {
                intervals[i][0] = shows[i][0];
                intervals[i][1] = shows[i][0] + shows[i][1] + 15;
            }

            // 按结束时间升序排序
            java.util.Arrays.sort(intervals, (a, b) -> {
                int r = Long.compare(a[1], b[1]);
                return r != 0 ? r : Long.compare(a[0], b[0]);
            });

            // dp[i]：前 i 个区间（1-based）中最多能选几个
            int[] dp = new int[n + 1];

            for (int i = 1; i <= n; i++) {
                // 不选第 i 个
                int skip = dp[i - 1];

                // 选第 i 个：找最后一个"结束时间 <= 第 i 个开始时间"的区间
                int p = findLastCompatible(intervals, i - 1);   // 0-based 下标

                // dp[p+1] 表示"前 p+1 个区间"的最优值（因为 dp 是 1-based）
                int take = dp[p + 1] + 1;

                dp[i] = Math.max(skip, take);
            }

            return dp[n];
        }

        // 在 intervals[0..idx-1] 中，二分找最后一个 end <= intervals[idx].start 的下标
        // 返回 -1 表示没有兼容的
        private int findLastCompatible(long[][] intervals, int idx) {
            long start = intervals[idx][0];
            int lo = 0, hi = idx - 1, ans = -1;
            while (lo <= hi) {
                int mid = lo + (hi - lo) / 2;
                if (intervals[mid][1] <= start) {
                    ans = mid;      // 这个兼容，尝试找更靠右的
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            }
            return ans;
        }
    }
}
