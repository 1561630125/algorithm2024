package OD.搜索与枚举;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 21:51
 */
public class 双源矩阵扩散 {


    int diffusionTime(int rows, int cols, int x1, int y1, int x2, int y2) {


        return 0;
    }

    int diffusionTime3(int rows, int cols, int x1, int y1, int x2, int y2) {
        // 边界检查
        if (rows <= 0 || cols <= 0) return -1;
        if (x1 < 0 || x1 >= rows || y1 < 0 || y1 >= cols) return -1;
        if (x2 < 0 || x2 >= rows || y2 < 0 || y2 >= cols) return -1;

        // 每个格子的覆盖时间，-1 表示还没被覆盖
        int[][] time = new int[rows][cols];
        for (int[] row : time) java.util.Arrays.fill(row, -1);

        java.util.ArrayDeque<int[]> queue = new java.util.ArrayDeque<>();

        // 两个源同时入队，时间都是 0
        queue.offer(new int[]{x1, y1});
        time[x1][y1] = 0;
        // 注意：如果两个源重合，只入队一次
        if (x1 != x2 || y1 != y2) {
            queue.offer(new int[]{x2, y2});
            time[x2][y2] = 0;
        }

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        int answer = 0;      // 最大覆盖时间
        int covered = (x1 == x2 && y1 == y2) ? 1 : 2;  // 已覆盖格子数

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int row = cur[0], col = cur[1];

            for (int k = 0; k < 4; k++) {
                int nr = row + dr[k];
                int nc = col + dc[k];

                // 越界 或 已覆盖，跳过
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                if (time[nr][nc] != -1) continue;

                // 覆盖时间 = 当前格子时间 + 1
                time[nr][nc] = time[row][col] + 1;
                answer = Math.max(answer, time[nr][nc]);
                covered++;
                queue.offer(new int[]{nr, nc});
            }
        }

        // 如果还有格子没覆盖（本题不会发生，四连通总能覆盖全图）
        if (covered != rows * cols) return -1;

        return answer;
    }


    int diffusionTime2(int rows, int cols, int x1, int y1, int x2, int y2) {
        // 行列数必须为正
        if (rows <= 0 || cols <= 0) return -1;

        // 起点 (x1, y1) 必须在网格内
        if (x1 < 0 || x1 >= rows || y1 < 0 || y1 >= cols) return -1;

        // 起点 (x2, y2) 必须在网格内
        if (x2 < 0 || x2 >= rows || y2 < 0 || y2 >= cols) return -1;

        int result = 0;

        // 遍历网格中每一个格子
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                // 该格子到 (x1, y1) 的曼哈顿距离
                int first = Math.abs(row - x1) + Math.abs(col - y1);

                // 该格子到 (x2, y2) 的曼哈顿距离
                int second = Math.abs(row - x2) + Math.abs(col - y2);

                // 两个扩散源同时扩散，该格子被「先到」的那个覆盖
                // 覆盖时间 = min(first, second)
                // 整张网格全部被覆盖的时间 = 所有格子覆盖时间的最大值
                result = Math.max(result, Math.min(first, second));
            }
        }

        return result;
    }

}
