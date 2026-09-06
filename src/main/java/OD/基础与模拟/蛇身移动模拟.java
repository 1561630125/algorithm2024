package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 14:19
 */
public class 蛇身移动模拟 {

    static int snakeLength(String[] operations, String[][] board) {

        int width = board.length;
        int height = board[0].length;

        int[][] dir = new int[][]
                {{0, -1}, // L
                        {0, 1}, // R
                        {1, 0},  // U
                        {-1, 0}}; // D

        int res = 1;
        int curDir = 0;
        for (int i = 0; i < operations.length; i++) {
            String operation = operations[i];
            if ("L".equals(operation)) {
                curDir = 0;
            } else if ("R".equals(operation)) {
                curDir = 1;
            } else if ("U".equals(operation)) {
                curDir = 2;
            } else if ("D".equals(operation)) {
                curDir = 3;
            } else {
                for (int j = 0; j < board.length; j++) {
                    for (int k = 0; k < height; k++) {
                        if ("H".equals(board[j][k])) {
                            int row = j + dir[curDir][0];
                            int col = k + dir[curDir][1];
                            if (row < 0 || row >= height || col < 0 || col >= width) {
                                return res;
                            }
                            if ("F".equals(board[row][col])) {
                                res++;
                            }
                            board[j][k] = "E";
                            board[row][col] = "H";
                        }
                    }
                }
            }
        }

        return res;
    }


    int snakeLength2(String[] operations, String[] board) {
        // 获取棋盘的行数和列数
        int rows = board.length, cols = rows == 0 ? 0 : board[0].length();
        // 将字符串数组转换为字符二维数组，方便操作
        char[][] grid = new char[rows][cols];
        // 使用双端队列存储蛇身每个节点的坐标（头部在队首，尾部在队尾）
        java.util.ArrayDeque<int[]> snake = new java.util.ArrayDeque<>();

        // 初始化网格，并找到蛇头位置（'H'）作为起始点
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++) {
                grid[row][col] = board[row].charAt(col);
                if (grid[row][col] == 'H')
                    snake.addFirst(new int[] {row, col});  // 蛇头入队
            }

        // 当前移动方向的增量（初始方向为向左，因为dr=0, dc=-1）
        int dr = 0, dc = -1;

        // 遍历所有操作指令
        for (String operation : operations) {
            // 方向指令：更新移动方向
            if (operation.equals("U")) {
                dr = -1;  // 向上：行减1
                dc = 0;
            } else if (operation.equals("D")) {
                dr = 1;   // 向下：行加1
                dc = 0;
            } else if (operation.equals("L")) {
                dr = 0;   // 向左：列减1
                dc = -1;
            } else if (operation.equals("R")) {
                dr = 0;   // 向右：列加1
                dc = 1;
            } else if (operation.equals("G")) {  // "G" 表示执行移动
                // 如果蛇为空，返回长度0
                if (snake.isEmpty())
                    return 0;

                // 获取蛇头和蛇尾坐标
                int[] head = snake.peekFirst(), tail = snake.peekLast();
                // 计算移动后蛇头的新位置
                int nr = head[0] + dr, nc = head[1] + dc;

                // 检查是否撞墙（超出棋盘边界）
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols)
                    break;  // 撞墙则游戏结束，跳出循环

                // 情况1：移动到空地上（'E'）
                if (grid[nr][nc] == 'E') {
                    // 蛇尾位置变为空地（蛇尾移除）
                    grid[tail[0]][tail[1]] = 'E';
                    snake.removeLast();          // 移除蛇尾
                    // 新蛇头位置变为蛇头
                    grid[nr][nc] = 'H';
                    snake.addFirst(new int[] {nr, nc});  // 新蛇头入队
                }
                // 情况2：吃到食物（'F'）
                else if (grid[nr][nc] == 'F') {
                    // 食物变为蛇头，蛇身增长（不删除蛇尾）
                    grid[nr][nc] = 'H';
                    snake.addFirst(new int[] {nr, nc});  // 新蛇头入队
                }
                // 情况3：移动到蛇身（'H'）
                else if (grid[nr][nc] == 'H') {
                    // 特殊情况：如果移动到的位置正好是蛇尾（且蛇尾即将移除），允许移动
                    if (nr == tail[0] && nc == tail[1]) {
                        snake.removeLast();          // 移除旧蛇尾
                        snake.addFirst(new int[] {nr, nc});  // 新蛇头入队
                    } else
                        break;  // 否则撞到自己，游戏结束
                }
                // 注意：如果格子是其他字符（如初始只有H/E/F），则忽略
            }
        }
        // 返回最终蛇的长度
        return snake.size();
    }

    public static void main(String[] args) {
        String[] operations = new String[]{"L", "G", "D", "G", "R", "G", "L", "G", "R", "G", "R", "G"};
        String[][] board = new String[][]{{"F", "H", "E"}, {"E", "E", "F"}};

        System.out.println(snakeLength(operations,board));
    }

}
