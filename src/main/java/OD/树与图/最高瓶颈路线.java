package OD.树与图;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-13 22:10
 */
public class 最高瓶颈路线 {

    class Solution {

        /**
         * 从左上角 (0,0) 走到右下角 (rows-1, cols-1)。
         * 路径的"信号" = 路径上所有格子值的最小值。
         * 求所有路径中，信号的最大值。
         *
         * @param grid 二维网格
         * @return 最大的"路径最小值"
         */
        long maximumRouteSignal(long[][] grid) {

            // ---------- 边界：空网格 ----------
            if (grid.length == 0 || grid[0].length == 0)
                return 0;

            int rows = grid.length, cols = grid[0].length;

            // best[r][c] = 从 (0,0) 到 (r,c) 的所有路径中，"路径最小值"的最大值
            long[][] best = new long[rows][cols];
            best[0][0] = grid[0][0];   // 起点：路径最小值就是自己

            // ---------- 优先队列：按 best 值从大到小出队 ----------
            // 存 {best值, 行, 列}
            java.util.PriorityQueue<long[]> queue =
                    new java.util.PriorityQueue<>((a, b) -> Long.compare(b[0], a[0]));
            queue.add(new long[]{best[0][0], 0, 0});

            // 四个方向：下、上、右、左
            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

            while (!queue.isEmpty()) {
                long[] current = queue.poll();
                long score = current[0];
                int row = (int) current[1], col = (int) current[2];

                // ---------- 惰性删除：过期条目跳过 ----------
                // 如果队列里的值不是当前最新的 best，说明它已被更优值替换
                if (score != best[row][col])
                    continue;

                // ---------- 向四个方向扩展 ----------
                for (int[] direction : directions) {
                    int nr = row + direction[0], nc = col + direction[1];

                    // 越界跳过
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols)
                        continue;

                    // 新路径的"最小值" = min(当前路径最小值, 新格子值)
                    long candidate = Math.min(score, grid[nr][nc]);

                    // 只有比已知的更优才更新
                    if (candidate > best[nr][nc]) {
                        best[nr][nc] = candidate;
                        queue.add(new long[]{candidate, nr, nc});
                    }
                }
            }

            return best[rows - 1][cols - 1];
        }
    }

}
