package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 20:43
 */
public class 双产品理财 {

    /**
     * 投资组合优化方案
     *
     * 核心逻辑：
     * 1. 在预算和风险限制内，从所有项目中最多选择2个项目进行投资
     * 2. 优先投资收益率高的项目，剩余资金投入次优项目
     * 3. 每个项目的投资金额不能超过其限额
     * 4. 采用暴力枚举所有可能的1-2个项目组合，找出最优解
     */
    class Solution {
        // ========== 全局状态变量 ==========
        private long budget;           // 总预算
        private int riskLimit;         // 风险上限
        private int[] returnRates;     // 每个项目的收益率
        private int[] risks;           // 每个项目的风险值
        private long[] limits;         // 每个项目的投资上限
        private long bestReturn;       // 当前最优收益
        private long[] best;           // 当前最优投资方案

        /**
         * 计算最优投资方案
         *
         * @param budget       总预算金额
         * @param riskLimit    允许的最大风险值
         * @param returnRates  每个项目的收益率（数组索引对应项目编号）
         * @param risks        每个项目的风险值
         * @param limits       每个项目的最大可投资金额
         * @return             最优投资方案数组，best[i]表示投资到项目i的金额
         */
        long[] optimalInvestmentPlan(
                long budget, int riskLimit, int[] returnRates, int[] risks, long[] limits) {

            int count = returnRates.length;

            // ========== 参数校验 ==========
            if (risks.length != count || limits.length != count)
                return new long[count];  // 参数不匹配，返回全0方案

            // ========== 初始化全局状态 ==========
            this.budget = Math.max(0L, budget);
            this.riskLimit = riskLimit;
            this.returnRates = returnRates;
            this.risks = risks;
            this.limits = limits;
            bestReturn = 0L;
            best = new long[count];

            // ========== 枚举所有可能的1-2个项目组合 ==========
            // 为什么只考虑1-2个项目？
            // 因为受风险限制，可能无法投资所有项目，而最优解通常只会选择少数高收益项目
            for (int first = 0; first < count; first++) {
                // 情况1：只投资一个项目（first）
                consider(first, -1);

                // 情况2：投资两个项目（first 和 second）
                for (int second = first + 1; second < count; second++)
                    consider(first, second);
            }
            return best;
        }

        /**
         * 考虑投资指定的一到两个项目
         *
         * @param first  第一个项目的索引
         * @param second 第二个项目的索引（-1表示只投资一个项目）
         */
        private void consider(int first, int second) {
            // ========== 检查风险是否超标 ==========
            long risk = risks[first] + (second < 0 ? 0L : risks[second]);
            if (risk > riskLimit)
                return;  // 风险超出限制，放弃此组合

            // ========== 确定投资优先级 ==========
            // 策略：优先投资收益率高的项目（贪心思想）
            int high = first, low = second;
            if (second >= 0 && returnRates[second] > returnRates[first]) {
                high = second;  // 高收益项目
                low = first;    // 低收益项目
            }

            // ========== 分配投资金额 ==========
            long[] amounts = new long[returnRates.length];
            long remaining = budget;

            // 先给高收益项目分配资金（尽可能投满）
            amounts[high] = Math.min(remaining, Math.max(0L, limits[high]));
            remaining -= amounts[high];

            // 如果还有剩余资金，分配给低收益项目
            if (low >= 0)
                amounts[low] = Math.min(remaining, Math.max(0L, limits[low]));
            // 注意：如果资金还有剩余，不会分配给其他项目（策略限制最多2个项目）

            // ========== 计算总收益 ==========
            long currentReturn = 0L;
            for (int index = 0; index < amounts.length; index++)
                currentReturn += amounts[index] * returnRates[index];

            // ========== 更新最优解 ==========
            if (currentReturn > bestReturn) {
                bestReturn = currentReturn;
                best = amounts;  // 注意：这里直接引用局部数组，存在风险
            }
        }
    }


}
