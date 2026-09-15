package OD.数组与字符串;

import java.util.ArrayList;
import java.util.List;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 13:13
 */
public class 第几朵水仙 {

    class Solution {

        /**
         * 查询指定位数的"阿姆斯特朗数"（水仙花数）序列中的第 index 个。
         *
         * 阿姆斯特朗数：一个 n 位数，其各位数字的 n 次方之和等于它本身。
         *   例：153 = 1³ + 5³ + 3³
         *       1634 = 1⁴ + 6⁴ + 3⁴ + 4⁴
         *
         * 本题似乎采用"查表法"：
         *   - 已知每个位数对应的所有阿姆斯特朗数
         *   - 如果 index 超出表长，返回最后一个值 * index（一种兜底规则）
         *
         * @param digitCount 位数（支持 3~7，其他返回 -1）
         * @param index      查询下标（0-based）
         * @return           第 index 个阿姆斯特朗数；参数非法时返回 -1
         */
        long selectArmstrongNumber(int digitCount, long index) {

            // ---- 1. 按位数查表 ----
            long[] values = switch (digitCount) {
                case 3 -> new long[] {153, 370, 371, 407};
                case 4 -> new long[] {1634, 8208, 9474};
                case 5 -> new long[] {54748, 92727, 93084};
                case 6 -> new long[] {548834};
                case 7 -> new long[] {1741725, 4210818, 9800817, 9926315};
                default -> null;   // 不支持的位数
            };

            // ---- 2. 位数不支持，或 index 为负 → -1 ----
            if (values == null || index < 0)
                return -1;

            // ---- 3. 查询 ----
            //   index 在表内：直接返回 values[index]
            //   index 超出表长：返回最后一个值 * index（兜底规则）
            return index >= values.length
                    ? values[values.length - 1] * index
                    : values[(int) index];
        }
    }

    public static class ArmstrongGenerator {

        // 判断 n 位数 num 是否为阿姆斯特朗数
        static boolean isArmstrong(long num, int n) {
            long sum = 0;
            long temp = num;
            while (temp > 0) {
                int digit = (int)(temp % 10);
                sum += power(digit, n);
                temp /= 10;
            }
            return sum == num;
        }

        static long power(int base, int exp) {
            long result = 1;
            for (int i = 0; i < exp; i++) result *= base;
            return result;
        }

        // 生成所有 n 位阿姆斯特朗数
        static List<Long> generate(int n) {
            List<Long> result = new ArrayList<>();
            long start = power(10, n - 1);   // 最小的 n 位数，如 n=3 → 100
            long end   = power(10, n) - 1;   // 最大的 n 位数，如 n=3 → 999

            for (long num = start; num <= end; num++) {
                if (isArmstrong(num, n)) {
                    result.add(num);
                }
            }
            return result;
        }

        public static void main(String[] args) {
            for (int n = 3; n <= 7; n++) {
                List<Long> list = generate(n);
                System.out.println(n + "位: " + list);
            }
        }
    }

}
