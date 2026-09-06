package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 22:08
 */
public class 首报积分 {

    static int maxFirstReportScore(int[] logs) {
        int[] dp = new int[logs.length];
        dp[0] = Math.min(100, logs[0]);

        int[] pre = new int[logs.length];
        pre[0] = logs[0];
        for(int i = 1; i < logs.length; i++) {
            pre[i] = pre[i-1] + logs[i];
        }

        for(int i = 1; i < logs.length; i++) {

            int sum = 0;
            for(int k = 0; k < i; k++) {
                sum += logs[k] * (i-k);
            }

            dp[i] = Math.min(100, pre[i]) - sum;
        }

        int res = 0;
        for(int i = 0; i < dp.length; i++) {
            res= Math.max(res,dp[i]);
        }

        return res;
    }


    static int maxFirstReportScore2(int[] logs) {
        int total = 0;
        int backlog = 0;
        int delay = 0;
        int best = 0;
        for (int current : logs) {
            total += current;
            delay += backlog;
            backlog += current;
            best = Math.max(best, Math.min(100, total) - delay);
            if (total >= 100) break;
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(maxFirstReportScore(new int[]{1,2,3,4}));
        System.out.println(maxFirstReportScore2(new int[]{1,2,3,4}));
    }

}
