package 力扣.多维动态规划.A63;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-11-01 14:13
 */
public class 不同路径2 {

    public static void main(String[] args) {
//        int[][] obstacleGrid = {{0, 0}};
        int[][] obstacleGrid = {{0, 0, 0}, {0, 0, 1}, {0, 1, 0}};
        Solution2 solution2 = new Solution2();
        int i = solution2.uniquePathsWithObstacles(obstacleGrid);
        System.out.println(i);
    }
}


class Solution {
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int n = obstacleGrid.length, m = obstacleGrid[0].length;
        int[] f = new int[m];

        f[0] = obstacleGrid[0][0] == 0 ? 1 : 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                if (obstacleGrid[i][j] == 1) {
                    f[j] = 0;
                    continue;
                }
                if (j - 1 >= 0 && obstacleGrid[i][j - 1] == 0) {
                    f[j] += f[j - 1];
                }
            }
        }

        return f[m - 1];
    }
}


class Solution2 {
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int ans[][] = new int[obstacleGrid.length + 1][obstacleGrid[0].length + 1];
        if (obstacleGrid[0][0] == 0) {
            ans[1][1] = 1;
        }
        for (int i = 1; i <= obstacleGrid.length; i++) {
            for (int j = 1; j <= obstacleGrid[0].length; j++) {
                if ((i != 1 || j != 1) && obstacleGrid[i - 1][j - 1] == 0) {
                    ans[i][j] = ans[i - 1][j] + ans[i][j - 1];
                }
            }
        }
        return ans[obstacleGrid.length][obstacleGrid[0].length];
    }
}