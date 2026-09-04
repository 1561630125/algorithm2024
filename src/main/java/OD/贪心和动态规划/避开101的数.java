package OD.贪心和动态规划;

/**
 * 数位DP
 *
 * @author faming.yang@hand-china.com 2026-09-04 10:50
 */
public class 避开101的数 {
    long countWithout101(long left, long right) {
        return validUpTo(right) - validUpTo(left - 1);
    }

    private long validUpTo(long limit) {
        if (limit < 0) return 0;
        String bits = Long.toBinaryString(limit);
        long[][][] memo = new long[bits.length()][4][2];
        for (long[][] layer : memo) for (long[] row : layer) java.util.Arrays.fill(row, -1);
        return search(bits, 0, 0, 1, memo);
    }

    private long search(String bits, int position, int previousTwo, int tight, long[][][] memo) {
        if (position == bits.length()) return 1;
        if (memo[position][previousTwo][tight] >= 0) return memo[position][previousTwo][tight];
        int maximum = tight == 1 ? bits.charAt(position) - '0' : 1;
        long result = 0;
        for (int bit = 0; bit <= maximum; bit++) {
            if (previousTwo == 2 && bit == 1) continue;
            result += search(bits, position + 1, ((previousTwo << 1) | bit) & 3, tight == 1 && bit == maximum ? 1 : 0, memo);
        }
        return memo[position][previousTwo][tight] = result;
    }

}
