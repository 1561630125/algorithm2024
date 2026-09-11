package OD.搜索与枚举;

import java.util.LinkedList;
import java.util.Queue;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 20:34
 */
public class 数位和寻宝 {

    // 计算一个数的各位数字之和
    // 例如 123 -> 1+2+3 = 6
    private int treasureDigitSum(int value) {
        int result = 0;
        while (value > 0) {
            result += value % 10;  // 取出最低位
            value /= 10;           // 去掉最低位
        }
        return result;
    }

    int maximumTreasureCells(int rows, int cols, int limit) {
        // 边界处理：行列有空，网格不存在
        if (rows == 0 || cols == 0) return 0;
        // 负数行列非法（原题意可能是防御性判断）
        if (rows < 0 || cols < 0) return 1;

        // visited 标记格子是否已经访问过
        boolean[][] visited = new boolean[rows][cols];

        // 用一维数组模拟 BFS 队列，存的是格子的编号 row * cols + col
        int[] queue = new int[rows * cols];
        int head = 0, tail = 1;  // head 出队指针，tail 入队指针（tail 也等于已入队元素个数）

        // 起点 (0,0) 入队并标记
        queue[0] = 0;
        visited[0][0] = true;

        // 四个方向：上、下、左、右
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // 标准 BFS
        while (head < tail) {
            // 出队一个格子，把编号还原成行列坐标
            int value = queue[head++];
            int row = value / cols;
            int column = value % cols;

            // 尝试向四个方向扩展
            for (int[] direction : directions) {
                int nextRow = row + direction[0];
                int nextColumn = column + direction[1];

                // 越界 或 已访问，跳过
                if (nextRow < 0 || nextRow >= rows ||
                        nextColumn < 0 || nextColumn >= cols ||
                        visited[nextRow][nextColumn]) continue;

                // 核心条件：行号各位数字之和 + 列号各位数字之和 <= limit
                // 不满足就跳过，不能走这个格子
                if (treasureDigitSum(nextRow) + treasureDigitSum(nextColumn) > limit)
                    continue;

                // 标记访问并入队
                visited[nextRow][nextColumn] = true;
                queue[tail++] = nextRow * cols + nextColumn;
            }
        }

        // tail 就是入队过的元素总数，即能到达的格子数
        return tail;
    }


    int maximumTreasureCells2(int rows, int cols, int limit) {
        if (rows == 0 || cols == 0) return 0;
        if (rows < 0 || cols < 0) return 1;

        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new LinkedList<>();

        // 起点入队
        queue.offer(new int[]{0, 0});
        visited[0][0] = true;
        int count = 0;  // 统计可达格子数

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int row = cur[0], column = cur[1];
            count++;  // 出队时计数

            for (int[] direction : directions) {
                int nextRow = row + direction[0];
                int nextColumn = column + direction[1];

                if (nextRow < 0 || nextRow >= rows ||
                        nextColumn < 0 || nextColumn >= cols ||
                        visited[nextRow][nextColumn]) continue;

                if (treasureDigitSum(nextRow) + treasureDigitSum(nextColumn) > limit)
                    continue;

                visited[nextRow][nextColumn] = true;
                queue.offer(new int[]{nextRow, nextColumn});
            }
        }

        return count;
    }

}
