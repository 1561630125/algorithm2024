package OD.搜索与枚举;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 18:49
 */
public class 背包问题 {

    public static void main(String[] args) {
        int[] w = new int[]{2,3,4,7};
        int[] v = new int[]{1,3,5,9};

        int n = w.length;
        int C = 10;

        int[][] dp = new int[n + 1][C + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= C; j++) {
                dp[i][j] = dp[i - 1][j];  // 不选第 i 件
                if (j >= w[i - 1])
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - w[i - 1]] + v[i - 1]);
            }
        }

        System.out.println(dp[n][C]);


        int[] dp2 = new int[C + 1];
        for (int i = 0; i < n; i++) {
            for (int j = C; j >= w[i]; j--) {   // 倒序！
                dp2[j] = Math.max(dp2[j], dp2[j - w[i]] + v[i]);
            }
        }

        System.out.println(dp2[C]);

    }

}
