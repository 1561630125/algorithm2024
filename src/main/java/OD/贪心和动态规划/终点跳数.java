package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 16:55
 */
public class 终点跳数 {

    class Solution {
        /**
         * 求从下标 0 跳到下标 size-1 所需的最少步数。
         *
         * 规则（基于代码逻辑推断）：
         *   - steps[i] 表示从位置 i 能向右跳的距离（可跳跃的"步长"）；
         *   - 从 i 出发可跳到 i + steps[i]；
         *   - 起点是 0，终点是 size-1；
         *   - 若无法到达返回 -1。
         *
         * @param steps 每个位置的可跳跃距离
         * @return 最少步数，无法到达返回 -1
         */
        int minimumStepsToEndpoint(int[] steps) {
            int size = steps.length;

            // 边界：长度为 0 或 1 时，起点即终点，不需要跳
            if (size <= 1)
                return 0;

            // 用一个"足够大的数"表示不可达（比任何合法步数都大即可）
            int infinity = size + 1;

            // best[i] 表示从起点 0 跳到位置 i 所需的最少步数
            int[] best = new int[size];
            java.util.Arrays.fill(best, infinity);   // 初始全部置为不可达

            // 初始化：前一半位置可以直接从起点一步到达
            // （推测规则：起点 0 可以一步跳到 [1, size/2) 范围内的任意位置）
            for (int index = 1; index < size / 2; index++)
                best[index] = 1;

            // 从左到右按顺序做 DP 递推
            for (int index = 1; index < size; index++) {
                int distance = steps[index];

                // 若当前位置不可达，或可跳距离非正，跳过
                if (best[index] == infinity || distance <= 0)
                    continue;

                // 目标位置（用 long 防止 index + distance 溢出）
                long target = (long) index + distance;

                // 若目标在范围内，尝试更新到 target 的最少步数
                if (target < size) {
                    best[(int) target] = Math.min(best[(int) target], best[index] + 1);
                }
            }

            // 终点不可达返回 -1，否则返回最少步数
            return best[size - 1] == infinity ? -1 : best[size - 1];
        }
    }


    class Solution2 {
        int size;
        int[] steps;
        int[] memo;                       // memo[i] = 从 i 到终点的最少步数
        static final int INF = Integer.MAX_VALUE;

        int minimumStepsToEndpoint(int[] steps) {
            this.steps = steps;
            this.size = steps.length;
            if (size <= 1) return 0;

            memo = new int[size];
            java.util.Arrays.fill(memo, -1);   // -1 表示"还没算过"

            int ans = dfs(0);
            return ans == INF ? -1 : ans;
        }

        private int dfs(int pos) {
            if (pos == size - 1) return 0;               // 终点
            if (pos < 0 || pos >= size) return INF;      // 越界

            if (memo[pos] != -1) return memo[pos];       // 记忆化命中

            int distance = steps[pos];
            int best = INF;
            if (distance > 0) {
                int next = pos + distance;
                if (next < size) {
                    int sub = dfs(next);
                    if (sub != INF) best = sub + 1;
                }
            }

            memo[pos] = best;                            // 记录
            return best;
        }
    }

    class Solution3 {
        int minimumStepsToEndpoint(int[] steps) {
            int size = steps.length;
            if (size <= 1) return 0;

            int[] dist = new int[size];
            java.util.Arrays.fill(dist, -1);   // -1 表示未访问
            java.util.Queue<Integer> queue = new java.util.ArrayDeque<>();
            dist[0] = 0;
            queue.offer(0);

            while (!queue.isEmpty()) {
                int pos = queue.poll();
                if (pos == size - 1) return dist[pos];   // 首次到达即最短

                int next = pos + steps[pos];
                if (steps[pos] > 0 && next < size && dist[next] == -1) {
                    dist[next] = dist[pos] + 1;
                    queue.offer(next);
                }
            }
            return -1;   // 到不了
        }
    }

}
