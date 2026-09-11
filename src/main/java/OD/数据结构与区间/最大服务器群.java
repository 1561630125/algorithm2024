package OD.数据结构与区间;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 22:38
 */
public class 最大服务器群 {

    class Solution {

        /**
         * 求网格中最大的「正数连通块」大小
         * 连通：上下左右四方向相邻
         * 只统计值 > 0 的格子
         *
         * @param grid 二维整数网格
         * @return 最大连通块的格子数
         */
        int largestServerNetwork(int[][] grid) {

            // 空网格检查
            if (grid.length == 0)
                return 0;

            int rows = grid.length, cols = grid[0].length;

            if (cols == 0)
                return 0;

            // 校验每行长度一致
            for (int[] row : grid)
                if (row.length != cols)
                    return 0;

            // visited 标记是否访问过
            boolean[][] visited = new boolean[rows][cols];

            int result = 0;

            // 遍历每个格子作为 BFS 起点
            for (int row = 0; row < rows; row++)
                for (int column = 0; column < cols; column++) {

                    // 只处理「值 > 0」且未访问的格子
                    if (grid[row][column] <= 0 || visited[row][column])
                        continue;

                    // 一维数组模拟 BFS 队列，存格子编号 row * cols + column
                    int[] queue = new int[rows * cols];
                    int head = 0, tail = 1;

                    // 起点入队并标记
                    queue[0] = row * cols + column;
                    visited[row][column] = true;

                    while (head < tail) {
                        // 出队，编号还原成坐标
                        int value = queue[head++];
                        int currentRow = value / cols;
                        int currentColumn = value % cols;

                        // 四个方向的邻居
                        int[][] next = {
                                {currentRow - 1, currentColumn},  // 上
                                {currentRow + 1, currentColumn},  // 下
                                {currentRow, currentColumn - 1},  // 左
                                {currentRow, currentColumn + 1}   // 右
                        };

                        for (int[] cell : next) {
                            // 越界、已访问、非正数都跳过
                            if (cell[0] >= 0 && cell[0] < rows
                                    && cell[1] >= 0 && cell[1] < cols
                                    && !visited[cell[0]][cell[1]]
                                    && grid[cell[0]][cell[1]] > 0) {

                                visited[cell[0]][cell[1]] = true;
                                queue[tail++] = cell[0] * cols + cell[1];
                            }
                        }
                    }

                    // tail 就是本连通块的格子数
                    result = Math.max(result, tail);
                }

            return result;
        }
    }


    class Solution2 {
        int largestServerNetwork(int[][] grid) {
            if (grid.length == 0) return 0;
            int rows = grid.length, cols = grid[0].length;
            if (cols == 0) return 0;
            for (int[] row : grid)
                if (row.length != cols) return 0;

            boolean[][] visited = new boolean[rows][cols];
            int result = 0;

            for (int row = 0; row < rows; row++)
                for (int column = 0; column < cols; column++) {

                    if (grid[row][column] <= 0 || visited[row][column])
                        continue;

                    // 用 Queue 存坐标
                    Queue<int[]> queue = new ArrayDeque<>();
                    queue.offer(new int[]{row, column});
                    visited[row][column] = true;

                    int size = 0;  // 连通块大小

                    while (!queue.isEmpty()) {
                        int[] current = queue.poll();
                        int currentRow = current[0];
                        int currentColumn = current[1];
                        size++;  // 出队时计数

                        int[][] next = {
                                {currentRow - 1, currentColumn},
                                {currentRow + 1, currentColumn},
                                {currentRow, currentColumn - 1},
                                {currentRow, currentColumn + 1}
                        };

                        for (int[] cell : next) {
                            if (cell[0] >= 0 && cell[0] < rows
                                    && cell[1] >= 0 && cell[1] < cols
                                    && !visited[cell[0]][cell[1]]
                                    && grid[cell[0]][cell[1]] > 0) {

                                visited[cell[0]][cell[1]] = true;
                                queue.offer(new int[]{cell[0], cell[1]});
                            }
                        }
                    }

                    result = Math.max(result, size);
                }

            return result;
        }
    }



}
