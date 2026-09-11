package OD.数据结构与区间;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 23:03
 */
public class 迷宫死角统计 {

    class Solution {

        /**
         * 统计迷宫中的两类格子：
         * traps       = 从起点可达、但无法到达终点的格子（陷阱）
         * unreachable = 从起点不可达、且不是终点的格子（不可达）
         * <p>
         * 移动方向：只能向右、向下（从起点到终点）
         * 只能向左、向上（从终点反向推）
         *
         * @param width  宽度（x 方向）
         * @param height 高度（y 方向）
         * @param walls  墙的坐标列表，每个元素是 {x, y}
         * @return {traps, unreachable}
         */
        long[] mazeCellCounts(int width, int height, long[][] walls) {

            // 尺寸非法
            if (width <= 0 || height <= 0)
                return new long[]{0, 0};

            // blocked[x][y]：该格子是否是墙
            boolean[][] blocked = new boolean[width][height];

            // reachable[x][y]：从起点 (0,0) 能否到达
            boolean[][] reachable = new boolean[width][height];

            // canExit[x][y]：从该格子能否到达终点 (width-1, height-1)
            boolean[][] canExit = new boolean[width][height];

            // 1. 标记墙
            for (long[] wall : walls)
                if (wall[0] >= 0 && wall[0] < width && wall[1] >= 0 && wall[1] < height)
                    blocked[(int) wall[0]][(int) wall[1]] = true;

            // 2. 正向 DP：从起点往右下推，标记所有可达格子
            //    只有起点本身可达，或者左边/上边可达，当前格子才可达
            if (!blocked[0][0])
                for (int x = 0; x < width; x++)
                    for (int y = 0; y < height; y++)
                        if (!blocked[x][y]
                                && ((x == 0 && y == 0)
                                || (x > 0 && reachable[x - 1][y])
                                || (y > 0 && reachable[x][y - 1])))
                            reachable[x][y] = true;

            // 3. 反向 DP：从终点往左上推，标记所有能到达终点的格子
            //    只有终点本身，或者右边/下边能到终点，当前格子才能到终点
            for (int x = width - 1; x >= 0; x--)
                for (int y = height - 1; y >= 0; y--)
                    if (!blocked[x][y]
                            && ((x == width - 1 && y == height - 1)
                            || (x + 1 < width && canExit[x + 1][y])
                            || (y + 1 < height && canExit[x][y + 1])))
                        canExit[x][y] = true;

            // 4. 统计两类格子
            long traps = 0, unreachable = 0;

            for (int x = 0; x < width; x++)
                for (int y = 0; y < height; y++) {

                    if (blocked[x][y]) continue;  // 墙不统计

                    if (reachable[x][y] && !canExit[x][y]) {
                        // 从起点能到，但到不了终点 → 陷阱
                        traps++;
                    } else if (!reachable[x][y]
                            && (x != width - 1 || y != height - 1)) {
                        // 从起点到不了，且不是终点 → 不可达
                        // （终点单独排除，因为它本身就是目标）
                        unreachable++;
                    }
                }

            return new long[]{traps, unreachable};
        }
    }


    class Solution2 {
        long[] mazeCellCounts(int width, int height, long[][] walls) {
            if (width <= 0 || height <= 0)
                return new long[]{0, 0};

            boolean[][] blocked = new boolean[width][height];
            boolean[][] reachable = new boolean[width][height];
            boolean[][] canExit = new boolean[width][height];

            for (long[] wall : walls)
                if (wall[0] >= 0 && wall[0] < width && wall[1] >= 0 && wall[1] < height)
                    blocked[(int) wall[0]][(int) wall[1]] = true;

            // 正向 BFS：从起点出发，只能向右、向下
            if (!blocked[0][0]) {
                Queue<int[]> queue = new ArrayDeque<>();
                queue.offer(new int[]{0, 0});
                reachable[0][0] = true;

                while (!queue.isEmpty()) {
                    int[] cur = queue.poll();
                    int x = cur[0], y = cur[1];

                    if (x + 1 < width && !blocked[x + 1][y] && !reachable[x + 1][y]) {
                        reachable[x + 1][y] = true;
                        queue.offer(new int[]{x + 1, y});
                    }
                    if (y + 1 < height && !blocked[x][y + 1] && !reachable[x][y + 1]) {
                        reachable[x][y + 1] = true;
                        queue.offer(new int[]{x, y + 1});
                    }
                }
            }

            // 反向 BFS：从终点出发，反向只能向左、向上
            if (!blocked[width - 1][height - 1]) {
                Queue<int[]> queue = new ArrayDeque<>();
                queue.offer(new int[]{width - 1, height - 1});
                canExit[width - 1][height - 1] = true;

                while (!queue.isEmpty()) {
                    int[] cur = queue.poll();
                    int x = cur[0], y = cur[1];

                    if (x - 1 >= 0 && !blocked[x - 1][y] && !canExit[x - 1][y]) {
                        canExit[x - 1][y] = true;
                        queue.offer(new int[]{x - 1, y});
                    }
                    if (y - 1 >= 0 && !blocked[x][y - 1] && !canExit[x][y - 1]) {
                        canExit[x][y - 1] = true;
                        queue.offer(new int[]{x, y - 1});
                    }
                }
            }

            // 统计
            long traps = 0, unreachable = 0;
            for (int x = 0; x < width; x++)
                for (int y = 0; y < height; y++) {
                    if (blocked[x][y]) continue;
                    if (reachable[x][y] && !canExit[x][y])
                        traps++;
                    else if (!reachable[x][y] && (x != width - 1 || y != height - 1))
                        unreachable++;
                }

            return new long[]{traps, unreachable};
        }
    }
}
