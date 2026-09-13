package OD.数据结构与区间;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-12 18:21
 */
public class 方阵连线 {

    class Solution {

        /**
         * 统计网格中"连续的 M（男性）"的最长长度。
         *
         * 网格含义（据代码推断）：
         *   "M" → 男性
         *   其他 → 非男性（不参与连续计数）
         *
         * 连续性：沿某个方向**连续相邻**的 M 算作一段。
         * 考虑的 4 个方向：·
         *   右 (0,1)、下 (1,0)、右下 (1,1)、左下 (1,-1)
         *
         * 对每个 M 格子，沿 4 个方向各延伸一次，统计最长连续 M 的长度。
         *
         * @param grid        网格，每行是字符串数组（行长可能不一致）
         * @param columnCount 考虑的最大列数
         * @return 最长的连续 M 长度
         */
        long maximumConnectedMaleCount(String[][] grid, int columnCount) {

            // 空网格 → 长度为 0
            if (grid.length == 0)
                return 0;

            int answer = 0;

            // 用第 0 行的长度作为初始宽度
            // 注意：下面实际访问时会用每行的 grid[row].length 再做检查
            int width = grid[0].length;

            // 四个方向：右、下、右下、左下
            // （为什么只选这 4 个方向？见下方"关键注释"）
            int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

            // ---------- 遍历每个格子 ----------
            for (int row = 0; row < grid.length; row++) {
                for (int column = 0; column < Math.max(columnCount, 0); column++) {

                    // 列超出当前行实际长度，或该格不是 "M" → 跳过
                    if (column >= grid[row].length
                            || !grid[row][column].equals("M"))
                        continue;

                    // ---------- 从该 M 格子出发，沿 4 个方向各延伸一次 ----------
                    for (int[] direction : directions) {

                        int length = 0;
                        int nextRow = row;
                        int nextColumn = column;

                        // 沿 direction 一直走，直到越界或遇到非 M
                        while (nextRow >= 0 && nextRow < grid.length
                                && nextColumn >= 0 && nextColumn < width
                                && nextColumn < grid[nextRow].length
                                && grid[nextRow][nextColumn].equals("M")) {

                            length++;
                            nextRow += direction[0];
                            nextColumn += direction[1];
                        }

                        // 更新最长长度
                        answer = Math.max(answer, length);
                    }
                }
            }

            return answer;
        }
    }



    class Solution2 {

        /**
         * 求网格中"沿某直线方向连续 M"的最长长度。
         *
         * 方向：右、下、右下、左下（4 个互补方向，覆盖全部 8 个方向）。
         *
         * 这里用 Queue 模拟"沿方向逐格前进"：
         *   把起点入队，每次出队一个格子，计数 +1，
         *   并把"沿该方向的下一格"入队，直到越界或非 M。
         *
         * @param grid        网格
         * @param columnCount 考虑的最大列数
         * @return 最长连续 M 长度
         */
        long maximumConnectedMaleCount(String[][] grid, int columnCount) {

            if (grid.length == 0)
                return 0;

            int rows = grid.length;
            int columns = Math.max(columnCount, 0);

            long answer = 0;

            int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

            // ---------- 遍历每个 M 格子作为起点 ----------
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {

                    if (column >= grid[row].length
                            || !grid[row][column].equals("M"))
                        continue;

                    // ---------- 对 4 个方向，各用 Queue 走一条直线 ----------
                    for (int[] direction : directions) {

                        Queue<int[]> queue = new ArrayDeque<>();
                        queue.add(new int[]{row, column});

                        int length = 0;

                        while (!queue.isEmpty()) {
                            int[] cell = queue.remove();
                            int r = cell[0], c = cell[1];

                            // 越界检查
                            if (r < 0 || r >= rows || c < 0 || c >= columns)
                                break;
                            if (c >= grid[r].length || !grid[r][c].equals("M"))
                                break;

                            length++;

                            // 沿当前方向，把下一格入队
                            queue.add(new int[]{
                                    r + direction[0],
                                    c + direction[1]
                            });
                        }

                        answer = Math.max(answer, length);
                    }
                }
            }

            return answer;
        }
    }

}
