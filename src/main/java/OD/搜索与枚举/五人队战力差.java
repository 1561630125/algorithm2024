package OD.搜索与枚举;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-12 17:57
 */
public class 五人队战力差 {

    class Solution {

        // ---------- 全局状态 ----------
        private long total;   // 所有队员的 rating 总和
        private long best;    // 当前找到的最小"两队强度差"

        /**
         * 递归枚举：从 index 开始选人，已选 taken 个，当前所选之和为 current。
         *
         * 目标：恰好选出 5 个人组成一队，使得
         *   |total - 2 * current|
         * 最小。
         *
         * 解释：
         *   设选出的一队强度为 current，则另一队强度 = total - current。
         *   两队差 = |current - (total - current)| = |total - 2 * current|。
         *
         * @param ratings 每个队员的 rating
         * @param index   当前考虑到的下标
         * @param taken   已选人数
         * @param current 已选队员的 rating 之和
         */
        private void visit(long[] ratings, int index, int taken, long current) {

            // ---------- 1. 已选满 5 人 ----------
            if (taken == 5) {
                // 更新最小差值
                best = Math.min(best, Math.abs(total - 2 * current));
                return;
            }

            // ---------- 2. 剪枝 ----------
            // index == ratings.length          → 没有更多元素可选了
            // taken + (ratings.length - index) < 5
            //   → 即使把剩下的全部选上，也凑不满 5 人，直接放弃
            if (index == ratings.length || taken + ratings.length - index < 5)
                return;

            // ---------- 3. 选当前元素 ----------
            visit(ratings, index + 1, taken + 1, current + ratings[index]);

            // ---------- 4. 不选当前元素 ----------
            visit(ratings, index + 1, taken, current);
        }


        private long visit(long[] ratings, int index, int taken, long current, long total) {
            if (taken == 5)
                return Math.abs(total - 2 * current);
            if (index == ratings.length || taken + ratings.length - index < 5)
                return Long.MAX_VALUE;   // 此分支无解

            long pick    = visit(ratings, index + 1, taken + 1, current + ratings[index], total);
            long skip    = visit(ratings, index + 1, taken, current, total);
            return Math.min(pick, skip);
        }

        long solve(long[] ratings) {
            long total = 0 ;
            return visit(ratings, 0, 0, 0, total);
        }

        /**
         * 求：把队员分成两队（每队恰好 5 人？据代码推断为"选 5 人组一队"），
         * 使得两队总 strength 之差最小，返回该最小差值。
         *
         * @param ratings 每个队员的 rating
         * @return 最小强度差；人数不足 5 时返回 0
         */
        long minimumTeamStrengthDifference(long[] ratings) {

            // 人数不足 5 人，无法组队 → 按约定返回 0
            if (ratings.length < 5)
                return 0;

            // ---------- 1. 求总和 ----------
            total = 0;
            for (long value : ratings)
                total += value;

            // ---------- 2. 初始化最优值 ----------
            best = Long.MAX_VALUE;

            // ---------- 3. 从下标 0、已选 0 人、当前和 0 开始递归枚举 ----------
            visit(ratings, 0, 0, 0);

            // ---------- 4. 返回结果 ----------
            // 若 best 仍为无穷大（理论上不会），返回 0 兜底
            return best == Long.MAX_VALUE ? 0 : best;
        }
    }

}
