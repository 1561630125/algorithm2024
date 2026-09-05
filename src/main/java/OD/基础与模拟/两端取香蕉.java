package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 13:24
 */
public class 两端取香蕉 {

    static long maxBananas(long[] numbers, int picks) {
        if (picks <=0) return 0;

        long[] prefix = new long[numbers.length];
        long sum = 0;
        for (long number : numbers) {
            sum += number;
        }
        prefix[0] = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            prefix[i] = prefix[i - 1] +numbers[i];
        }

        long cursum = Math.max(prefix[picks - 1], sum - prefix[numbers.length - picks - 1]);
        for (int i = 0; i < picks; i++) {
            cursum = Math.max(cursum, prefix[i] + sum - prefix[numbers.length - picks + i]);
        }
        return cursum;
    }

    static long maxBananas2(long[] numbers, int picks) {
        if (numbers == null || picks < 0 || picks > numbers.length)
            return 0;
        long current = 0;
        for (int index = numbers.length - picks; index < numbers.length; index++)
            current += numbers[index];
        long best = current;
        for (int left = 1; left <= picks; left++) {
            current += numbers[left - 1] - numbers[numbers.length - picks + left - 1];
            best = Math.max(best, current);
        }
        return best;
    }


    public static void main(String[] args) {

        long[] numbers = new long[]{1, 1, 1, 1, 1, 1, 1};
        int picks = 1;
        System.out.println(maxBananas(numbers, picks));

        System.out.println(maxBananas2(numbers, picks));
    }

}
