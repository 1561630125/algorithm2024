package OD.数据结构与区间;

import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 考点：多源BFS
 *
 * @author faming.yang@hand-china.com 2026-09-12 22:10
 */
public class 信号递减 {

    class Solution {

        /**
         * 计算目标格子 (targetRow, targetColumn) 处的信号强度。
         *
         * 网格含义（据代码推断）：
         *   grid[r][c] > 0  → 信号源，值为初始信号强度
         *   grid[r][c] == 0 → 空白，信号可以传播进来
         *   其他值（< 0）   → 障碍，信号不能通过（也不会被赋值）
         *
         * 传播规则：
         *   信号从所有信号源同时向上下左右扩散，每走一格强度减 1。
         *   一个格子只能被"赋值一次"（首次到达即确定），之后不再更新。
         *   当信号强度降到 1 时，不再继续往外传播。
         *
         * 算法：多源 BFS（所有信号源一起入队）。
         *
         * @param grid         网格
         * @param targetRow    目标行
         * @param targetColumn 目标列
         * @return 目标格子的信号强度；若从未被信号到达则返回 0
         */
        long signalStrengthAt(long[][] grid, int targetRow, int targetColumn) {

            // ---------- 1. 空网格处理 ----------
            if (grid.length == 0 || grid[0].length == 0)
                return 0;

            int rows = grid.length;
            int columns = grid[0].length;

            // cells 是 grid 的副本，用于在传播过程中修改
            // （不改原 grid，避免副作用）
            long[][] cells = new long[rows][columns];

            // 手写数组队列，存格子的一维编号
            int[] queue = new int[rows * columns];
            int head = 0, tail = 0;

            // ---------- 2. 初始化：把所有信号源入队 ----------
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    cells[row][column] = grid[row][column];

                    // 信号源（值 > 0）全部入队
                    if (cells[row][column] > 0)
                        queue[tail++] = row * columns + column;
                }
            }

            // 四方向：上、下、左、右
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

            // ---------- 3. 多源 BFS 主循环 ----------
            while (head < tail) {

                // 出队：还原行列
                int index = queue[head++];
                int row = index / columns;
                int column = index % columns;

                // 强度为 1 时不再向外传播（再传就变 0 了）
                if (cells[row][column] == 1)
                    continue;

                // 向四方向尝试传播
                for (int[] direction : directions) {
                    int nextRow = row + direction[0];
                    int nextColumn = column + direction[1];

                    // 边界检查 + 只传播到"还没被赋值"的空白格（值为 0）
                    if (nextRow >= 0 && nextRow < rows
                            && nextColumn >= 0 && nextColumn < columns
                            && cells[nextRow][nextColumn] == 0) {

                        // 信号强度减 1 后写入，并入队继续传播
                        cells[nextRow][nextColumn] = cells[row][column] - 1;
                        queue[tail++] = nextRow * columns + nextColumn;
                    }
                }
            }

            // ---------- 4. 返回目标格子的值 ----------
            int targetIndex = targetRow * columns + targetColumn;
            return cells[targetIndex / columns][targetIndex % columns];
        }
    }

    class Solution2 {

        /**
         * 计算目标格子 (targetRow, targetColumn) 处的信号强度。
         *
         * 网格含义：
         *   grid[r][c] > 0  → 信号源，值为初始强度
         *   grid[r][c] == 0 → 空白，信号可传播进来
         *   grid[r][c] < 0  → 障碍，信号不可通过
         *
         * 传播规则：
         *   所有信号源同时向上下左右扩散，每走一格强度减 1。
         *   每格只被赋值一次（首次到达即最近源）。
         *   强度为 1 时不再继续传播。
         *
         * 算法：多源 BFS（所有源一起入队）。
         *
         * @param grid         网格
         * @param targetRow    目标行
         * @param targetColumn 目标列
         * @return 目标格子的信号强度；从未被到达返回 0
         */
        long signalStrengthAt(long[][] grid, int targetRow, int targetColumn) {

            // ---------- 1. 空网格处理 ----------
            if (grid.length == 0 || grid[0].length == 0)
                return 0;

            int rows = grid.length;
            int columns = grid[0].length;

            // 目标坐标越界检查
            if (targetRow < 0 || targetRow >= rows
                    || targetColumn < 0 || targetColumn >= columns)
                return 0;

            // ---------- 2. cells 副本 + 多源入队 ----------
            long[][] cells = new long[rows][columns];

            // 用 Queue 代替手写数组队列
            Queue<Integer> queue = new ArrayDeque<>();

            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    cells[row][column] = grid[row][column];

                    // 所有信号源入队
                    if (cells[row][column] > 0)
                        queue.add(row * columns + column);
                }
            }

            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

            // ---------- 3. 多源 BFS 主循环 ----------
            while (!queue.isEmpty()) {

                // 出队，还原行列
                int index = queue.remove();
                int row = index / columns;
                int column = index % columns;

                // 强度为 1 时停止向外传播
                if (cells[row][column] == 1)
                    continue;

                // 四方向传播
                for (int[] direction : directions) {
                    int nextRow = row + direction[0];
                    int nextColumn = column + direction[1];

                    // 边界检查 + 只传播到未访问的空白格（值为 0）
                    if (nextRow >= 0 && nextRow < rows
                            && nextColumn >= 0 && nextColumn < columns
                            && cells[nextRow][nextColumn] == 0) {

                        cells[nextRow][nextColumn] = cells[row][column] - 1;
                        queue.add(nextRow * columns + nextColumn);
                    }
                }
            }

            // ---------- 4. 返回目标格子的值 ----------
            return cells[targetRow][targetColumn];
        }
    }



    class Solution3 {

        /**
         * 求每个格子的信号强度，规则：
         *   所有源同时传播，每走一格强度减 1；
         *   若多个源都能到达某格，取"传播过来强度最大"的那个。
         *
         * 用多源 Dijkstra（优先队列按强度从大到小）：
         *   每次取出当前强度最大的格子，用它去更新邻居。
         *   邻居强度 = 当前强度 - 1。
         *   只有"新强度 > 已记录强度"时才更新并入队。
         *
         * 网格含义：
         *   grid[r][c] > 0  → 源，初始强度
         *   grid[r][c] == 0 → 空白
         *   grid[r][c] < 0  → 障碍，不可通过
         *
         * @param grid         网格
         * @param targetRow    目标行
         * @param targetColumn 目标列
         * @return 目标格子的最大信号强度；不可达返回 0
         */
        long signalStrengthAt(long[][] grid, int targetRow, int targetColumn) {

            if (grid.length == 0 || grid[0].length == 0)
                return 0;

            int rows = grid.length;
            int columns = grid[0].length;

            if (targetRow < 0 || targetRow >= rows
                    || targetColumn < 0 || targetColumn >= columns)
                return 0;

            // best[r][c] = 到达该格时能获得的【最大信号强度】，-1 表示未到达
            long[][] best = new long[rows][columns];
            for (long[] row : best)
                java.util.Arrays.fill(row, -1);

            // 优先队列：按强度【从大到小】排序
            //   元素: {强度, 行, 列}
            PriorityQueue<long[]> pq = new PriorityQueue<>(
                    (a, b) -> Long.compare(b[0], a[0]));   // ← 注意 b, a：降序

            // ---------- 1. 所有源入队 ----------
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    if (grid[r][c] > 0) {
                        best[r][c] = grid[r][c];
                        pq.add(new long[]{grid[r][c], r, c});
                    }
                }
            }

            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

            // ---------- 2. Dijkstra 主循环 ----------
            while (!pq.isEmpty()) {
                long[] item = pq.remove();
                long strength = item[0];
                int row = (int) item[1];
                int column = (int) item[2];

                // 懒惰删除：堆里可能是过期的旧值
                if (strength != best[row][column])
                    continue;

                // 强度为 1 时再传就变 0，停止
                if (strength <= 1)
                    continue;

                // 向四方向传播
                for (int[] direction : directions) {
                    int nr = row + direction[0];
                    int nc = column + direction[1];

                    // 边界 + 非障碍
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= columns)
                        continue;
                    if (grid[nr][nc] < 0)     // 障碍
                        continue;

                    long nextStrength = strength - 1;

                    // 只有"更强"才更新
                    if (nextStrength > best[nr][nc]) {
                        best[nr][nc] = nextStrength;
                        pq.add(new long[]{nextStrength, nr, nc});
                    }
                }
            }

            // ---------- 3. 返回目标格子的最大强度 ----------
            return Math.max(best[targetRow][targetColumn], 0);
        }
    }

}
