package OD.基础与模拟;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 21:08
 */
public class 竖盘四连 {
    static String connectFourResult(int width, int height, int[] moves) {
        if (width < 0 || height < 0) return "1,error";
        int[][] block = new int[width][height];
        for (int i = 0; i < block.length; i++) {
            Arrays.fill(block[i], -1);
        }

        for (int i = 0; i < moves.length; i++) {
            int column = moves[i] - 1;
            String user = (i + 1) % 2 == 1 ? "red" : "blue";
            if (column < 0 || column >= width) {
                return i + 1 + ",error";
            }

            int row = -1;
            for (int j = height - 1; j >= 0; j--) {
                if (block[j][column] == -1) {
                    block[j][column] = user.equals("red") ? 0 : 1;
                    row = j;
                    break;
                }
            }

            if (row == -1) {
                return i + 1 + ",error";
            }

            // 4个方向：水平、垂直、正斜(\), 反斜(/)
            int[][] directions = {
                    {0, 1},   // 水平：列变化
                    {1, 0},   // 垂直：行变化
                    {1, 1},   // 正斜：行和列同时增加
                    {1, -1}   // 反斜：行增加列减少
            };

            int count1 = 1;
            for (int[] dir : directions) {
                count1 += countInDirection(block,row,column,block[row][column],dir[0],dir[1]);
                count1 += countInDirection(block,row,column,block[row][column],-dir[0],-dir[1]);
                if (count1 >= 4) {
                    return i + 1 + "," + user;
                }
            }
        }
        return "0,draw";
    }

    static int countInDirection(int[][] board, int row, int col, int player, int deltaRow, int deltaCol) {
        int count = 0;
        int r = row + deltaRow;
        int c = col + deltaCol;

        while (r >= 0 && r < board.length && c >= 0 && c < board[0].length) {
            if (board[r][c] != player) {
                break;  // 遇到不同颜色或空位就停止
            }
            count++;
            r += deltaRow;
            c += deltaCol;
        }
        return count;
    }

    static String connectFourResult2(int width, int height, int[] moves) {
        // ============ 1. 参数校验 ============
        if (moves == null)
            moves = new int[0];  // 防御性编程：null转换为空数组

        // 棋盘尺寸非法：如果没有任何步骤，是平局；否则第1步就出错
        if (width <= 0 || height <= 0)
            return moves.length > 0 ? "1,error" : "0,draw";

        // ============ 2. 初始化棋盘 ============
        int[][] grid = new int[height][width];     // grid[row][col]，0=空，1=红，2=蓝
        int[] occupied = new int[width];           // 每列已占用行数（从底部往上数）
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};  // 4个方向向量

        // ============ 3. 模拟每一步落子 ============
        for (int index = 0; index < moves.length; index++) {
            // 输入列号从1开始，转为0-based索引
            int column = moves[index] - 1;

            // ---- 3.1 合法性检查 ----
            // 列号越界 或 该列已满
            if (column < 0 || column >= width || occupied[column] >= height)
                return (index + 1) + ",error";  // 返回出错步数

            // ---- 3.2 落子（利用重力） ----
            // occupied[column] 是该列当前已有棋子数
            // 重力方向向下，所以行号 = height - 1 - occupied[column]
            // 注意：occupied[column]++ 先取值再自增（妙用！）
            int row = height - 1 - occupied[column]++;
            int player = index % 2 + 1;  // 第0步红方(1)，第1步蓝方(2)，交替
            grid[row][column] = player;

            // ---- 3.3 检查是否获胜（4个方向） ----
            for (int[] direction : directions) {
                int count = 1;  // 当前棋子算1个

                // 双向延伸：sign = -1 反方向，sign = 1 正方向
                for (int sign : new int[] {-1, 1}) {
                    int nextRow = row + direction[0] * sign;
                    int nextColumn = column + direction[1] * sign;

                    // 沿当前方向持续延伸，直到遇到边界或不同颜色的棋子
                    while (nextRow >= 0 && nextRow < height &&
                            nextColumn >= 0 && nextColumn < width &&
                            grid[nextRow][nextColumn] == player) {
                        count++;
                        // 继续向前走一步
                        nextRow += direction[0] * sign;
                        nextColumn += direction[1] * sign;
                    }
                }

                // 任一方向连续相同棋子数 >= 4 即获胜
                if (count >= 4)
                    return (index + 1) + "," + (player == 1 ? "red" : "blue");
            }
            // 注意：这里没有 step >= 6 的优化，因为每次都要检查，但代码更简洁
        }

        // ============ 4. 所有步骤合法但无人获胜 ============
        return "0,draw";
    }



    public static void main(String[] args) {

        int[] moves = new int[]{2, 2, 3, 3, 4, 4, 5, 5};
        int[] moves2 = new int[]{-1, -1, -1, -1, -1, -1, -1, -1};
        int[] moves3 = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        System.out.println(connectFourResult(-1, -1, moves2));
        System.out.println(connectFourResult2(-1, -1, moves2));
        System.out.println(connectFourResult(0, -0, moves3));
        System.out.println(connectFourResult2(0, 0, moves3));

    }

}
