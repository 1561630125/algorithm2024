package OD.搜索与枚举;

/**
 * 考点：二分答案
 *
 * @author faming.yang@hand-china.com 2026-09-12 17:47
 */
public class 最慢吃桃速度 {


    class Solution {

        /**
         * 求"吃桃子的最小速度"：每小时吃 k 个，要求在 hours 小时内吃完所有 piles。
         *
         * 规则：
         *   - 每小时只能选一堆吃，吃 k 个（不足 k 则吃完这堆为止）。
         *   - 同一堆吃完后才能吃下一堆。
         *   - 求满足条件的最小 k。
         *
         * 思路：二分答案。
         *   "速度为 k 能否在 hours 内吃完" 具有单调性：
         *     k 越大越容易吃完 → 若 k 可行，则 k+1 也可行。
         *   因此在 [1, max(piles)] 中二分最小的可行 k。
         *
         * @param piles 每堆桃子的数量
         * @param hours 允许的小时数
         * @return 最小可行速度；输入非法返回 0
         */
        long minimumPeachEatingSpeed(long[] piles, long hours) {

            // ---------- 1. 输入合法性校验 ----------
            // hours <= 0：没有可用时间
            // piles 为空：没有桃子可吃（按题目约定返回 0）
            // piles.length > hours：即使每堆只花 1 小时也吃不完
            if (hours <= 0 || piles.length == 0 || piles.length > hours)
                return 0;

            // ---------- 2. 找出最大堆，作为二分上界 ----------
            long maximum = 0;
            for (long pile : piles) {
                if (pile < 0)
                    return 0;               // 出现负数，非法输入
                maximum = Math.max(maximum, pile);
            }

            // 若小时数恰好等于堆数 → 每堆只需 1 小时吃完
            // 速度只要达到最大堆的大小即可（这样一堆最多 1 小时）
            if (piles.length == hours)
                return maximum;

            // 所有堆都为 0 → 无需吃
            if (maximum == 0)
                return 0;

            // ---------- 3. 二分答案：找最小的可行速度 ----------
            // 速度下界为 1（不能为 0，否则除零）
            // 速度上界为 maximum（每小时吃最大堆那么多，最多也只需 piles.length 小时）
            long left = 1, right = maximum;

            while (left < right) {
                // 下取整中点
                long middle = left + (right - left) / 2;

                // 计算以速度 middle 吃完所有堆需要的总小时数
                long used = 0;
                for (long pile : piles) {
                    // 向上取整: ceil(pile / middle)
                    //   pile / middle           → 向下取整
                    //   + (pile % middle == 0 ? 0 : 1) → 不能整除时补 1
                    used += pile / middle + (pile % middle == 0 ? 0 : 1);

                    // 小优化：一旦超过 hours，无需继续算，直接判定不可行
                    if (used > hours)
                        break;
                }

                if (used <= hours) {
                    // middle 可行 → 尝试更小的速度
                    right = middle;
                } else {
                    // middle 不可行 → 必须更大
                    left = middle + 1;
                }
            }

            // 循环结束 left == right，即最小可行速度
            return left;
        }
    }


}
