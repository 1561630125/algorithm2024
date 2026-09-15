package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 15:58
 */
public class 限时渡河人数 {
    class Solution {

        /**
         * 计算两个人（或两个士兵）一起过桥所需的时间。
         *
         * 题意：两个人结伴过桥，过桥时间取决于较慢的那个。
         * 但这里加了一个「批量」条件：如果两人时间差距太大，
         * 慢的人可能被「批量处理」—— 这里用 min(second, 10 * first) 表示。
         *
         * 具体含义（根据上下文推断）：
         *   - 若 second <= 10 * first，则花费 second（慢者决定）
         *   - 否则花费 10 * first（快的那个来回跑 10 次？或某种批量上限）
         *
         * @param first  较快者的过桥时间
         * @param second 较慢者的过桥时间
         * @return 两人一起过桥的实际耗时
         */
        long batch66Crossing(long first, long second) {
            return Math.min(second, 10 * first);
        }

        /**
         * 计算在给定时间限制 limit 内，最多能让多少个士兵过桥，
         * 以及实际花费的总时间。
         *
         * 经典的「士兵过桥 / 手电筒过桥」问题：
         *   - 每次最多两人过桥，过桥时间取较慢者
         *   - 需要有人把手电筒送回，送回时间取送者自己的时间
         *   - 目标：在总时间不超过 limit 的前提下，让尽可能多的人过桥
         *
         * @param times 每个士兵的过桥时间（未排序，方法内会克隆并排序）
         * @param limit 总时间限制
         * @return long[]{ 过桥人数, 实际花费总时间 }
         *         若一个都过不了，返回 {0, 0}
         */
        long[] soldierCrossingPlan(long[] times, long limit) {
            // 克隆一份，避免修改调用者传入的数组
            times = times.clone();
            // 按过桥时间升序排序，快的在前
            java.util.Arrays.sort(times);

            int count = times.length;

            // 特殊情况 1：没有士兵，或者最快的人单独过桥都超时 → 一个都过不了
            if (count == 0 || times[0] > limit)
                return new long[] {0, 0};

            // 特殊情况 2：只有 1 个士兵 → 他自己过桥即可
            if (count == 1)
                return new long[] {1, times[0]};

            // dp[i] 表示「前 i+1 个士兵全部过桥」所需的最少总时间
            long[] dp = new long[count];

            // 只有 1 个人：直接过桥
            dp[0] = times[0];

            // 有 2 个人：两人一起过桥，时间取较慢者（经过 batch66Crossing 修正）
            dp[1] = batch66Crossing(times[0], times[1]);

            // 如果连前 2 个人都过不了，那只能让最快的 1 个人过
            if (dp[1] > limit)
                return new long[] {1, times[0]};

            // 从第 3 个人开始，逐个计算前 index+1 个人全部过桥的最少时间
            for (int index = 2; index < count; index++) {

                // 方案一：最快的 times[0] 来回接送
                //   1. 前 index 个人（下标 0..index-1）已经过桥，耗时 dp[index-1]
                //   2. times[0] 把手电筒送回来，耗时 times[0]
                //   3. times[0] 和 times[index] 一起过桥，耗时 batch66Crossing(times[0], times[index])
                long one = dp[index - 1] + times[0]
                        + batch66Crossing(times[0], times[index]);

                // 方案二：用「两个最慢的一起走，两个最快的负责送手电筒」的经典策略
                //   1. 前 index-1 个人（下标 0..index-2）已经过桥，耗时 dp[index-2]
                //   2. times[0] 送手电筒回来，耗时 times[0]
                //   3. times[index-1] 和 times[index] 一起过桥（两个最慢的一起），
                //      耗时 batch66Crossing(times[index-1], times[index])
                //   4. times[1] 送手电筒回来，耗时 times[1]
                //   5. times[0] 和 times[1] 一起过桥，耗时 batch66Crossing(times[0], times[1])
                long two = dp[index - 2] + times[0]
                        + batch66Crossing(times[index - 1], times[index])
                        + times[1]
                        + batch66Crossing(times[0], times[1]);

                // 取两种方案中较快的一个
                dp[index] = Math.min(one, two);

                // 如果当前人数已经超时，说明前 index 个人能过，第 index+1 个人过不了
                // 返回 {index, dp[index-1]}：过桥人数为 index（下标 0..index-1 共 index 人），
                // 对应时间为 dp[index-1]
                if (dp[index] > limit)
                    return new long[] {index, dp[index - 1]};
            }

            // 所有人都能在限时内过桥
            return new long[] {count, dp[count - 1]};
        }
    }
}
