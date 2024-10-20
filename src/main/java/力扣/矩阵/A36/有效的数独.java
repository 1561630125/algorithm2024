package 力扣.矩阵.A36;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-20 15:38
 */
public class 有效的数独 {
    //创建二维数组 rows 和 columns 分别记录数独的每一行和每一列中的每个数字的出现次数，
    // 创建三维数组 subboxes 记录数独的每一个小九宫格中的每个数字的出现次数
    public boolean isValidSudoku(char[][] board) {
        int[][] rows = new int[9][9];
        int[][] columns = new int[9][9];
        int[][][] subboxes = new int[3][3][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                char c = board[i][j];
                if (c != '.') {
                    int index = c - '0' - 1;
                    rows[i][index]++;
                    columns[j][index]++;
                    subboxes[i / 3][j / 3][index]++;
                    if (rows[i][index] > 1 || columns[j][index] > 1 || subboxes[i / 3][j / 3][index] > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
