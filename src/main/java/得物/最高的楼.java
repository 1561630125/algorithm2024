package 得物;

import java.util.Scanner;

/**
 * 得物2023秋招-最高的楼 - 面试鸭 - 程序员求职面试刷题神器
 * https://www.mianshiya.com/question/1814980236408815617
 *
 * @author faming.yang@hand-china.com 2024-11-02 20:20
 */
public class 最高的楼 {

}

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        long n = scanner.nextLong();
        long m = scanner.nextLong();
        long x = scanner.nextLong();

        long left = 0;
        long right = m + 1;

        while (left < right) {
            long mid = left + (right - left) / 2;
            if (calTotalHeight(n, x, mid) > m) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        System.out.println(left - 1);
    }

    public static long calTotalHeight(long n, long x, long k) {
        long total = n;
        total += (Math.max(1, k - x) + k - 2) * Math.min(x - 1, k - 2) / 2;
        total += (Math.max(1, (k - 1) - (n - x)) + k - 1) * Math.min(k - 1, n - x + 1) / 2;
        return total;
    }
}