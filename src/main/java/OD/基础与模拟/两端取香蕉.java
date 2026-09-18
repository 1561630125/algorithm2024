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
        // ---------- 1. 边界检查 ----------
        // picks < 0 或 picks > 数组长度 → 非法，返回 0
        if (numbers == null || picks < 0 || picks > numbers.length)
            return 0;

        // ---------- 2. 初始窗口：取最右边的 picks 个 ----------
        // 即 left = 0 的情况：头部取 0 个，尾部取 picks 个
        long current = 0;
        for (int index = numbers.length - picks; index < numbers.length; index++)
            current += numbers[index];

        long best = current;   // 当前最优

        // ---------- 3. 枚举 left = 1 到 picks ----------
        // left 表示"从头部取几个"
        // 尾部取 picks - left 个
        for (int left = 1; left <= picks; left++) {

            // 滑动窗口左移一位：
            //   加入 numbers[left - 1]（头部新加入的）
            //   移除 numbers[numbers.length - picks + left - 1]（尾部退出的）
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
