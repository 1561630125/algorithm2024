package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-03 18:11
 */
public class 实力配对 {

    int minOpponentDifference(int[] strengths, int maxDifference) {
        int[] values = strengths.clone();
        java.util.Arrays.sort(values);
        int[] pairs = new int[values.length + 1];
        long[] sums = new long[values.length + 1];
        for (int size = 2; size <= values.length; size++) {
            pairs[size] = pairs[size - 1];
            sums[size] = sums[size - 1];
            long difference = (long)values[size - 1] - values[size - 2];
            if (difference <= maxDifference) {
                int candidatePairs = pairs[size - 2] + 1;
                long candidateSum = sums[size - 2] + difference;
                if (candidatePairs > pairs[size] || (candidatePairs == pairs[size] && candidateSum < sums[size])) {
                    pairs[size] = candidatePairs;
                    sums[size] = candidateSum;
                }
            }
        }
        return pairs[values.length] > 0 ? (int)sums[values.length] : -1;
    }

}
