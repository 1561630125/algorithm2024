package OD.搜索与枚举;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 16:11
 */
public class 宜居区扩散 {
    /**
     * 多源 BFS：从所有 "YES" 格子同时扩散，每天把相邻的 "NO" 变成 "YES"
     * 求把所有 "NO" 变成 "YES" 所需的最少天数
     *
     * @param grid 二维字符串网格，"YES" 表示已宜居，"NO" 表示待宜居
     * @return 最少天数；无解返回 -1；无需转换返回 0
     */
    int minimumHabitableDays(String[][] grid) {

        // 空值检查
        if (grid == null || grid.length == 0
                || grid[0] == null || grid[0].length == 0)
            return -1;

        int columns = grid[0].length;

        // 复制一份网格，避免修改原数组
        String[][] values = new String[grid.length][columns];

        // 多源 BFS 队列，每个元素是 {row, column, 当前天数}
        java.util.ArrayDeque<int[]> queue = new java.util.ArrayDeque<>();

        int remaining = 0;  // 还没变成 "YES" 的 "NO" 格子数

        // 1. 初始化：所有 "YES" 入队（天数 0），统计 "NO" 数量
        for (int row = 0; row < grid.length; row++) {
            if (grid[row] == null || grid[row].length != columns)
                return -1;  // 行非法
            values[row] = java.util.Arrays.copyOf(grid[row], columns);

            for (int column = 0; column < columns; column++) {
                if ("YES".equals(values[row][column]))
                    queue.add(new int[]{row, column, 0});  // 源，天数 0
                else if ("NO".equals(values[row][column]))
                    remaining++;  // 待转换
            }
        }

        // 2. 特判
        if (remaining == 0) return 0;    // 没有 "NO"，0 天
        if (queue.isEmpty()) return -1;  // 没有 "YES"，无法扩散

        int days = 0;

        // 四连通方向：上、下、左、右
        int[] rowChange = {-1, 1, 0, 0};
        int[] columnChange = {0, 0, -1, 1};

        // 3. 多源 BFS
        while (!queue.isEmpty()) {
            int[] current = queue.remove();

            for (int direction = 0; direction < 4; direction++) {
                int nextRow = current[0] + rowChange[direction];
                int nextColumn = current[1] + columnChange[direction];

                // 越界 或 不是 "NO"，跳过
                if (nextRow < 0 || nextRow >= values.length
                        || nextColumn < 0 || nextColumn >= columns
                        || !"NO".equals(values[nextRow][nextColumn]))
                    continue;

                // 把 "NO" 变成 "YES"
                values[nextRow][nextColumn] = "YES";
                remaining--;

                // 天数 = 当前格子天数 + 1
                days = current[2] + 1;

                // 新格子入队，带上天数
                queue.add(new int[]{nextRow, nextColumn, current[2] + 1});
            }
        }

        // 4. 如果所有 "NO" 都转换完，返回天数；否则有无法到达的，返回 -1
        return remaining == 0 ? days : -1;
    }


    int minimumHabitableDays2(String[][] grid) {
        if (grid == null || grid.length == 0
                || grid[0] == null || grid[0].length == 0)
            return -1;

        int rows = grid.length;
        int columns = grid[0].length;

        // 复制网格
        String[][] values = new String[rows][columns];
        boolean[][] vis = new boolean[rows][columns];  // 访问标记

        java.util.ArrayDeque<int[]> queue = new java.util.ArrayDeque<>();
        int remaining = 0;

        // 初始化
        for (int row = 0; row < rows; row++) {
            if (grid[row] == null || grid[row].length != columns)
                return -1;
            values[row] = java.util.Arrays.copyOf(grid[row], columns);

            for (int column = 0; column < columns; column++) {
                if ("YES".equals(values[row][column])) {
                    queue.add(new int[]{row, column, 0});
                    vis[row][column] = true;   // 源标记已访问
                } else if ("NO".equals(values[row][column]))
                    remaining++;
            }
        }

        if (remaining == 0) return 0;
        if (queue.isEmpty()) return -1;

        int days = 0;
        int[] rowChange = {-1, 1, 0, 0};
        int[] columnChange = {0, 0, -1, 1};

        while (!queue.isEmpty()) {
            int[] current = queue.remove();

            for (int direction = 0; direction < 4; direction++) {
                int nextRow = current[0] + rowChange[direction];
                int nextColumn = current[1] + columnChange[direction];

                if (nextRow < 0 || nextRow >= rows
                        || nextColumn < 0 || nextColumn >= columns)
                    continue;

                // 用 vis 判断是否访问过
                if (vis[nextRow][nextColumn])
                    continue;

                // 只处理 "NO"
                if (!"NO".equals(values[nextRow][nextColumn]))
                    continue;

                vis[nextRow][nextColumn] = true;   // 标记访问
                values[nextRow][nextColumn] = "YES"; // 可选：改状态
                remaining--;
                days = current[2] + 1;
                queue.add(new int[]{nextRow, nextColumn, current[2] + 1});
            }
        }

        return remaining == 0 ? days : -1;
    }

}
