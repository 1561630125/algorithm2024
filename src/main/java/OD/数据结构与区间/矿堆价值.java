package OD.数据结构与区间;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-12 22:28
 */
public class 矿堆价值 {


    class Solution {

        /**
         * 求网格中"四方向连通的非 0 数字块"的最大元素之和。
         *
         * 网格含义：
         *   rows[r].charAt(c) == '0'  → 空白，不算入任何块
         *   rows[r].charAt(c) != '0'  → 矿藏，数字字符 '1'~'9'，累加其数值
         *
         * 连通性：上下左右四方向相邻的"非 0"格子属于同一块。
         *
         * 算法：BFS 遍历每个连通块，累加块内数字之和，取最大值。
         *
         * @param rows 网格，每行一个字符串
         * @return 最大的连通块元素之和
         */
        long maximumOrePileValue(String[] rows) {

            // 空网格 → 0
            if (rows.length == 0)
                return 0;

            int height = rows.length;
            int width = rows[0].length();   // 假设各行等长
            int answer = 0;

            // visited[r][c] 标记该格是否已被访问
            boolean[][] visited = new boolean[height][width];

            // 手写数组队列，存格子的一维编号
            int[] queue = new int[height * width];

            // 四方向：下、上、右、左
            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

            // ---------- 遍历每个格子作为 BFS 起点 ----------
            for (int row = 0; row < height; row++) {
                for (int column = 0; column < width; column++) {

                    // 是 '0'（空白）或已访问 → 跳过
                    if (rows[row].charAt(column) == '0' || visited[row][column])
                        continue;

                    // ---------- 开始 BFS，探索整个连通块 ----------
                    int head = 0, tail = 1;   // 手写队列的头尾指针
                    int value = 0;            // 当前块的数字之和

                    queue[0] = row * width + column;
                    visited[row][column] = true;

                    while (head < tail) {
                        // 出队：还原行列
                        int index = queue[head++];
                        int currentRow = index / width;
                        int currentColumn = index % width;

                        // 累加当前格子的数字值：'3' - '0' = 3
                        value += rows[currentRow].charAt(currentColumn) - '0';

                        // 向四方向扩展
                        for (int[] direction : directions) {
                            int nextRow = currentRow + direction[0];
                            int nextColumn = currentColumn + direction[1];

                            // 边界检查 + 非 '0' + 未访问
                            if (nextRow >= 0 && nextRow < height
                                    && nextColumn >= 0 && nextColumn < width
                                    && rows[nextRow].charAt(nextColumn) != '0'
                                    && !visited[nextRow][nextColumn]) {

                                visited[nextRow][nextColumn] = true;   // 入队即标记
                                queue[tail++] = nextRow * width + nextColumn;
                            }
                        }
                    }

                    // ---------- 结算当前块 ----------
                    answer = Math.max(answer, value);
                }
            }

            return answer;
        }
    }

    class Solution3 {

        /**
         * 求网格中"四方向连通的非 0 数字块"的最大元素之和。
         *
         * 网格含义：
         *   rows[r].charAt(c) == '0'  → 空白
         *   rows[r].charAt(c) != '0'  → 矿藏，累加其数字值
         *
         * 连通性：上下左右四方向相邻的非 0 格子属于同一块。
         *
         * 算法：BFS 遍历每个连通块，累加块内数字之和，取最大值。
         *
         * @param rows 网格，每行一个字符串
         * @return 最大的连通块元素之和
         */
        long maximumOrePileValue(String[] rows) {

            // 空网格 → 0
            if (rows.length == 0)
                return 0;

            int height = rows.length;
            int width = rows[0].length();
            long answer = 0;

            boolean[][] visited = new boolean[height][width];

            // 四方向：下、上、右、左
            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

            // ---------- 遍历每个格子作为 BFS 起点 ----------
            for (int row = 0; row < height; row++) {
                for (int column = 0; column < width; column++) {

                    // 是 '0'（空白）或已访问 → 跳过
                    if (rows[row].charAt(column) == '0' || visited[row][column])
                        continue;

                    // ---------- 用 Queue 做 BFS ----------
                    Queue<Integer> queue = new ArrayDeque<>();
                    long value = 0;

                    queue.add(row * width + column);
                    visited[row][column] = true;

                    while (!queue.isEmpty()) {
                        // 出队，还原行列
                        int index = queue.remove();
                        int currentRow = index / width;
                        int currentColumn = index % width;

                        // 累加当前格子的数字值
                        value += rows[currentRow].charAt(currentColumn) - '0';

                        // 四方向扩展
                        for (int[] direction : directions) {
                            int nextRow = currentRow + direction[0];
                            int nextColumn = currentColumn + direction[1];

                            // 边界 + 非 '0' + 未访问
                            if (nextRow >= 0 && nextRow < height
                                    && nextColumn >= 0 && nextColumn < width
                                    && rows[nextRow].charAt(nextColumn) != '0'
                                    && !visited[nextRow][nextColumn]) {

                                visited[nextRow][nextColumn] = true;   // 入队即标记
                                queue.add(nextRow * width + nextColumn);
                            }
                        }
                    }

                    // 结算当前块
                    answer = Math.max(answer, value);
                }
            }

            return answer;
        }
    }

}
