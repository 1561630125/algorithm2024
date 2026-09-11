package OD.搜索与枚举;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 21:17
 */
public class 八领域消除 {

    int countEightConnectedGroups(int[][] grid) {

        int[][] dir = new int[][]{{1,0},{-1,0},{0,-1},{0,1},{1,1},{-1,1},{-1,-1},{1,-1}};
        int rowLength = grid.length;
        int colLength = grid[0].length;

        for(int i = 0; i < rowLength; i++) {
            for(int j = 0; j < colLength; j++) {

                if (grid[i][j] == 1) {

                }

            }
        }
        return 0;
    }


    int countEightConnectedGroups2(int[][] grid) {
        // 行数、列数
        int rows = grid.length;
        int columns = rows == 0 ? 0 : grid[0].length;

        // 空网格直接返回 0
        if (columns == 0)
            return 0;

        // 校验每行长度一致（矩形网格）
        for (int[] row : grid)
            if (row.length != columns)
                return 0;

        // visited 标记格子是否已被访问
        boolean[][] visited = new boolean[rows][columns];

        int groups = 0;  // 连通块个数

        // 遍历每个格子作为 BFS 起点
        for (int startRow = 0; startRow < rows; startRow++)
            for (int startColumn = 0; startColumn < columns; startColumn++) {

                // 只处理值为 1 且未访问的格子
                if (grid[startRow][startColumn] != 1
                        || visited[startRow][startColumn])
                    continue;

                // 发现一个新的连通块
                groups++;

                // BFS 队列，存坐标
                java.util.ArrayDeque<int[]> queue = new java.util.ArrayDeque<>();
                queue.add(new int[]{startRow, startColumn});
                visited[startRow][startColumn] = true;

                while (!queue.isEmpty()) {
                    int[] current = queue.remove();

                    // 八连通：行偏移和列偏移都从 -1 到 1
                    for (int rowStep = -1; rowStep <= 1; rowStep++)
                        for (int columnStep = -1; columnStep <= 1; columnStep++) {

                            // 跳过自己 (0,0)
                            if (rowStep == 0 && columnStep == 0)
                                continue;

                            int nextRow = current[0] + rowStep;
                            int nextColumn = current[1] + columnStep;

                            // 越界、已访问、非 1 都跳过
                            if (nextRow >= 0 && nextRow < rows
                                    && nextColumn >= 0 && nextColumn < columns
                                    && !visited[nextRow][nextColumn]
                                    && grid[nextRow][nextColumn] == 1) {
                                visited[nextRow][nextColumn] = true;
                                queue.add(new int[]{nextRow, nextColumn});
                            }
                        }


                    /*int[][] dir = {
                            {1, 0}, {-1, 0}, {0, -1}, {0, 1},
                            {1, 1}, {-1, 1}, {-1, -1}, {1, -1}
                    };

                    while (!queue.isEmpty()) {
                        int[] current = queue.remove();

                        for (int[] d : dir) {
                            int nextRow = current[0] + d[0];
                            int nextColumn = current[1] + d[1];

                            if (nextRow >= 0 && nextRow < rows
                                    && nextColumn >= 0 && nextColumn < columns
                                    && !visited[nextRow][nextColumn]
                                    && grid[nextRow][nextColumn] == 1) {
                                visited[nextRow][nextColumn] = true;
                                queue.add(new int[]{nextRow, nextColumn});
                            }
                        }
                    }*/

                }
            }

        return groups;  // 返回连通块总数
    }

}
