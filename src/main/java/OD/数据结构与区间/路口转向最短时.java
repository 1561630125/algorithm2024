package OD.数据结构与区间;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-12 16:25
 */
public class 路口转向最短时 {

    class Solution {

        /**
         * 在一个 rows × cols 的网格中，从起点 start 走到终点 end，求最小总耗时。
         *
         * 状态定义：
         *   每个格子 + 一个"进入方向"构成一个状态，共 cells * 4 个状态。
         *   方向编码：0=上, 1=右, 2=下, 3=左。
         *
         * 移动规则（关键）：
         *   - 每走一条边固定耗时 timePerRoad。
         *   - 转弯代价：只有当"下一步方向"恰好是"当前方向的右转"
         *     即 nextDirection == (direction + 1) % 4 时，转弯免费；
         *     否则需要额外等待当前格子的红绿灯 lights[row][column] 时长。
         *   （可以理解为：右转不等待，直行/左转/掉头都要等灯）
         *
         * 算法：分层图上的 Dijkstra 最短路。
         *
         * @param lights     每个格子的等待时间（红绿灯时长）
         * @param timePerRoad 走一条路（边）的固定耗时
         * @param start      起点坐标 [行, 列]
         * @param end        终点坐标 [行, 列]
         * @return 最小总耗时；不可达或输入非法返回 -1
         */
        long minimumIntersectionTime(long[][] lights, long timePerRoad, long[] start, long[] end) {

            // ---------- 1. 基本输入校验 ----------
            int rows = lights.length;
            int cols = rows == 0 ? 0 : lights[0].length;

            // 网格为空，或起点/终点坐标维度不足 → 非法
            if (rows == 0 || cols == 0 || start.length < 2 || end.length < 2)
                return -1;

            // 将二维坐标压缩成一维编号：id = row * cols + col
            long source = start[0] * cols + start[1];
            long target = end[0] * cols + end[1];

            int cells = rows * cols;   // 格子总数

            // 起点/终点编号越界 → 非法
            if (source < 0 || source >= cells || target < 0 || target >= cells)
                return -1;

            // ---------- 2. 距离数组与优先队列初始化 ----------
            // 用一个"足够大但不会溢出"的值作为无穷大（避免加法溢出 Long.MAX_VALUE）
            long infinity = Long.MAX_VALUE / 4;

            // 分层距离数组：distance[position * 4 + direction]
            long[] distance = new long[cells * 4];
            java.util.Arrays.fill(distance, infinity);

            // 小顶堆，元素为 {当前耗时, 位置编号, 进入方向}
            java.util.PriorityQueue<long[]> heap =
                    new java.util.PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));

            // ---------- 3. 起点入队：四个"进入方向"都可以作为初始状态，代价均为 0 ----------
            for (int direction = 0; direction < 4; direction++) {
                distance[(int) source * 4 + direction] = 0;
                heap.add(new long[] {0, source, direction});
            }

            // 四个方向的位移增量：上、右、下、左
            int[] dr = {-1, 0, 1, 0};
            int[] dc = {0, 1, 0, -1};

            // ---------- 4. Dijkstra 主循环 ----------
            while (!heap.isEmpty()) {
                long[] item = heap.remove();
                long current = item[0];              // 当前累计耗时
                int position = (int) item[1];        // 当前格子编号
                int direction = (int) item[2];       // 进入当前格子的方向

                // 懒惰删除：堆中可能有过期状态，跳过
                if (current != distance[position * 4 + direction])
                    continue;

                // 还原当前格子的行列坐标
                int row = position / cols;
                int column = position % cols;

                // 尝试向四个方向走一步
                for (int nextDirection = 0; nextDirection < 4; nextDirection++) {

                    int nr = row + dr[nextDirection];
                    int nc = column + dc[nextDirection];

                    // 越界检查
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols)
                        continue;

                    int nextPosition = nr * cols + nc;
                    int state = nextPosition * 4 + nextDirection;

                    // 计算从当前状态转移到下一状态的代价：
                    //   timePerRoad                     —— 走一步的固定耗时
                    // + lights[row][column]             —— 若不是"右转"则需等待当前格子的灯
                    //
                    // 方向编码 0=上,1=右,2=下,3=左，
                    // 因此"右转"即 (direction + 1) % 4 == nextDirection，
                    // 此时不额外等待；否则要加上当前格子等待时间。
                    long candidate = current + timePerRoad
                            + (nextDirection == (direction + 1) % 4 ? 0 : lights[row][column]);

                    // 松弛操作
                    if (candidate < distance[state]) {
                        distance[state] = candidate;
                        heap.add(new long[] {candidate, nextPosition, nextDirection});
                    }
                }
            }

            // ---------- 5. 汇总答案：终点任意进入方向的最小值 ----------
            long answer = infinity;
            for (int direction = 0; direction < 4; direction++)
                answer = Math.min(answer, distance[(int) target * 4 + direction]);

            // 若仍为无穷大说明不可达（正常网格图一般可达，此处兜底）
            return answer == infinity ? -1 : answer;
        }
    }



    class Solution2 {

        /**
         * 与优先队列版逻辑一致，只是把 Dijkstra 换成基于 Queue 的 SPFA。
         *
         * 状态：distance[position * 4 + direction]
         * 方向编码：0=上, 1=右, 2=下, 3=左
         *
         * 代价规则（与原代码保持一致）：
         *   走一步固定 timePerRoad；
         *   若不是右转，则再加当前格子的等待时间 lights[row][column]。
         */
        long minimumIntersectionTime(long[][] lights, long timePerRoad, long[] start, long[] end) {

            // ---------- 输入校验 ----------
            int rows = lights.length;
            int cols = rows == 0 ? 0 : lights[0].length;
            if (rows == 0 || cols == 0 || start.length < 2 || end.length < 2)
                return -1;

            long source = start[0] * cols + start[1];
            long target = end[0] * cols + end[1];
            int cells = rows * cols;

            if (source < 0 || source >= cells || target < 0 || target >= cells)
                return -1;

            long infinity = Long.MAX_VALUE / 4;
            long[] distance = new long[cells * 4];
            Arrays.fill(distance, infinity);

            // ---------- 队列（SPFA）----------
            Queue<long[]> queue = new ArrayDeque<>();
            boolean[] inQueue = new boolean[cells * 4];  // 可选：避免同一状态重复入队

            // 起点：4 个进入方向代价均为 0
            for (int direction = 0; direction < 4; direction++) {
                int state = (int) source * 4 + direction;
                distance[state] = 0;
                queue.add(new long[] {source, direction});
                inQueue[state] = true;
            }

            int[] dr = {-1, 0, 1, 0};
            int[] dc = {0, 1, 0, -1};

            // ---------- SPFA 主循环 ----------
            while (!queue.isEmpty()) {
                long[] item = queue.remove();
                int position = (int) item[0];
                int direction = (int) item[1];
                int currentState = position * 4 + direction;
                inQueue[currentState] = false;

                long current = distance[currentState];   // 当前状态的最新距离
                int row = position / cols;
                int column = position % cols;

                for (int nextDirection = 0; nextDirection < 4; nextDirection++) {
                    int nr = row + dr[nextDirection];
                    int nc = column + dc[nextDirection];
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols)
                        continue;

                    int nextPosition = nr * cols + nc;
                    int state = nextPosition * 4 + nextDirection;

                    long candidate = current + timePerRoad
                            + (nextDirection == (direction + 1) % 4 ? 0 : lights[row][column]);

                    // 松弛成功就（若不在队列中）重新入队
                    if (candidate < distance[state]) {
                        distance[state] = candidate;
                        if (!inQueue[state]) {
                            queue.add(new long[] {nextPosition, nextDirection});
                            inQueue[state] = true;
                        }
                    }
                }
            }

            long answer = infinity;
            for (int direction = 0; direction < 4; direction++)
                answer = Math.min(answer, distance[(int) target * 4 + direction]);

            return answer == infinity ? -1 : answer;
        }
    }

}
