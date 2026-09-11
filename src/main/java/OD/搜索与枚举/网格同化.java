package OD.搜索与枚举;

import java.util.ArrayDeque;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 15:23
 */
public class 网格同化 {

    static int countNonOneAfterAssimilation(int[][] grid) {
        int rows = grid.length;
        int columns = grid[0].length;

        ArrayDeque<int[]> queue = new ArrayDeque<>();
        boolean[][] vis = new boolean[rows][columns];
        grid[0][0] = 1;
        vis[0][0] = true;
        queue.offer(new int[]{0,0});

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if (grid[i][j] == 1 && !vis[i][j]) {
                    queue.offer(new int[]{i,j});
                    vis[i][j] = true;
                }
            }
        }

        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};  // 4个方向向量
        while (!queue.isEmpty()) {
            int[] poll = queue.poll();

            for(int i = 0; i < directions.length; i++) {
                int nextRow = poll[0] + directions[i][0];
                int nextCol = poll[1] + directions[i][1];
                if (nextRow >= 0 && nextRow < rows && nextCol >= 0 && nextCol < columns
                        && !vis[nextRow][nextCol] && grid[nextRow][nextCol] == 0) {
                    grid[nextRow][nextCol] = 1;
                    queue.offer(new int[]{nextRow,nextCol});
                    vis[nextRow][nextCol] = true;
                }
            }
        }

        int ans = 0;
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if (grid[i][j] == 0 || grid[i][j] == 2) {
                    ans++;
                }
            }
        }
        return ans;
    }

    /**
     * 从网格左上角 (0,0) 出发，对值为 0 的格子做 BFS 洪水填充，全部染成 1。
     * 规则：
     *   - (0,0) 强制置为 1；
     *   - 从已染色的格子向上下左右扩散，遇到值为 0 的格子就染成 1 并入队；
     *   - 值为 1 的格子视为"墙"，不扩散。
     * 返回：填充完成后，值不等于 1 的格子总数。
     *
     * 若参数非法（null、空、行长度不一致），返回 0。
     */
    static int countNonOneAfterAssimilation2(int[][] grid) {
        // ---------- 参数校验 ----------
        if (grid == null || grid.length == 0 || grid[0] == null || grid[0].length == 0)
            return 0;
        int columns = grid[0].length;

        // ---------- 拷贝一份网格，避免修改原数组 ----------
        int[][] values = new int[grid.length][columns];
        for (int row = 0; row < grid.length; row++) {
            if (grid[row] == null || grid[row].length != columns)
                return 0; // 出现锯齿/非法行，直接返回 0
            values[row] = java.util.Arrays.copyOf(grid[row], columns);
        }

        // ---------- BFS 起点 ----------
        values[0][0] = 1; // 左上角强制置 1
        java.util.ArrayDeque<int[]> queue = new java.util.ArrayDeque<>();
        queue.add(new int[] {0, 0});

        // 四个方向：上、下、左、右
        int[] rowChange = {-1, 1, 0, 0};
        int[] columnChange = {0, 0, -1, 1};

        // ---------- 洪水填充 ----------
        while (!queue.isEmpty()) {
            int[] current = queue.remove(); // 取出当前格子
            for (int direction = 0; direction < 4; direction++) {
                int nextRow = current[0] + rowChange[direction];
                int nextColumn = current[1] + columnChange[direction];

                // 边界检查 + 只对值为 0 的格子染色（值为 1 是墙，跳过）
                if (nextRow >= 0 && nextRow < values.length
                        && nextColumn >= 0 && nextColumn < columns
                        && values[nextRow][nextColumn] == 0) {
                    values[nextRow][nextColumn] = 1;              // 染色
                    queue.add(new int[] {nextRow, nextColumn});   // 入队继续扩散
                }
            }
        }

        // ---------- 统计仍不为 1 的格子 ----------
        int count = 0;
        for (int[] row : values)
            for (int value : row)
                if (value != 1)
                    count++;
        return count;
    }


    public static void main(String[] args) {
        int[][] grid = new int[][]{{2,2,2},{2,0,1}};
        System.out.println(countNonOneAfterAssimilation2(grid));
        System.out.println(countNonOneAfterAssimilation(grid));

    }
}
