package 力扣.多维动态规划.A120;

import java.util.List;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-11-01 9:33
 */
public class 三角形最小路径和 {

}


class Solution {
    public int minimumTotal(List<List<Integer>> triangle) {
        int n = triangle.size();
        int[][] f = new int[n][n]; //用 f[i][j] 表示从三角形顶部走到位置 (i,j) 的最小路径和
        f[0][0] = triangle.get(0).get(0);
        for (int i = 1; i < n; ++i) {
            f[i][0] = f[i - 1][0] + triangle.get(i).get(0); //当我们在第 i 行的最左侧时，我们只能从第 i−1 行的最左侧移动过来
            for (int j = 1; j < i; ++j) {
                f[i][j] = Math.min(f[i - 1][j - 1], f[i - 1][j]) + triangle.get(i).get(j);
            }
            f[i][i] = f[i - 1][i - 1] + triangle.get(i).get(i); //当我们在第 i 行的最右侧时，我们只能从第 i−1 行的最右侧移动过来
        }
        int minTotal = f[n - 1][0];
        for (int i = 1; i < n; ++i) {
            minTotal = Math.min(minTotal, f[n - 1][i]);
        }
        return minTotal;
    }
}


//动态规划 + 空间优化
class Solution2 {
    public int minimumTotal(List<List<Integer>> triangle) {
        int n = triangle.size();
        int[][] f = new int[2][n];
        f[0][0] = triangle.get(0).get(0);
        for (int i = 1; i < n; ++i) {
            int curr = i % 2;
            int prev = 1 - curr;
            f[curr][0] = f[prev][0] + triangle.get(i).get(0);
            for (int j = 1; j < i; ++j) {
                f[curr][j] = Math.min(f[prev][j - 1], f[prev][j]) + triangle.get(i).get(j);
            }
            f[curr][i] = f[prev][i - 1] + triangle.get(i).get(i);
        }
        int minTotal = f[(n - 1) % 2][0];
        for (int i = 1; i < n; ++i) {
            minTotal = Math.min(minTotal, f[(n - 1) % 2][i]);
        }
        return minTotal;
    }
}