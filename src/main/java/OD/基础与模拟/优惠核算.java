package OD.基础与模拟;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 13:28
 */
public class 优惠核算 {

    static class Res{
        long amount;
        long count;

        public Res(long amount, long count) {
            this.amount = amount;
            this.count = count;
        }
    }

    static long[][] bestShoppingDiscount(
            int fullReductionCount,
            int discountCount,
            int noThresholdCount,
            long[] prices
    ) {

        List<Res> res = new LinkedList<>();
        for(int i = 0; i < prices.length; i++) {
            long price = prices[i];
            long min1 = Math.min(fullReductionCount, Math.floorDiv(price, 100));
            long min3 = Math.min(noThresholdCount, Math.floorDiv(price, 5));

            long amount = price - (min1 * 10 + min3 * 5);
            long count = min1 + min3;

            boolean flag = discountCount > 1;

            long price2 =  (flag ? (long) Math.floor(price * 0.92) : 0) - min1 * 10;
            long count2 = min1 + (flag ? 1 : 0);
            if (price2 < amount) {
                amount = price2;
                count = count2;
            } else if (price2 == amount && count > count2) {
                count = count2;
            }

            long price3 = (flag ? (long) Math.floor(price * 0.92) : 0) - min3 * 5;
            long count3 = min3 + (flag ? 1 : 0);
            if (price3 < amount) {
                amount = price3;
                count = count3;
            } else if (price3 == amount && count > count2) {
                count = count3;
            }

            res.add(new Res(amount, count));
        }

        long[][] res1 = new long[res.size()][2];
        for(int i = 0; i < res.size(); i++) {
            res1[i][0] = res.get(i).amount;
            res1[i][1] = res.get(i).count;
        }
        return res1;
    }

    long floorDiv(long value, long divisor) {
        return Math.floorDiv(value, divisor);
    }

    /**
     * 应用单种优惠券，计算优惠后的价格和使用次数
     *
     * @param type  优惠类型：0=满减，1=折扣，2=立减
     * @param count 该类型优惠券可用数量
     * @param price 当前商品价格
     * @return long[]{优惠后价格, 使用次数}
     */
    long[] apply(int type, int count, long price) {
        long used;  // 实际使用的优惠次数

        // 类型0：满减优惠（每满100减10）
        if (type == 0) {
            // 计算最多能用几次：min(价格包含的100元份数, 可用数量)
            used = Math.min(floorDiv(price, 100), count);
            // 返回 [原价 - 使用次数 * 10, 使用次数]
            return new long[] {price - used * 10, used};
        }

        // 类型1：折扣优惠（92折，可叠加使用）
        if (type == 1) {
            // 92折优惠最多只能用1次（业务规则限制）
            used = Math.min(1, count);
            // 价格乘以 0.92 的 used 次方，向下取整
            return new long[] {(long) Math.floor(price * Math.pow(.92, used)), used};
        }

        // 类型2：立减优惠（每满5减5，即价格>=5就减5）
        used = Math.min(floorDiv(price, 5), count);
        return new long[] {price - used * 5, used};
    }

    /**
     * 为每个商品计算最优的优惠组合
     *
     * @param fullReductionCount 满减券数量（类型0）
     * @param discountCount      折扣券数量（类型1）
     * @param noThresholdCount   立减券数量（类型2）
     * @param prices             商品价格数组
     * @return long[商品数量][2]，每行：[最优价格, 使用的最少优惠券数]
     */
    long[][] bestShoppingDiscount2(
            int fullReductionCount, int discountCount, int noThresholdCount, long[] prices) {

        // 各类型优惠券的可用数量
        int[] counts = {fullReductionCount, discountCount, noThresholdCount};

        // 所有可能的优惠券使用顺序组合（2种券的排列，共6种）
        // 0=满减, 1=折扣, 2=立减
        int[][] orders = {
                {0, 1},  // 先满减，后折扣
                {1, 0},  // 先折扣，后满减
                {0, 2},  // 先满减，后立减
                {2, 0},  // 先立减，后满减
                {1, 2},  // 先折扣，后立减
                {2, 1}   // 先立减，后折扣
        };

        // 结果数组：每行两个元素 [最终价格, 优惠券使用总数]
        long[][] output = new long[prices.length][2];

        // 遍历每个商品
        for (int index = 0; index < prices.length; index++) {
            long finalPrice = prices[index];   // 当前最优价格，初始为原价
            long minimumCount = 0;             // 当前最优价格对应的最少优惠券数

            // 尝试所有优惠券使用顺序
            for (int[] order : orders) {
                // 第一步：使用第一种优惠券
                long[] first = apply(order[0], counts[order[0]], prices[index]);

                // 第二步：在第一步结果上使用第二种优惠券
                long[] second = apply(order[1], counts[order[1]], first[0]);

                // 总共使用的优惠券数量
                long used = first[1] + second[1];

                // 如果这个顺序得到的价格更低，则更新最优解
                if (second[0] < finalPrice) {
                    finalPrice = second[0];
                    minimumCount = used;
                }
                // 如果价格相同，选择使用优惠券更少的方案
                else if (second[0] == finalPrice) {
                    minimumCount = Math.min(minimumCount, used);
                }
            }

            // 记录当前商品的最优结果
            output[index][0] = finalPrice;
            output[index][1] = minimumCount;
        }

        return output;
    }


    public static void main(String[] args) {
        System.out.println(Arrays.deepToString(bestShoppingDiscount(3, 2, 5, new long[]{100, 200, 399})));
    }

}
