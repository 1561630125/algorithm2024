package OD.搜索与枚举;

import java.util.ArrayDeque;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 20:45
 */
public class 单入口空闲区 {

    String largestSingleEntranceArea(String[] grid) {
        // 行数、列数
        int rows = grid.length;
        int columns = rows == 0 ? 0 : grid[0].length();

        // 空网格直接返回 NULL
        if (rows == 0 || columns == 0)
            return "NULL";

        // 校验每行长度一致（矩形网格）
        for (String row : grid)
            if (row.length() != columns)
                return "NULL";

        // visited 标记每个格子是否已被某个连通块访问过
        boolean[][] visited = new boolean[rows][columns];

        // 记录最优连通块：
        // bestSize  最大面积
        // bestCount 达到最大面积的连通块个数（用于判断是否唯一）
        // bestRow/bestColumn 该连通块唯一的入口坐标
        int bestSize = -1, bestCount = 0, bestRow = -1, bestColumn = -1;

        // 遍历每个格子作为 BFS 起点
        for (int startRow = 0; startRow < rows; startRow++)
            for (int startColumn = 0; startColumn < columns; startColumn++) {

                // 只处理 'O' 且没访问过的格子
                if (grid[startRow].charAt(startColumn) != 'O'
                        || visited[startRow][startColumn])
                    continue;

                // BFS 队列，存坐标
                java.util.ArrayDeque<int[]> queue = new java.util.ArrayDeque<>();
                queue.add(new int[]{startRow, startColumn});
                visited[startRow][startColumn] = true;

                // 当前连通块的统计信息
                int size = 0;              // 面积（格子数）
                int entrances = 0;         // 入口数量（位于边界上的格子数）
                int entranceRow = -1;      // 入口坐标（只关心最后一个，因为后面要求 entrances == 1）
                int entranceColumn = -1;

                while (!queue.isEmpty()) {
                    int[] current = queue.remove();
                    int row = current[0], column = current[1];
                    size++;

                    // 如果当前格子在网格边界上，就算作一个入口
                    if (row == 0 || row == rows - 1
                            || column == 0 || column == columns - 1) {
                        entrances++;
                        entranceRow = row;
                        entranceColumn = column;
                    }

                    // 向四个方向扩展
                    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
                    for (int[] direction : directions) {
                        int nextRow = row + direction[0];
                        int nextColumn = column + direction[1];

                        // 越界、已访问、非 'O' 都跳过
                        if (nextRow >= 0 && nextRow < rows
                                && nextColumn >= 0 && nextColumn < columns
                                && !visited[nextRow][nextColumn]
                                && grid[nextRow].charAt(nextColumn) == 'O') {
                            visited[nextRow][nextColumn] = true;
                            queue.add(new int[]{nextRow, nextColumn});
                        }
                    }
                }

                // 只关心「恰好一个入口」的连通块
                if (entrances == 1) {
                    if (size > bestSize) {
                        // 发现更大的连通块，重置
                        bestSize = size;
                        bestCount = 1;
                        bestRow = entranceRow;
                        bestColumn = entranceColumn;
                    } else if (size == bestSize) {
                        // 面积并列最大，计数 +1（后面会据此返回不同结果）
                        bestCount++;
                    }
                }
            }

        // 没有任何「单入口」连通块
        if (bestSize < 0)
            return "NULL";

        // 最大面积有多个并列，无法确定唯一入口，只返回面积
        if (bestCount > 1)
            return Integer.toString(bestSize);

        // 唯一最大单入口连通块，返回入口坐标和面积
        return bestRow + " " + bestColumn + " " + bestSize;
    }



    String largestSingleEntranceArea2(String[] grid) {
        int rows = grid.length;
        int columns = rows == 0 ? 0 : grid[0].length();
        if (rows == 0 || columns == 0) return "NULL";
        for (String row : grid)
            if (row.length() != columns) return "NULL";

        boolean[][] visited = new boolean[rows][columns];
        int bestSize = -1, bestCount = 0, bestRow = -1, bestColumn = -1;

        // 只从边界上的 'O' 出发
        for (int startRow = 0; startRow < rows; startRow++) {
            for (int startColumn = 0; startColumn < columns; startColumn++) {

                // 只处理边界格子
                boolean onBorder = startRow == 0 || startRow == rows - 1
                        || startColumn == 0 || startColumn == columns - 1;
                if (!onBorder) continue;

                if (grid[startRow].charAt(startColumn) != 'O'
                        || visited[startRow][startColumn]) continue;

                // BFS 统计这个连通块
                java.util.ArrayDeque<int[]> queue = new java.util.ArrayDeque<>();
                queue.add(new int[]{startRow, startColumn});
                visited[startRow][startColumn] = true;

                int size = 0, entrances = 0, entranceRow = -1, entranceColumn = -1;

                while (!queue.isEmpty()) {
                    int[] current = queue.remove();
                    int row = current[0], column = current[1];
                    size++;

                    if (row == 0 || row == rows - 1
                            || column == 0 || column == columns - 1) {
                        entrances++;
                        entranceRow = row;
                        entranceColumn = column;
                    }

                    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
                    for (int[] direction : directions) {
                        int nextRow = row + direction[0];
                        int nextColumn = column + direction[1];
                        if (nextRow >= 0 && nextRow < rows
                                && nextColumn >= 0 && nextColumn < columns
                                && !visited[nextRow][nextColumn]
                                && grid[nextRow].charAt(nextColumn) == 'O') {
                            visited[nextRow][nextColumn] = true;
                            queue.add(new int[]{nextRow, nextColumn});
                        }
                    }


                    /*int[][] directions = new int[4][2];
                    int idx = 0;
                    for (int dr = -1; dr <= 1; dr++) {
                        for (int dc = -1; dc <= 1; dc++) {
                            // 跳过自己，且只保留上下左右（dr 和 dc 恰有一个为 0）
                            if (dr == 0 && dc == 0) continue;
                            if (dr != 0 && dc != 0) continue;  // 跳对角
                            directions[idx][0] = dr;
                            directions[idx][1] = dc;
                            idx++;
                        }
                    }*/
                }

                if (entrances == 1) {
                    if (size > bestSize) {
                        bestSize = size;
                        bestCount = 1;
                        bestRow = entranceRow;
                        bestColumn = entranceColumn;
                    } else if (size == bestSize) {
                        bestCount++;
                    }
                }
            }
        }

        if (bestSize < 0) return "NULL";
        if (bestCount > 1) return Integer.toString(bestSize);
        return bestRow + " " + bestColumn + " " + bestSize;
    }



    String largestSingleEntranceArea3(String[] grid) {


        int rowLength = grid.length;
        int colLength = grid[0].length();

        int bestSize = -1;
        int rol = -1;
        int col = -1;

        int[][] vis = new int[grid.length][];
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        for(int i = 0; i < rowLength; i++) {
            for(int j = 0; j < colLength; j++) {

                // 边界
                if (!(i == 0 || i == rowLength -1 || j == 0 || j == colLength - 1)) {
                    continue;
                }
                // O字符
                if (grid[i].charAt(j) != 'O') {
                    continue;
                }
                queue.offer(new int[]{i,j});
                vis[i][j] = 1;
                rol = i;
                col = j;

                int size = 0;
                while (!queue.isEmpty()) {
                    int[] poll = queue.poll();
                    size++;

                    int[][] dir = new int[][]{{-1,0},{1,0},{0,-1},{0,1}};
                    for(int k = 0; k < dir.length; k++) {
                        int nextRol = poll[0] + dir[k][0];
                        int nextCol = poll[1] + dir[k][1];

                        if (vis[nextRol][nextCol] != 1 && grid[nextRol].charAt(nextCol) == 'O') {
                            queue.offer(new int[]{nextRol,nextCol});
                            vis[nextRol][nextCol] = 1;
                        }
                    }


                }

                if (size > bestSize) {
                    bestSize = size;
                }
            }
        }

        return "NULL";
    }
}
