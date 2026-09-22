package OD.贪心和动态规划;

/**
 * 考点：贪心
 *
 * @author faming.yang@hand-china.com 2026-09-15 17:15
 */
public class 糖果折半 {


    class Solution {
        /**
         * 给定初始糖果数 candies，每次可以做以下三种操作之一：
         *   1. 若为偶数：除以 2        （candies /= 2）
         *   2. 若为奇数且 candies == 3 或 candies % 4 == 1：减 1
         *   3. 否则（奇数且 candies % 4 == 3，且不为 3）：加 1
         * 求把 candies 变成 1 所需的最少操作次数。
         *
         * 本质：这是经典的 "Integer Replacement"（整数替换）问题，
         *       求 n → 1 的最少步数，允许 n/2、n-1、n+1（奇偶规则）。
         *
         * @param candies 初始糖果数
         * @return 最少操作次数；输入非法（<=0 或 >= 10^10）返回 -1
         */
        int minimumCandyOperations(long candies) {
            // 输入合法性检查：
            //   candies <= 0 无意义
            //   candies >= 10_000_000_000 超出约定范围，按题意返回 -1
            if (candies <= 0L || candies >= 10_000_000_000L)
                return -1;

            int operations = 0;   // 记录操作次数

            // 主循环：只要还没到 1，就持续操作
            while (candies != 1L) {
                if ((candies & 1L) == 0L) {
                    // 情况 1：偶数（最低位为 0）→ 直接除以 2，最优
                    candies /= 2L;
                } else if (candies == 3L || candies % 4L == 1L) {
                    // 情况 2：奇数且 (candies == 3 或 candies % 4 == 1) → 减 1
                    //   为什么减 1？
                    //   奇数加/减 1 后会变成偶数，希望这个偶数能被 4 整除，
                    //   这样下一步除 2 后仍是偶数，能连续除更多次。
                    //   candies % 4 == 1 时，减 1 得到 %4==0，更优。
                    //   candies == 3 是特例：3 → 2 → 1（2 步），
                    //   而 3 → 4 → 2 → 1（3 步），所以 3 应减 1。
                    candies--;
                } else {
                    // 情况 3：奇数且 candies % 4 == 3（且不为 3）→ 加 1
                    //   此时加 1 得到 %4==0，下一步除 2 后是偶数，更优。
                    candies++;
                }
                operations++;   // 每执行一次操作计数 +1
            }

            return operations;
        }
    }


    class Solution2 {
        java.util.Map<Long, Integer> memo = new java.util.HashMap<>();

        int minimumCandyOperations(long candies) {
            if (candies <= 0L || candies >= 10_000_000_000L)
                return -1;
            return f(candies);
        }

        // f(n) = n → 1 的最少操作次数
        private int f(long n) {
            if (n == 1L) return 0;
            if (memo.containsKey(n)) return memo.get(n);

            int result;
            if ((n & 1L) == 0L) {
                // 偶数：只能除 2
                result = 1 + f(n / 2);
            } else {
                // 奇数：取 +1 / -1 的较小者
                result = 1 + Math.min(f(n - 1), f(n + 1));
            }

            memo.put(n, result);
            return result;
        }
    }

}
