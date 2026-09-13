package OD.数据结构与区间;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-12 18:09
 */
public class 区域敌情 {
    class Solution {

        /**
         * 统计网格中"敌人数量小于 threshold"的连通区域（区域）个数。
         *
         * 网格含义（据代码推断）：
         *   'E' → 敌人
         *   '#' → 障碍，不可通行
         *   其他字符（如 '.'）→ 空地，可通行
         *
         * 连通性：上下左右四方向相邻的非障碍格子属于同一区域。
         *
         * 对每个连通区域，统计其中的 'E' 个数 enemies：
         *   若 enemies < threshold，则计入答案。
         *
         * @param grid        网格，每行是字符串（行长可能不一致）
         * @param columnCount 考虑的最大列数（可能大于某些行的实际长度）
         * @param threshold   敌人数量阈值
         * @return 满足条件的连通区域个数
         */
        long countEnemyRegions(String[] grid, int columnCount, long threshold) {

            int rows = grid.length;
            int columns = Math.max(columnCount, 0);   // 列数不能为负

            // visited[row][column] 标记该格是否已被访问过
            boolean[][] visited = new boolean[rows][columns];

            long answer = 0;   // 满足条件的区域计数

            // 四方向位移：上、下、左、右
            int[] dr = {-1, 1, 0, 0};
            int[] dc = {0, 0, -1, 1};

            // ---------- 遍历每个格子作为 BFS 起点 ----------
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {

                    // 已访问 / 该列超出当前行实际长度 / 是障碍 → 跳过
                    if (visited[row][column]
                            || column >= grid[row].length()
                            || grid[row].charAt(column) == '#')
                        continue;

                    // ---------- 开始一次 BFS，探索整个连通区域 ----------

                    // 手写队列（数组实现，比 ArrayDeque 更快）
                    // 最大容量 = 格子总数，保证不会溢出
                    int[] queue = new int[Math.max(rows * columns, 1)];
                    int head = 0, tail = 0;

                    int enemies = 0;   // 当前区域内的敌人数

                    // 起点入队
                    queue[tail++] = row * columns + column;
                    visited[row][column] = true;

                    // ---------- BFS 主循环 ----------
                    while (head < tail) {
                        int position = queue[head++];

                        // 一维编号还原为行列
                        int currentRow = position / columns;
                        int currentColumn = position % columns;

                        // 统计敌人
                        if (grid[currentRow].charAt(currentColumn) == 'E')
                            enemies++;

                        // 向四个方向扩展
                        for (int direction = 0; direction < 4; direction++) {
                            int nr = currentRow + dr[direction];
                            int nc = currentColumn + dc[direction];

                            // 边界检查 + 未访问 + 在当前行实际长度内 + 非障碍
                            if (nr >= 0 && nr < rows
                                    && nc >= 0 && nc < columns
                                    && !visited[nr][nc]
                                    && nc < grid[nr].length()
                                    && grid[nr].charAt(nc) != '#') {

                                visited[nr][nc] = true;
                                queue[tail++] = nr * columns + nc;
                            }
                        }
                    }

                    // ---------- 结算当前区域 ----------
                    // 敌人数量严格小于 threshold 才计入
                    if (enemies < threshold)
                        answer++;
                }
            }

            return answer;
        }
    }



    class Solution2 {

        /**
         * 统计网格中"敌人数量小于 threshold"的连通区域个数。
         *
         * 网格含义：
         *   'E' → 敌人
         *   '#' → 障碍，不可通行
         *   其他 → 空地，可通行
         *
         * 四方向连通；每行长度可能不同（锯齿网格）。
         *
         * @param grid        网格
         * @param columnCount 考虑的最大列数
         * @param threshold   敌人数量阈值
         * @return 满足条件的连通区域个数
         */
        long countEnemyRegions(String[] grid, int columnCount, long threshold) {

            int rows = grid.length;
            int columns = Math.max(columnCount, 0);

            boolean[][] visited = new boolean[rows][columns];
            long answer = 0;

            int[] dr = {-1, 1, 0, 0};
            int[] dc = {0, 0, -1, 1};

            // ---------- 遍历每个格子作为 BFS 起点 ----------
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {

                    // 已访问 / 超出当前行实际长度 / 障碍 → 跳过
                    if (visited[row][column]
                            || column >= grid[row].length()
                            || grid[row].charAt(column) == '#')
                        continue;

                    // ---------- 用 Queue 做 BFS ----------
                    Queue<Integer> queue = new ArrayDeque<>();
                    int enemies = 0;

                    queue.add(row * columns + column);
                    visited[row][column] = true;

                    while (!queue.isEmpty()) {
                        int position = queue.remove();
                        int currentRow = position / columns;
                        int currentColumn = position % columns;

                        // 统计敌人
                        if (grid[currentRow].charAt(currentColumn) == 'E')
                            enemies++;

                        // 四方向扩展
                        for (int direction = 0; direction < 4; direction++) {
                            int nr = currentRow + dr[direction];
                            int nc = currentColumn + dc[direction];

                            if (nr >= 0 && nr < rows
                                    && nc >= 0 && nc < columns
                                    && !visited[nr][nc]
                                    && nc < grid[nr].length()
                                    && grid[nr].charAt(nc) != '#') {

                                visited[nr][nc] = true;          // 入队即标记
                                queue.add(nr * columns + nc);
                            }
                        }
                    }

                    // 结算当前区域
                    if (enemies < threshold)
                        answer++;
                }
            }

            return answer;
        }
    }

}
