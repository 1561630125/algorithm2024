package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 23:11
 */
public class 分隔差值 {

    long maximumSplitDifference(long[] numbers) {
        int cur = 0;
        long res = 0;

        long tatal = 0;
        for (int i = 0; i < numbers.length; i++) {
            tatal += numbers[i];
        }

        for (int i = 0; i < numbers.length; i++) {
            cur += numbers[i];
            res = Math.max(res, Math.abs(tatal - cur - cur));
        }
        return res;
    }

    long maximumSplitDifference2(long[] numbers) {
        if (numbers.length < 2) return 0L;
        long total = 0L;
        for (long value : numbers) total += value;
        long prefix = 0L, result = 0L;
        for (int index = 0; index + 1 < numbers.length; index++) {
            prefix += numbers[index];
            result = Math.max(result, Math.abs(prefix - (total - prefix)));
        }
        return result;
    }

}
