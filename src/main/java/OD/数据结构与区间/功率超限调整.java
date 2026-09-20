package OD.数据结构与区间;

/**
 * 考点： 递归 or 枚举
 *
 * @author faming.yang@hand-china.com 2026-09-13 17:14
 */
public class 功率超限调整 {

    public class Solution {

        /**
         * 求最少移除多少个区间（mode），使剩余区间在每个位置叠加的功率都不超过 maxPower。
         *
         * @param maxPower 任意位置允许的最大叠加功率
         * @param modes    区间数组，modes[i] = {左端点, 右端点, 功率}
         * @return 最少需要移除的区间数
         */
        public int minimumRemovedModes(int maxPower, int[][] modes) {
            int best = 0;   // 满足约束时，最多能保留多少个区间

            // ---------- 1. 枚举所有子集（2^m 种选法） ----------
            for (int mask = 0; mask < (1 << modes.length); mask++) {

                // ---------- 2. 收集被选中区间的所有端点（用于切分位置） ----------
                java.util.SortedSet<Integer> points = new java.util.TreeSet<>();
                int selected = 0;
                for (int index = 0; index < modes.length; index++) {
                    if ((mask & (1 << index)) != 0) {   // 第 index 个区间被选中
                        selected++;
                        points.add(modes[index][0]);    // 左端点
                        points.add(modes[index][1]);    // 右端点
                    }
                }

                // ---------- 3. 把所有端点排序，形成若干"相邻区间段" ----------
                Integer[] boundaries = points.toArray(new Integer[0]);

                // ---------- 4. 检查每个相邻段内，叠加功率是否超过 maxPower ----------
                boolean valid = true;
                for (int point = 0; point + 1 < boundaries.length && valid; point++) {
                    // 取相邻两个端点 [boundaries[point], boundaries[point+1]] 作为一段
                    int power = 0;

                    // 累加所有"跨越这一段"的选中区间的功率
                    for (int index = 0; index < modes.length; index++) {
                        if ((mask & (1 << index)) != 0
                                && modes[index][0] <= boundaries[point]
                                && modes[index][1] >= boundaries[point + 1]) {
                            power += modes[index][2];
                        }
                    }

                    // 该段叠加功率超标 → 这个子集不合法
                    if (power > maxPower)
                        valid = false;
                }

                // ---------- 5. 合法则更新最多保留数 ----------
                if (valid)
                    best = Math.max(best, selected);
            }

            // 最少移除 = 总数 - 最多保留
            return modes.length - best;
        }
    }


    public class Solution3 {

        // ---------- 全局状态 ----------
        private int m;           // 区间总数
        private int maxPower;    // 每段允许的最大叠加功率
        private int best;        // 当前最优：最多能选多少个区间
        private int[][] modes;   // 区间数组：{左端点, 右端点, 功率}
        private int[] diff;      // 差分/累加数组：diff[i] = 第 i 段当前的叠加功率

        /**
         * 求最少移除多少个区间，使任意位置叠加功率 ≤ maxPower。
         */
        public int minimumRemovedModes(int maxPower, int[][] modes) {
            this.m = modes.length;
            this.maxPower = maxPower;
            this.modes = modes;
            this.best = 0;

            // ---------- 1. 离散化所有端点 ----------
            // 功率只会在区间端点处变化，所以只需关注这些端点切出的"段"
            java.util.TreeSet<Integer> set = new java.util.TreeSet<>();
            for (int[] mode : modes) {
                set.add(mode[0]);   // 左端点
                set.add(mode[1]);   // 右端点
            }
            // TreeSet 自动升序 + 去重，转成数组方便按下标访问
            int[] coords = set.stream().mapToInt(Integer::intValue).toArray();

            // diff[i] 表示"第 i 段"（coords[i] 到 coords[i+1]）的叠加功率
            diff = new int[coords.length];

            // ---------- 2. DFS 搜索 ----------
            dfs(0, 0, coords);

            // 最少移除 = 总数 - 最多保留
            return m - best;
        }

        /**
         * 深度优先搜索。
         *
         * @param index    当前考虑第 index 个区间（0..m-1）
         * @param selected 已选中的区间数
         * @param coords   离散化后的端点数组
         */
        private void dfs(int index, int selected, int[] coords) {

            // ---------- 剪枝 1：乐观上界 ----------
            // 即使把剩下所有区间全选上，也超不过当前 best → 没必要继续
            if (selected + (m - index) <= best)
                return;

            // ---------- 到达叶子：所有区间都决策完毕 ----------
            if (index == m) {
                best = Math.max(best, selected);
                return;
            }

            // ---------- 分支 1：尝试"选"第 index 个区间 ----------
            if (canAdd(index, coords, +1)) {          // 先检查是否超限
                apply(index, coords, +1);             // 加入：更新 diff
                dfs(index + 1, selected + 1, coords); // 递归下一层
                apply(index, coords, -1);             // 回溯：撤销 diff 修改
            }

            // ---------- 分支 2：不选第 index 个区间 ----------
            dfs(index + 1, selected, coords);
        }

        /**
         * 判断把第 idx 个区间加入后，是否会超过 maxPower。
         * sign = +1 表示"加入"，用于检查。
         */
        private boolean canAdd(int idx, int[] coords, int sign) {
            int l = modes[idx][0];
            int r = modes[idx][1];
            int w = modes[idx][2];

            // 遍历所有"段"，找被该区间覆盖的段
            for (int i = 0; i + 1 < coords.length; i++) {
                // 段 [coords[i], coords[i+1]] 完全落在区间 [l, r] 内
                if (coords[i] >= l && coords[i + 1] <= r) {
                    if (diff[i] + sign * w > maxPower)   // 加上后超限
                        return false;
                }
            }
            return true;
        }

        /**
         * 把第 idx 个区间的功率加到（或从）各覆盖段上。
         * sign = +1 加入，sign = -1 撤销。
         */
        private void apply(int idx, int[] coords, int sign) {
            int l = modes[idx][0];
            int r = modes[idx][1];
            int w = modes[idx][2];

            for (int i = 0; i + 1 < coords.length; i++) {
                if (coords[i] >= l && coords[i + 1] <= r) {
                    diff[i] += sign * w;
                }
            }
        }
    }

}
