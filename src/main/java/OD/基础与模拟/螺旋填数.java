package OD.基础与模拟;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 11:29
 */
public class 螺旋填数 {

    public static String[][] buildSpiralMatrix(int n, int rows) {
        if (n <= 0 || rows <= 0)
            return new String[0][0];

        int columns = (n + rows - 1) / rows;
        String[][] matrix = new String[rows][columns];
        for (String[] row : matrix)
            java.util.Arrays.fill(row, "*");

        int top = 0;
        int bottom = rows - 1;
        int left = 0;
        int right = columns - 1;
        int value = 1;

        while (true) {
            // 从左到右填充顶部
            for (int col = left; col <= right && value <= n; col++) {
                matrix[top][col] = Integer.toString(value++);
            }
            if (++top > bottom || value > n) break;

            // 从上到下填充右侧
            for (int row = top; row <= bottom && value <= n; row++) {
                matrix[row][right] = Integer.toString(value++);
            }
            if (--right < left || value > n) break;

            // 从右到左填充底部
            if (top <= bottom) {
                for (int col = right; col >= left && value <= n; col--) {
                    matrix[bottom][col] = Integer.toString(value++);
                }
                bottom--;
            }
            if (left > right || value > n) break;

            // 从下到上填充左侧
            for (int row = bottom; row >= top && value <= n; row--) {
                matrix[row][left] = Integer.toString(value++);
            }
            left++;
            if (top > bottom || value > n) break;
        }

        return matrix;
    }

    public static void main(String[] args) {
        String[][]  ints = buildSpiralMatrix(10,4);
        for (int i = 0; i < ints.length; i++) {
            System.out.print(Arrays.toString(ints[i]) +" ");
            System.out.println();
        }
    }
}
