package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 14:39
 */
public class 等重分石 {

    class Solution {

        /**
         * 把石头分成两堆，使两堆重量相等，求"其中一堆所需的最少石头个数"。
         *
         * 问题等价于：
         *   从 weights 中选若干块石头，使其总重量 = 总重量的一半，
         *   并且选出的石头个数最少。
         *
         * 如果总重量是奇数，无法平分，返回 -1。
         * 如果无法凑出总重量的一半，返回 -1。
         *
         * 这是经典的 0/1 背包 / 子集和问题，
         * 但这里求的不是"方案数"，而是"达到该重量所需的最少件数"。
         *
         * @param weights 每块石头的重量
         * @return        最少石头个数；无法平分时返回 -1
         */
        int minBalancedStoneCount(int[] weights) {

            // ---- 1. 求总重量 ----
            int total = 0;
            for (int weight : weights) total += weight;

            // 总重量为奇数，不可能平分
            if (total % 2 != 0) return -1;

            // ---- 2. 目标：凑出 total/2 的重量 ----
            int target = total / 2;

            // "不可达"标记：比任何可能的件数都大
            int unreachable = weights.length + 1;

            // minimum[w] = 凑出重量 w 所需的"最少石头件数"
            // 初始化为 unreachable，表示尚未到达
            int[] minimum = new int[target + 1];
            java.util.Arrays.fill(minimum, unreachable);
            minimum[0] = 0;   // 凑出重量 0 需要 0 件

            // ---- 3. 0/1 背包：逐个石头更新 DP ----
            for (int weight : weights) {
                // 倒序更新，保证每块石头只用一次
                for (int current = target; current >= weight; current--) {
                    // 选择：不拿这块石头（minimum[current]）
                    //       或 拿这块石头（minimum[current - weight] + 1）
                    // 取件数更少的方案
                    minimum[current] = Math.min(
                            minimum[current],
                            minimum[current - weight] + 1
                    );
                }
            }

            // ---- 4. 检查目标是否可达 ----
            // minimum[target] 仍是 unreachable 说明凑不出，返回 -1
            // 否则返回最少件数
            return minimum[target] <= weights.length ? minimum[target] : -1;
        }
    }

}
