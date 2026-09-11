package OD.搜索与枚举;

import java.util.ArrayDeque;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 21:33
 */
public class 机器人最大活动区 {

    int largestRobotActivityArea(int[][] grid) {

        int rowLength = grid.length;
        int colLength = grid[0].length;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        ArrayDeque<int[]> queue = new ArrayDeque<>();
        int[][] vis = new int[rowLength][];

        int res = -1;
        for(int i = 0; i < rowLength; i++) {
            for(int j = 0; j < colLength; j++) {

                queue.offer(new int[]{i,j});
                vis[i][j] = 1;
                int size = 0;
                while (!queue.isEmpty()) {
                    int[] cur = queue.poll();
                    size++;
                    for(int k = 0; k < directions.length; k++) {
                        int nextRow = cur[0] + directions[k][0];
                        int nextCol = cur[1] + directions[k][1];

                        if (nextRow>= 0 && nextRow < rowLength &&
                                nextCol>= 0 && nextCol < colLength &&
                                Math.abs(grid[i][j]-grid[nextRow][nextCol]) <= 1 &&
                                vis[nextRow][nextCol] != 1){
                            queue.offer(new int[]{nextRow,nextCol});
                            vis[nextRow][nextCol] = 1;
                        }
                    }
                }

                res = Math.max(res, size);
            }
        }

        return res;
    }


    int largestRobotActivityArea2(int[][] grid) {
        // 空值检查
        if (grid == null || grid.length == 0 || grid[0] == null || grid[0].length == 0)
            return 0;

        int rows = grid.length, columns = grid[0].length;

        // 校验每行长度一致
        for (int[] row : grid)
            if (row == null || row.length != columns)
                return 0;

        // seen 标记是否访问过
        boolean[][] seen = new boolean[rows][columns];

        // 一维数组模拟 BFS 队列，存格子编号 row * columns + column
        int[] queue = new int[rows * columns];

        int best = 0;  // 最大连通块面积

        // 四连通方向：上、下、左、右
        int[] dr = {-1, 1, 0, 0}, dc = {0, 0, -1, 1};

        // 遍历每个格子作为 BFS 起点
        for (int sr = 0; sr < rows; sr++)
            for (int sc = 0; sc < columns; sc++) {

                // 已访问过就跳过
                if (seen[sr][sc])
                    continue;

                // 新连通块，初始化队列
                int head = 0, tail = 0;
                queue[tail++] = sr * columns + sc;
                seen[sr][sc] = true;

                while (head < tail) {
                    // 出队，编号还原成坐标
                    int cell = queue[head++];
                    int row = cell / columns;
                    int column = cell % columns;

                    // 向四个方向扩展
                    for (int direction = 0; direction < 4; direction++) {
                        int nr = row + dr[direction];
                        int nc = column + dc[direction];

                        // 越界、已访问、高度差 > 1 都跳过
                        if (nr < 0 || nr >= rows || nc < 0 || nc >= columns
                                || seen[nr][nc]
                                || Math.abs((long) grid[row][column] - grid[nr][nc]) > 1)
                            continue;

                        seen[nr][nc] = true;
                        queue[tail++] = nr * columns + nc;
                    }
                }

                // tail 就是本连通块的格子数
                best = Math.max(best, tail);
            }

        return best;
    }

    int largestRobotActivityArea3(int[][] grid) {
        int rowLength = grid.length;
        int colLength = grid[0].length;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        boolean[][] vis = new boolean[rowLength][colLength];  // 修复1：完整初始化
        int res = 0;                                          // 修复2：初始 0

        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < colLength; j++) {

                if (vis[i][j]) continue;                      // 修复4：跳过已访问起点

                ArrayDeque<int[]> queue = new ArrayDeque<>();
                queue.offer(new int[]{i, j});
                vis[i][j] = true;
                int size = 0;

                while (!queue.isEmpty()) {
                    int[] cur = queue.poll();
                    size++;

                    for (int k = 0; k < directions.length; k++) {
                        int nextRow = cur[0] + directions[k][0];
                        int nextCol = cur[1] + directions[k][1];

                        if (nextRow >= 0 && nextRow < rowLength &&
                                nextCol >= 0 && nextCol < colLength &&
                                Math.abs(grid[cur[0]][cur[1]]  // 修复3：用当前格高度
                                        - grid[nextRow][nextCol]) <= 1 &&
                                !vis[nextRow][nextCol]) {

                            queue.offer(new int[]{nextRow, nextCol});
                            vis[nextRow][nextCol] = true;
                        }
                    }
                }

                res = Math.max(res, size);
            }
        }

        return res;
    }

}
