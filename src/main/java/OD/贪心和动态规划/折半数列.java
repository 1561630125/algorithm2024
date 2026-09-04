package OD.贪心和动态规划;

/**
 *  Fenwick树（树状数组）
 *
 * @author faming.yang@hand-china.com 2026-09-04 9:54
 */
public class 折半数列 {

    long countConstructibleSequences(int n) {
        if (n <= 1)
            return 1;
        long[] sameOdd = new long[n + 1], sameEven = new long[n + 1];
        long[] alternatingOdd = new long[n + 1], alternatingEven = new long[n + 1];
        long answer = 1;
        for (int value = 1; value <= n; value++) {
            int limit = value / 2;
            long same, alternating;
            if ((value & 1) == 1) {
                same = 1 + query(sameOdd, limit);
                alternating = 1 + query(alternatingEven, limit);
                update(sameOdd, value, same);
                update(alternatingOdd, value, alternating);
            } else {
                same = 1 + query(sameEven, limit);
                alternating = 1 + query(alternatingOdd, limit);
                update(sameEven, value, same);
                update(alternatingEven, value, alternating);
            }
            if (value == n)
                answer = same + alternating - 1;
        }
        return answer;
    }
    private long query(long[] tree, int index) {
        long result = 0;
        while (index > 0) {
            result += tree[index];
            index -= index & -index;
        }
        return result;
    }
    private void update(long[] tree, int index, long value) {
        while (index < tree.length) {
            tree[index] += value;
            index += index & -index;
        }
    }

}
