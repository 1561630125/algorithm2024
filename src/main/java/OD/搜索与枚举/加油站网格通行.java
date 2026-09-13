package OD.搜索与枚举;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-12 17:03
 */
public class 加油站网格通行 {


    class Solution {

        // 把 grid、行列数提为成员变量，方便 canReach 等辅助方法直接访问
        private int[][] grid;
        private int rows;
        private int columns;

        /**
         * 求从左上角 (0,0) 走到右下角 (rows-1, cols-1) 所需的【最小初始燃料】。
         *
         * 网格含义（据代码推断）:
         *   grid[r][c] == 0   → 障碍，不可走
         *   grid[r][c] == -1  → 加油站，经过时燃料补满为 100
         *   grid[r][c] >  0   → 经过时需要消耗这么多燃料
         *
         * 思路:
         *   1. 燃料上限是 100，答案一定在 [0, 100] 之间。
         *   2. "初始燃料 x 能否到达" 具有单调性:
         *        x 越大越容易到达 → 若 x 可行，则 x+1 也可行。
         *      因此可以【二分答案】找最小可行初始燃料。
         *   3. 对每个候选值，用 BFS（带"最大剩余燃料"剪枝）判断是否可达。
         *
         * @param grid 网格
         * @return 最小初始燃料；非法输入或不可达返回 -1
         */
        int minimumInitialFuel(int[][] grid) {

            // ---------- 1. 输入合法性校验 ----------
            if (grid.length == 0 || grid[0].length == 0
                    || grid.length > 200 || grid[0].length > 200)
                return -1;

            rows = grid.length;
            columns = grid[0].length;

            // 每行长度必须一致（矩形）
            for (int[] row : grid)
                if (row.length != columns)
                    return -1;

            this.grid = grid;

            // ---------- 2. 先判断"燃料给满 100"是否可达 ----------
            // 若满燃料都到不了，说明无论如何都不可达，直接返回 -1
            if (!canReach(100))
                return -1;

            // ---------- 3. 二分答案: 在 [0, 100] 中找最小的可行初始燃料 ----------
            int low = 0, high = 100;
            while (low < high) {
                int middle = (low + high) / 2;
                if (canReach(middle))
                    high = middle;        // middle 可行，尝试更小
                else
                    low = middle + 1;     // middle 不可行，必须更大
            }
            return low;   // 循环结束时 low == high，即最小可行值
        }

        /**
         * 判断以 initial 作为初始燃料，能否从 (0,0) 走到 (rows-1, cols-1)。
         *
         * 采用 BFS 遍历，但状态是"到达某格时剩余的最大燃料"。
         * 用 best[r][c] 记录到达 (r,c) 时曾经达到过的最大剩余燃料:
         *   - 只有"剩余燃料更大"时才值得再次入队，否则是重复无意义状态。
         *
         * @param initial 初始燃料
         * @return 能否到达终点
         */
        private boolean canReach(int initial) {

            // 起点或终点是障碍 → 直接不可达
            if (grid[0][0] == 0 || grid[rows - 1][columns - 1] == 0)
                return false;

            // 起点剩余燃料:
            //   起点是加油站(-1) → 直接补满 100
            //   否则             → initial 减去起点消耗 grid[0][0]
            int fuel = grid[0][0] == -1 ? 100 : initial - grid[0][0];
            if (fuel < 0)
                return false;   // 初始燃料连起点都过不去

            // best[r][c] = 到达该格时曾经拥有的最大剩余燃料，-1 表示未访问
            int[][] best = new int[rows][columns];
            for (int[] row : best)
                java.util.Arrays.fill(row, -1);
            best[0][0] = fuel;

            // BFS 队列，存格子的一维编号 row * columns + column
            java.util.ArrayDeque<Integer> queue = new java.util.ArrayDeque<>();
            queue.add(0);

            // 方向数组技巧: {-1,0,1,0,-1}
            //   第 d 步: row += directions[d], column += directions[d+1]
            //   即依次表示 上、右、下、左
            int[] directions = {-1, 0, 1, 0, -1};

            while (!queue.isEmpty()) {
                int cellIndex = queue.remove();
                int row = cellIndex / columns;
                int column = cellIndex % columns;

                // 尝试向四个方向走
                for (int direction = 0; direction < 4; direction++) {
                    int nextRow = row + directions[direction];
                    int nextColumn = column + directions[direction + 1];

                    // 越界 或 目标格是障碍 → 跳过
                    if (nextRow < 0 || nextRow >= rows
                            || nextColumn < 0 || nextColumn >= columns
                            || grid[nextRow][nextColumn] == 0)
                        continue;

                    int cell = grid[nextRow][nextColumn];

                    // 到达下一格后的剩余燃料:
                    //   下一格是加油站(-1) → 补满 100
                    //   否则              → 当前剩余燃料减去该格消耗
                    int nextFuel = cell == -1
                            ? 100
                            : best[row][column] - cell;

                    // 只有当剩余燃料 >= 0（没走死）且比之前记录的更大时，
                    // 才更新并入队（避免重复处理更差的状态）
                    if (nextFuel >= 0 && nextFuel > best[nextRow][nextColumn]) {
                        best[nextRow][nextColumn] = nextFuel;
                        queue.add(nextRow * columns + nextColumn);
                    }
                }
            }

            // 终点被访问过（best >= 0）说明可达
            return best[rows - 1][columns - 1] >= 0;
        }
    }


}
