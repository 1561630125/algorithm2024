package OD.搜索与枚举.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-13 16:26
 */
public class 稿件评审安排 {

    public class Solution {

        // 当前最优解：最少审稿人数
        private int bestCount;
        // 当前最优解：在最少人数前提下的最小成本
        private int bestCost;

        /**
         * 计算覆盖所有论文的最小审稿成本。
         *
         * @param n      论文数量（编号 0 ~ n-1）
         * @param m      审稿人数量（编号 0 ~ m-1）
         * @param files  files[paper] = 能审这篇论文的审稿人列表
         * @param costs  costs[teacher] = 审稿人 teacher 的成本
         * @return 覆盖全部论文的最优成本；若无法覆盖则返回 Integer.MAX_VALUE
         */
        public int minimumReviewCost(int n, int m, int[][] files, int[] costs) {

            // ---------- 1. 预处理：把"每位审稿人能审哪些论文"转成位掩码 ----------
            // coverage[teacher] 的第 paper 位为 1，表示 teacher 能审 paper 这篇论文
            int[] coverage = new int[m];
            for (int paper = 0; paper < n; paper++) {
                for (int teacher : files[paper]) {
                    coverage[teacher] |= 1 << paper;
                }
            }
            // 目标掩码：所有 n 篇论文都被覆盖 = 低 n 位全 1
            int target = (1 << n) - 1;

            // ---------- 2. 初始化最优解 ----------
            bestCount = m + 1;              // 比"最多全选"还大，保证能被更新
            bestCost = Integer.MAX_VALUE;

            // ---------- 3. 回溯搜索 ----------
            search(0, 0, 0, 0, target, coverage, costs);

            return bestCost;
        }

        /**
         * 深度优先回溯搜索。
         *
         * @param index    当前可选的起始审稿人编号（保证组合不重复）
         * @param covered  已覆盖论文的位掩码
         * @param selected 已选审稿人数量
         * @param cost     已选审稿人总成本
         * @param target   目标掩码（全部论文）
         * @param coverage 每位审稿人的覆盖掩码
         * @param costs    每位审稿人的成本
         */
        private void search(
                int index,
                int covered,
                int selected,
                int cost,
                int target,
                int[] coverage,
                int[] costs
        ) {

            // ---------- 剪枝 1：已选人数超过当前最优人数，不可能更优 ----------
            if (selected > bestCount)
                return;

            // ---------- 找到可行解：所有论文都被覆盖 ----------
            if (covered == target) {
                // 优先比人数，人数相同再比成本
                if (selected < bestCount
                        || (selected == bestCount && cost < bestCost)) {
                    bestCount = selected;
                    bestCost = cost;
                }
                return;
            }

            // ---------- 枚举下一位审稿人 ----------
            // 从 index 开始，保证组合而非排列，避免重复搜索
            for (int teacher = index; teacher < coverage.length; teacher++) {
                search(
                        teacher + 1,                    // 下一次从下一位开始
                        covered | coverage[teacher],    // 并入该审稿人的覆盖
                        selected + 1,                   // 选中人数 +1
                        cost + costs[teacher],          // 成本累加
                        target,
                        coverage,
                        costs
                );
            }
        }
    }


    public class Solution2 {
        private int bestCount;
        private int bestCost;
        private int n;
        private java.util.List<java.util.Set<Integer>> canReview; // 每个审稿人能审的论文集合
        private int[] costs;

        public int minimumReviewCost(int n, int m, int[][] files, int[] costs) {
            this.n = n;
            this.costs = costs;

            // 预处理：每个审稿人能审哪些论文
            canReview = new java.util.ArrayList<>();
            for (int t = 0; t < m; t++)
                canReview.add(new java.util.HashSet<>());
            for (int paper = 0; paper < n; paper++)
                for (int teacher : files[paper])
                    canReview.get(teacher).add(paper);

            bestCount = m + 1;
            bestCost = Integer.MAX_VALUE;
            search(0, new java.util.HashSet<>(), 0, 0);
            return bestCost;
        }

        /**
         * @param index    下一个考虑的审稿人编号
         * @param covered  已覆盖的论文集合
         * @param selected 已选人数
         * @param cost     已花成本
         */
        private void search(int index, java.util.Set<Integer> covered,
                            int selected, int cost) {
            if (selected > bestCount) return;

            // 全覆盖：集合大小等于论文总数
            if (covered.size() == n) {
                if (selected < bestCount || (selected == bestCount && cost < bestCost)) {
                    bestCount = selected;
                    bestCost = cost;
                }
                return;
            }

            for (int teacher = index; teacher < canReview.size(); teacher++) {
                // 关键：为了能"回溯"，先复制一份集合再加
                java.util.Set<Integer> next = new java.util.HashSet<>(covered);
                next.addAll(canReview.get(teacher));

                search(teacher + 1, next, selected + 1, cost + costs[teacher]);

//                covered.removeAll(canReview.get(teacher)); // 撤销

            }
        }
    }

}
