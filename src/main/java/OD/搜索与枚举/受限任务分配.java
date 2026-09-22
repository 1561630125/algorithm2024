package OD.搜索与枚举;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-21 20:14
 */
public class 受限任务分配 {
    private Integer bestCost;
    private Integer bestCount;
    private Integer budgetUp;
    private Integer addCostM;
    private Integer returnCostM;

    public int[] assignTasks(int newTasks, int budget, int addCost, int returnCost) {
        // write code here
        budgetUp = budget;
        addCostM = addCost;
        returnCostM = returnCost;

        dfs(newTasks , 0 , 0, new int[]{});

        return new int[0];
    }

    boolean dfs(int newTasks, int limit, int count, int[] memo) {
        if (newTasks == 1) {
            return true;
        }
        if (limit > budgetUp) {
            return false;
        }
        if (limit < bestCost || (limit == bestCost && count < bestCount)) {
            bestCost = limit;
            bestCount = count;
        }

        if (newTasks % 2 == 0) {
            return dfs(newTasks / 2, limit + 1, count + 1, memo);
        } else {
            boolean add = dfs(newTasks + 1, limit + addCostM, count + 1, memo);

            boolean sub = dfs(newTasks - 1, limit + returnCostM, count + 1, memo);

            return add || sub;
        }
    }

    /**
     * 求任务数降至 1 的最低成本，以及该成本下最少操作次数。
     *
     * 题目背景（根据代码反推）：
     * 初始有 newTasks 个任务，允许两种操作把任务数变少：
     *   1. 若任务数是偶数 x，可以直接“减半”，成本 1，操作次数 1；
     *   2. 若任务数是奇数 x，可以：
     *        - 先加 1 变成 x+1（偶数），再减半，成本 addCost + 1，操作次数 2；
     *        - 先减 1 变成 x-1（偶数），再减半，成本 returnCost + 1，操作次数 2。
     *      其中 addCost 表示“加 1”的成本，returnCost 表示“减 1”的成本。
     *
     * 目标：把任务数降到 1，并且总成本不能超过 budget。
     * 返回：
     *   - 若最低成本超过 budget，返回 [-1, -1]；
     *   - 否则返回 [最低成本, 该成本下的最少操作次数]。
     */
    public class Solution {

        /**
         * @param newTasks   初始任务数
         * @param budget     预算上限
         * @param addCost    对奇数任务数执行“加 1”操作的成本
         * @param returnCost 对奇数任务数执行“减 1”操作的成本
         * @return 长度为 2 的数组：
         *         成功时返回 [最低成本, 最少操作次数]；
         *         若最低成本超过预算，返回 [-1, -1]。
         */
        public int[] assignTasks(int newTasks, int budget, int addCost, int returnCost) {
            // cost[x]：把任务数 x 降到 1 所需的最低成本
            // steps[x]：在达到最低成本的前提下，把任务数 x 降到 1 所需的最少操作次数
            int[] cost = new int[newTasks + 1];
            int[] steps = new int[newTasks + 1];

            // 从 2 开始递推到 newTasks
            // cost[1] 和 steps[1] 默认为 0，因为 1 已经是目标状态，不需要任何操作
            for (int x = 2; x <= newTasks; x++) {
                if (x % 2 == 0) {
                    // 如果 x 是偶数，最优策略通常是直接减半
                    // 成本 = cost[x / 2] + 1
                    // 操作次数 = steps[x / 2] + 1
                    cost[x] = cost[x / 2] + 1;
                    steps[x] = steps[x / 2] + 1;
                } else {
                    // 如果 x 是奇数，有两种方式把它变成偶数后再减半：
                    //   方式 A：先减 1，变成 (x - 1) / 2
                    //   方式 B：先加 1，变成 (x + 1) / 2
                    int a = (x - 1) / 2; // 先减 1 再减半后的任务数
                    int b = (x + 1) / 2; // 先加 1 再减半后的任务数

                    // ca：选择“先减 1 再减半”的总成本
                    //     其中 returnCost 是减 1 的成本，+1 是减半操作的成本
                    int ca = cost[a] + returnCost + 1;

                    // cb：选择“先加 1 再减半”的总成本
                    //     其中 addCost 是加 1 的成本，+1 是减半操作的成本
                    int cb = cost[b] + addCost + 1;

                    // 比较两种方式：
                    //   1. 优先选择成本更小的；
                    //   2. 如果成本相同，选择操作次数更少的（steps[a] <= steps[b]）。
                    //
                    // 注意：这里比较的是 steps[a] 和 steps[b]，而不是 steps[a] + 2 和 steps[b] + 2，
                    // 因为两种方式最后都要额外进行“加/减 1”和“减半”两步，
                    // 所以比较子问题的操作次数即可。
                    if (ca < cb || ca == cb && steps[a] <= steps[b]) {
                        cost[x] = ca;
                        steps[x] = steps[a] + 2; // 加/减 1 一步，减半一步，共 2 步
                    } else {
                        cost[x] = cb;
                        steps[x] = steps[b] + 2; // 加/减 1 一步，减半一步，共 2 步
                    }
                }
            }

            // 如果最低成本超过预算，返回 [-1, -1]
            // 否则返回最低成本及其对应的最少操作次数
            return cost[newTasks] > budget
                    ? new int[] {-1, -1}
                    : new int[] {cost[newTasks], steps[newTasks]};
        }
    }

    /**
     * 求任务数降至 1 的最低成本，以及该成本下最少操作次数。
     *
     * 题目背景（根据代码反推）：
     * 初始有 newTasks 个任务，允许两种操作把任务数变少：
     *   1. 若任务数是偶数 x，可以直接“减半”，成本 1，操作次数 1；
     *   2. 若任务数是奇数 x，可以：
     *        - 先加 1 变成 x+1（偶数），再减半，成本 addCost + 1，操作次数 2；
     *        - 先减 1 变成 x-1（偶数），再减半，成本 returnCost + 1，操作次数 2。
     *      其中 addCost 表示“加 1”的成本，returnCost 表示“减 1”的成本。
     *
     * 目标：把任务数降到 1，并且总成本不能超过 budget。
     * 返回：
     *   - 若最低成本超过 budget，返回 [-1, -1]；
     *   - 否则返回 [最低成本, 该成本下的最少操作次数]。
     */
    public class Solution3 {

        // 记忆化数组：
        // memoCost[x]：把任务数 x 降到 1 所需的最低成本
        // memoSteps[x]：在达到最低成本的前提下，把任务数 x 降到 1 所需的最少操作次数
        // 初始值为 -1，表示尚未计算过
        private int[] memoCost;
        private int[] memoSteps;

        /**
         * @param newTasks   初始任务数
         * @param budget     预算上限
         * @param addCost    对奇数任务数执行“加 1”操作的成本
         * @param returnCost 对奇数任务数执行“减 1”操作的成本
         * @return 长度为 2 的数组：
         *         成功时返回 [最低成本, 最少操作次数]；
         *         若最低成本超过预算，返回 [-1, -1]。
         */
        public int[] assignTasks(int newTasks, int budget, int addCost, int returnCost) {
            // 初始化记忆化数组，长度为 newTasks + 1
            // 因为递归过程中任务数最多只会到达 newTasks（奇数加 1 后最多变成 newTasks + 1，
            // 但 newTasks 为奇数时，(newTasks + 1) / 2 远小于 newTasks，所以不会越界）
            memoCost = new int[newTasks + 1];
            memoSteps = new int[newTasks + 1];
            Arrays.fill(memoCost, -1);
            Arrays.fill(memoSteps, -1);

            // 基本情况：任务数已经是 1，不需要任何操作
            memoCost[1] = 0;
            memoSteps[1] = 0;

            // 递归求解
            int minCost = dfsCost(newTasks, addCost, returnCost);
            int minSteps = dfsSteps(newTasks, addCost, returnCost);

            // 如果最低成本超过预算，返回 [-1, -1]
            // 否则返回最低成本及其对应的最少操作次数
            return minCost > budget
                    ? new int[] {-1, -1}
                    : new int[] {minCost, minSteps};
        }

        /**
         * 递归 + 记忆化：求把任务数 x 降到 1 的最低成本。
         *
         * @param x          当前任务数
         * @param addCost    加 1 操作的成本
         * @param returnCost 减 1 操作的成本
         * @return 把 x 降到 1 的最低成本
         */
        private int dfsCost(int x, int addCost, int returnCost) {
            // ===== 显式递归终止条件 =====
            // 任务数已经是 1，不需要任何操作，操作次数为 0
            if (x == 1) {
                return 0;
            }

            // 如果已经计算过，直接返回
            if (memoCost[x] != -1) {
                return memoCost[x];
            }

            int result;
            if (x % 2 == 0) {
                // x 是偶数：直接减半，成本 = dfsCost(x / 2) + 1
                result = dfsCost(x / 2, addCost, returnCost) + 1;
            } else {
                // x 是奇数：可以减 1 或加 1 变成偶数后再减半，取成本更小者
                int a = (x - 1) / 2; // 减 1 再减半后的任务数
                int b = (x + 1) / 2; // 加 1 再减半后的任务数

                // 方式 A：先减 1 再减半
                // 成本 = dfsCost(a) + returnCost（减 1）+ 1（减半）
                int ca = dfsCost(a, addCost, returnCost) + returnCost + 1;

                // 方式 B：先加 1 再减半
                // 成本 = dfsCost(b) + addCost（加 1）+ 1（减半）
                int cb = dfsCost(b, addCost, returnCost) + addCost + 1;

                result = Math.min(ca, cb);
            }

            // 记忆化并返回
            memoCost[x] = result;
            return result;
        }

        /**
         * 递归 + 记忆化：在最低成本的前提下，求把任务数 x 降到 1 的最少操作次数。
         *
         * 注意：此方法依赖 dfsCost 已经计算完毕（memoCost 已填充），
         * 因为判断“走哪条路径”需要比较成本。
         *
         * @param x          当前任务数
         * @param addCost    加 1 操作的成本
         * @param returnCost 减 1 操作的成本
         * @return 把 x 降到 1 的最少操作次数（在最低成本前提下）
         */
        private int dfsSteps(int x, int addCost, int returnCost) {
            // ===== 显式递归终止条件 =====
            // 任务数已经是 1，不需要任何操作，操作次数为 0
            if (x == 1) {
                return 0;
            }


            // 如果已经计算过，直接返回
            if (memoSteps[x] != -1) {
                return memoSteps[x];
            }

            int result;
            if (x % 2 == 0) {
                // x 是偶数：只有一条路径，直接减半
                // 操作次数 = dfsSteps(x / 2) + 1
                result = dfsSteps(x / 2, addCost, returnCost) + 1;
            } else {
                // x 是奇数：需要比较两条路径的成本，选择成本更小者
                // 若成本相同，选择操作次数更少者
                int a = (x - 1) / 2;
                int b = (x + 1) / 2;

                int ca = dfsCost(a, addCost, returnCost) + returnCost + 1;
                int cb = dfsCost(b, addCost, returnCost) + addCost + 1;

                if (ca < cb) {
                    // 选择“减 1 再减半”，操作次数 = dfsSteps(a) + 2
                    result = dfsSteps(a, addCost, returnCost) + 2;
                } else if (ca > cb) {
                    // 选择“加 1 再减半”，操作次数 = dfsSteps(b) + 2
                    result = dfsSteps(b, addCost, returnCost) + 2;
                } else {
                    // 成本相同：选择操作次数更少的路径
                    int sa = dfsSteps(a, addCost, returnCost);
                    int sb = dfsSteps(b, addCost, returnCost);
                    result = Math.min(sa, sb) + 2;
                }
            }

            // 记忆化并返回
            memoSteps[x] = result;
            return result;
        }
    }

}
