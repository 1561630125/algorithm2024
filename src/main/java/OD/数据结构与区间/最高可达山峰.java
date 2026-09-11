package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 22:45
 */
public class 最高可达山峰 {


    class Solution {

        /**
         * 从 (0,0) 出发，在高度差不超过 maxDifference 的前提下能到达的最高峰
         * 以及到达该最高峰的最少步数
         *
         * @param grid          二维高度网格
         * @param maxDifference 相邻格子允许的最大高度差
         * @return {最高峰高度, 到达它的最少步数}
         */
        long[] highestReachablePeak(long[][] grid, long maxDifference) {

            // 空网格
            if (grid.length == 0 || grid[0].length == 0)
                return new long[]{0, 0};

            int rows = grid.length, cols = grid[0].length;

            // visited 标记是否访问过
            boolean[][] visited = new boolean[rows][cols];

            // BFS 队列，元素是 {row, col, distance}
            java.util.ArrayDeque<int[]> queue = new java.util.ArrayDeque<>();

            // 起点 (0,0)，距离 0
            queue.add(new int[]{0, 0, 0});
            visited[0][0] = true;

            long best = grid[0][0];  // 目前到达过的最高高度
            long steps = 0;          // 到达最高高度的步数

            // 四连通方向
            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

            while (!queue.isEmpty()) {
                int[] current = queue.remove();
                int row = current[0];
                int col = current[1];
                int distance = current[2];

                for (int[] direction : directions) {
                    int nr = row + direction[0];
                    int nc = col + direction[1];

                    // 越界、已访问、高度差超过 maxDifference 都跳过
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols
                            || visited[nr][nc]
                            || Math.abs(grid[nr][nc] - grid[row][col]) > maxDifference)
                        continue;

                    visited[nr][nc] = true;
                    queue.add(new int[]{nr, nc, distance + 1});  // 步数 +1

                    // 发现更高的峰，更新最佳
                    if (grid[nr][nc] > best) {
                        best = grid[nr][nc];
                        steps = distance + 1;
                    }
                }
            }

            return new long[]{best, steps};
        }
    }


    class Solution2 {
        long[] highestReachablePeak(long[][] grid, long maxDifference) {
            if (grid.length == 0 || grid[0].length == 0)
                return new long[]{0, 0};

            int rows = grid.length, cols = grid[0].length;
            boolean[][] visited = new boolean[rows][cols];
            java.util.ArrayDeque<int[]> queue = new java.util.ArrayDeque<>();

            queue.add(new int[]{0, 0});
            visited[0][0] = true;

            long best = grid[0][0];
            long steps = 0;

            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            int distance = 0;  // 当前层数（距离）

            while (!queue.isEmpty()) {
                int size = queue.size();  // 当前层的节点数

                for (int i = 0; i < size; i++) {
                    int[] current = queue.remove();
                    int row = current[0], col = current[1];

                    for (int[] direction : directions) {
                        int nr = row + direction[0], nc = col + direction[1];

                        if (nr < 0 || nr >= rows || nc < 0 || nc >= cols
                                || visited[nr][nc]
                                || Math.abs(grid[nr][nc] - grid[row][col]) > maxDifference)
                            continue;

                        visited[nr][nc] = true;
                        queue.add(new int[]{nr, nc});

                        // 新格子距离 = distance + 1
                        if (grid[nr][nc] > best) {
                            best = grid[nr][nc];
                            steps = distance + 1;
                        }
                    }
                }

                distance++;  // 一层处理完，距离 +1
            }

            return new long[]{best, steps};
        }
    }


}
