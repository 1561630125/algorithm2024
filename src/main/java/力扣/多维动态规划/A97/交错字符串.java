package 力扣.多维动态规划.A97;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-11-01 15:04
 */
public class 交错字符串 {

}

class Solution {
    public boolean isInterleave(String s1, String s2, String s3) {
        int n = s1.length(), m = s2.length(), t = s3.length();

        if (n + m != t) {
            return false;
        }

        boolean[][] f = new boolean[n + 1][m + 1]; //定义f(i,j)表示s1的前i个元素和s2的前j个元素是否能交错组成s3的前i+j个元素

        f[0][0] = true;
        for (int i = 0; i <= n; ++i) {
            for (int j = 0; j <= m; ++j) {
                int p = i + j - 1;
                if (i > 0) {
                    f[i][j] = f[i][j] || (f[i - 1][j] && s1.charAt(i - 1) == s3.charAt(p));
                }
                if (j > 0) {
                    f[i][j] = f[i][j] || (f[i][j - 1] && s2.charAt(j - 1) == s3.charAt(p));
                }
            }
        }

        return f[n][m];
    }
}
