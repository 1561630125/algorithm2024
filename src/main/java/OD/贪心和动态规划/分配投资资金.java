package OD.贪心和动态规划;

/**
 * 考点：贪心
 *
 * @author faming.yang@hand-china.com 2026-09-03 16:58
 */
public class 分配投资资金 {

    class Solution {
        /**
         * 在预算、风险上限、单品种投资上限的约束下，求最大投资收益。
         *
         * @param budget          总预算
         * @param maximumRisk     可承受的最大风险值
         * @param perProductLimit 单个产品最多投多少
         * @param products        products[i] = {预期收益率, 风险值}（收益率单位疑似"百分之几"）
         * @return 最大收益（整数，疑似四舍五入到整数元）
         * <p>
         * 思路：贪心 —— 过滤掉风险超标的商品，把剩下的按收益率从高到低排序，
         * 优先把钱投给收益率最高的商品，直到预算用完或单品种限额用尽。
         */
        int maxInvestmentReturn(
                int budget,
                int maximumRisk,
                int perProductLimit,
                int[][] products
        ) {
            // 收集所有"风险可接受"的商品的预期收益率
            java.util.List<Integer> eligible = new java.util.ArrayList<>();
            for (
                    int[] product : products
            )
                // 跳过格式不对（长度 < 2）或风险超过上限的商品
                if (product.length >= 2 && product[1] <= maximumRisk)
                    eligible.add(product[0]);

            // 按收益率从高到低排序（贪心：优先投高收益）
            eligible.sort(java.util.Collections.reverseOrder());

            // 预算不能为负，负预算按 0 处理
            int remaining = Math.max(budget, 0);
            long weighted = 0;

            // 逐个商品投资
            for (int expected : eligible) {
                // 预算用完，或单品种限额非正，停止
                if (remaining <= 0 || perProductLimit <= 0)
                    break;

                // 本商品最多投 min(单品种限额, 剩余预算)
                int invested = Math.min(perProductLimit, remaining);
                // 累加 投入金额 × 收益率
                weighted += (long) invested * expected;
                // 扣减剩余预算
                remaining -= invested;
            }

            // 把 weighted（金额 × 百分数）四舍五入换算成整数结果
            // 正数：加 50 再除以 100；负数：取绝对值加 50 再除以 100，最后加负号
            return (int) (weighted >= 0 ? (weighted + 50) / 100 : -((-weighted + 50) / 100));
        }
    }

}
