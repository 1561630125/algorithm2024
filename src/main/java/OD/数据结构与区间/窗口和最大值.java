package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 14:44
 */
public class 窗口和最大值 {

    long maxWindowSum(long[] values, int windowSize) {
        long res = Long.MIN_VALUE;
        long[] sum = new long[values.length];
        sum[0] = values[0];
        for(int i = 1; i < values.length; i++) {
            sum[i] = sum[i-1] + values[i];
        }

        for(int i = windowSize; i < values.length; i++) {
            res = Math.max(res, sum[i] - sum[i - windowSize]);
        }

        return res;
    }


    long maxWindowSum2(long[] values, int windowSize) {
        if (windowSize <= 0 || windowSize > values.length) return 0;
        long current = 0;
        for (int index = 0; index < windowSize; index++) current += values[index];
        long best = current;
        for (int index = windowSize; index < values.length; index++) {
            current += values[index] - values[index - windowSize];
            if (current > best) best = current;
        }
        return best;
    }

}
