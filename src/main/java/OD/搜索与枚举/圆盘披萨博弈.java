package OD.搜索与枚举;

/**
 * 考点：递归+环形区间博弈
 *
 * @author faming.yang@hand-china.com 2026-09-20 15:28
 */
public class 圆盘披萨博弈 {

    public long maximumPizzaShare(long[] slices) {
        // write code here

        //5 9 3 11 6 8

        return 0L;
    }


    class Solution {
        // slices 存储环形披萨每块的大小
        private long[] slices;
        // memo[left][right] 记忆化：当剩余区间为 [left, right]（环形）时，
        // 当前行动者能获得的最大披萨总量
        private long[][] memo;
        // seen[left][right] 标记该区间是否已经计算过
        private boolean[][] seen;
        // 披萨块数
        private int count;

        /**
         * 计算"先手"能获得的最大披萨总量
         * 规则：环形排列，每次只能取当前剩余区间的左端或右端，
         *      两人交替取，都采取最优策略（零和博弈）
         */
        long maximumPizzaShare(long[] slices) {
            count = slices.length;

            // 边界检查：
            // count < 3     ：少于 3 块无意义（无法构成博弈）
            // count 为偶数  ：环形博弈中先手必败或平局，返回 0
            // count >= 500  ：超过记忆化数组的合理规模，返回 0
            if (count < 3 || count % 2 == 0 || count >= 500)
                return 0L;

            this.slices = slices;

            // 记忆化数组：memo[left][right] 表示区间 [left, right] 的最优解
            memo = new long[count][count];
            seen = new boolean[count][count];

            long result = 0L;

            // 枚举先手第一次取哪一块：
            // 因为是环形，先手可以取任意一块作为"起点"
            // 取完后，剩余区间变成 (previous(first), next(first)) 的开区间
            // 此时轮到对手行动，所以调用 gainAfterOpponent 计算"对手行动后，
            // 先手还能获得多少"
            for (int first = 0; first < count; first++)
                result = Math.max(
                        result,
                        slices[first] + gainAfterOpponent(previous(first), next(first))
                );

            return result;
        }

        // 环形索引：前一个位置
        private int previous(int index) {
            return (index + count - 1) % count;
        }

        // 环形索引：后一个位置
        private int next(int index) {
            return (index + 1) % count;
        }

        /**
         * 计算：当剩余区间为 [left, right]（环形）时，
         * 当前行动者能获得的最大披萨总量
         *
         * 注意：这里的"当前行动者"是相对于调用者的对手，
         * 但由于零和博弈的对称性，函数返回的是"轮到谁行动，谁就能获得的最大值"
         */
        private long gainAfterOpponent(int left, int right) {
            int originalLeft = left, originalRight = right;

            // 记忆化：如果该区间已经计算过，直接返回
            if (seen[originalLeft][originalRight])
                return memo[originalLeft][originalRight];

            // 计算当前剩余块数：
            // 环形区间 [left, right] 的长度 = (left - right + count) % count + 1
            int remaining = (left - right + count) % count + 1;

            // 模拟对手的选择：
            // 对手会选择两端中较大的那块（贪心？不，这里其实是模拟对手最优选择）
            // 如果左端更大，对手取左端，区间左边界左移
            // 否则对手取右端，区间右边界右移
            if (slices[left] > slices[right])
                left = previous(left);
            else
                right = next(right);
            remaining--;

            // 计算当前行动者（也就是"先手"这一方）在对手取完后能获得的最大值：
            // - 如果只剩 1 块，直接取走
            // - 否则，当前行动者可以选择取左端或右端，取较大者
            long result = remaining == 1
                    ? slices[left]
                    : Math.max(
                    slices[left] + gainAfterOpponent(previous(left), right),
                    slices[right] + gainAfterOpponent(left, next(right))
            );

            // 记忆化存储
            seen[originalLeft][originalRight] = true;
            memo[originalLeft][originalRight] = result;
            return result;
        }
    }


}
