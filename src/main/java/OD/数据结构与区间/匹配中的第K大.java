package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-12 13:22
 */
public class 匹配中的第K大 {


    int minKthLargestMatch(int[][] matrix, int k) {
        // 空矩阵 / k 越界，直接返回 0
        if (matrix.length == 0 || matrix[0].length == 0 || k < 1 || k > matrix.length) return 0;

        // 1. 把所有元素拍平、排序、去重
        int[] values = new int[matrix.length * matrix[0].length];
        int size = 0;
        for (int[] row : matrix) for (int value : row) values[size++] = value;
        java.util.Arrays.sort(values);

        int unique = 0;
        for (int value : values)
            if (unique == 0 || value != values[unique - 1])
                values[unique++] = value;   // 原地去重

        // 2. 二分目标：找最小的 threshold，使匹配数 >= target
        int target = matrix.length - k + 1;
        int left = 0, right = unique - 1;
        while (left < right) {
            int middle = left + (right - left) / 2;
            if (matchingCount(matrix, values[middle]) >= target)
                right = middle;       // 可行，尝试更小的阈值
            else
                left = middle + 1;    // 不可行，必须放大阈值
        }
        return values[left];          // 收敛到最小可行阈值
    }


    private int matchingCount(int[][] matrix, int threshold) {
        int[] match = new int[matrix[0].length];        // match[col] = 匹配到该列的行号
        java.util.Arrays.fill(match, -1);
        int count = 0;
        for (int row = 0; row < matrix.length; row++) {
            boolean[] visited = new boolean[matrix[0].length];
            if (augment(row, threshold, matrix, match, visited)) count++;
        }
        return count;
    }

    private boolean augment(int row, int threshold, int[][] matrix, int[] match, boolean[] visited) {
        for (int col = 0; col < matrix[row].length; col++) {
            // 只考虑边权 ≤ threshold 的边；visited 防止同一轮重复访问
            if (matrix[row][col] > threshold || visited[col]) continue;
            visited[col] = true;

            // 该列还没被匹配，或者原来匹配的行能腾出位置
            if (match[col] == -1 || augment(match[col], threshold, matrix, match, visited)) {
                match[col] = row;
                return true;
            }
        }
        return false;
    }

}
