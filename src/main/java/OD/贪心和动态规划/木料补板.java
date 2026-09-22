package OD.贪心和动态规划;

/**
 * 考点：贪心 + 排序
 *
 * @author faming.yang@hand-china.com 2026-09-15 16:44
 */
public class 木料补板 {

    class Solution {
        /**
         * 给定一组木板长度 boards 和额外木料 extraWood，
         * 求在"使所有木板长度尽可能相等且最大化最短木板长度"的目标下，
         * 能达到的最终最短木板长度（即最大化最小值 / 二分答案思想的水位法）。
         * <p>
         * 思路（类似"接雨水"水位上升法）：
         * 1. 把木板按长度升序排序；
         * 2. 用 extraWood 把最短的一批木板"垫高"到下一档长度；
         * 3. 若木料不足以垫到下一档，就平均分配后返回；
         * 4. 若所有木板都垫平后还有剩余，则均分给全部木板。
         *
         * @param boards    每块木板的初始长度
         * @param extraWood 可额外补充的木料总量
         * @return 最终能达到的最大"最短木板长度"
         */
        long maximumMinimumBoardLength(long[] boards, long extraWood) {
            // 边界情况：没有木板，直接返回 0
            if (boards.length == 0)
                return 0;

            // 拷贝一份并排序（不破坏原数组），升序排列便于从最短木板开始垫高
            long[] values = boards.clone();
            java.util.Arrays.sort(values);

            // current 表示当前"水位"（已被垫平的木板统一达到的长度）
            // 初始为最短木板长度
            long current = values[0];

            // index 表示当前已经"垫平"到 current 高度的木板数量
            // 初始先统计与最短木板等长的木板个数
            int index = 1;
            while (index < values.length && values[index] == current)
                index++;

            // extraWood 为负属于非法输入（木料不能为负），
            // 此时直接按当前水位扣减后返回（相当于"削木板"）
            if (extraWood < 0)
                return current + Math.floorDiv(extraWood, index);

            // 逐档把已垫平的 index 块木板提升到下一块更高的木板高度
            while (index < values.length) {
                long target = values[index];                      // 下一档目标高度
                long cost = (target - current) * index;           // 把 index 块木板都垫到 target 所需木料

                // 木料不够垫到下一档：只能把剩余木料平均分给这 index 块木板
                if (cost > extraWood)
                    return current + extraWood / index;

                // 木料够：垫到 target，更新水位和剩余木料
                extraWood -= cost;
                current = target;

                // 把与 target 等高的木板也纳入"已垫平"集合
                while (index < values.length && values[index] == current)
                    index++;
            }

            // 所有木板都已被垫平（index == values.length），
            // 剩余木料平均分给全部木板，得到最终统一高度
            return current + extraWood / values.length;
        }
    }


    class Solution2 {
        public long maximumMinimumBoardLength(long[] boards, long extraWood) {
            if (boards.length == 0) return 0;

            long lo = Long.MAX_VALUE;
            for (long b : boards) lo = Math.min(lo, b);
            long hi = lo + extraWood;

            while (lo < hi) {
                long mid = lo + (hi - lo + 1) / 2;
                if (canMake(boards, extraWood, mid)) {
                    lo = mid;
                } else {
                    hi = mid - 1;
                }
            }
            return lo;
        }

        private boolean canMake(long[] boards, long extraWood, long L) {
            long cost = 0;
            for (long b : boards) {
                if (b < L) {
                    cost += L - b;
                    if (cost > extraWood) return false;
                }
            }
            return cost <= extraWood;
        }
    }

}
