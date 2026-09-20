package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 12:26
 */
public class 升级窗口 {
    class Solution {

        /**
         * 在一个【环形数组】上，找出一段"连续子数组"（可跨过数组末尾回到开头），
         * 使得：
         * 1. 元素之和 <= tolerance；
         * 2. 长度尽可能长；
         * 3. 长度相同时，起始下标尽可能小。
         * <p>
         * 返回 {起始下标, 结束下标}；若无解返回 {-1, -1}。
         *
         * @param tolerance 允许的元素和上限
         * @param visits    环形数组
         * @return {bestStart, bestEnd} 或 {-1, -1}
         */
        int[] bestUpgradeWindow(long tolerance, long[] visits) {

            int count = visits.length;
            int bestLength = 0;     // 当前最优长度
            int bestStart = -1;     // 当前最优起始下标

            // ---------- 枚举每个起点 ----------
            for (int start = 0; start < count; start++) {

                long total = 0;     // 当前窗口的和

                // ---------- 从该起点出发，逐步加长窗口 ----------
                for (int length = 1; length <= count; length++) {

                    // 环形取下标：超过 count 回到 0
                    total += visits[(start + length - 1) % count];

                    // ---------- 判断是否更新最优 ----------
                    if (total <= tolerance
                            && (length > bestLength
                            || (length == bestLength
                            && (bestStart < 0 || start < bestStart)))) {

                        bestLength = length;
                        bestStart = start;
                    }
                }
            }

            // ---------- 返回结果 ----------
            return bestLength == 0
                    ? new int[]{-1, -1}
                    : new int[]{bestStart, (bestStart + bestLength - 1) % count};
        }
    }
}
