package OD.数组与字符串;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-14 23:43
 */
public class 相等数对计数 {

    class Solution {
        long countMatchingPairs(long[] first, long[] second) {
            java.util.Map<Long, Long> frequencies = new java.util.HashMap<>();
            for (long value : first)
                frequencies.put(value, frequencies.getOrDefault(value, 0L) + 1);
            long result = 0;
            for (long value : second)
                result += frequencies.getOrDefault(value, 0L);
            return result;
        }
    }

}
