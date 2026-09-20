package OD.搜索与枚举;

/**
 * 考点：BFS邻接表+往返
 *
 * @author faming.yang@hand-china.com 2026-09-13 15:57
 */
public class 巡逻相遇 {


    public class Solution {
        /**
         * 计算 A 与巡逻者最早相遇的回合数。
         *
         * @param n          房间总数（节点编号 0 ~ n-1）
         * @param edges      无向边数组，edges[i] = {u, v}
         * @param startA     A 的起始房间
         * @param patrolPath 巡逻者按顺序循环经过的房间序列
         * @return 最早相遇的回合数；无法相遇返回 -1
         */
        public int getMinMeet(int n, int[][] edges, int startA, int[] patrolPath) {

            // ---------- 1. 构建邻接表（无向图） ----------
            java.util.List<java.util.List<Integer>> graph = new java.util.ArrayList<>();
            for (int room = 0; room < n; room++)
                graph.add(new java.util.ArrayList<>());
            for (int[] edge : edges) {
                graph.get(edge[0]).add(edge[1]);   // u -> v
                graph.get(edge[1]).add(edge[0]);   // v -> u（无向）
            }

            // ---------- 2. BFS 求 startA 到所有房间的最短距离 ----------
            int[] distance = new int[n];
            java.util.Arrays.fill(distance, -1);   // -1 表示不可达
            distance[startA] = 0;

            java.util.ArrayDeque<Integer> queue = new java.util.ArrayDeque<>();
            queue.add(startA);

            while (!queue.isEmpty()) {
                int room = queue.remove();
                for (int next : graph.get(room)) {
                    if (distance[next] < 0) {              // 未访问过
                        distance[next] = distance[room] + 1;
                        queue.add(next);
                    }
                }
            }

            // ---------- 3. 边界处理 ----------
            if (patrolPath.length == 0)
                return -1;   // 巡逻路径为空，永远不会相遇

            // ---------- 4. 计算巡逻周期长度 ----------
            // 巡逻路径按往返方式循环：
            //   长度 L：patrolPath[0], [1], ..., [L-1], [L-2], ..., [1], 然后回到 [0]
            //   一个完整周期经过的房间数 = 2L - 2（L > 1 时）；L = 1 时为 1
            int cycleLength = patrolPath.length == 1 ? 1 : patrolPath.length * 2 - 2;

            // ---------- 5. 求 A 到巡逻路径上任意房间的最大距离 ----------
            // 用于确定模拟的上界：turn 超过 maximum + cycleLength 后
            // 状态一定会重复，无需继续模拟。
            int maximum = -1;
            for (int room : patrolPath)
                maximum = Math.max(maximum, distance[room]);

            if (maximum < 0)
                return -1;   // 巡逻路径上的房间 A 全部不可达

            // ---------- 6. 逐回合模拟相遇 ----------
            for (int turn = 1; turn <= maximum + cycleLength; turn++) {

                // 将 turn 映射到巡逻路径上的索引（考虑往返）
                int offset = turn % cycleLength;
                int pathIndex = offset < patrolPath.length
                        ? offset                      // 去程：0,1,...,L-1
                        : cycleLength - offset;       // 回程：L-2,...,1

                int room = patrolPath[pathIndex];

                // 判断：巡逻者第 turn 回合在 room，且 A 能在 turn 回合内到达 room
                // distance[room] <= turn 表示 A 可以提前到达并等待
                if (distance[room] >= 0 && distance[room] <= turn)
                    return turn;
            }

            return -1;   // 理论上不会执行到这里
        }
    }


}
