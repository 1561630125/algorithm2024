package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 21:12
 */
public class l两个素因数 {
    static long[] primeProductFactors(long num) {


        return new long[] {-1L, -1L };
    }

    /**
     * 判断一个数是否为质数（素数）
     * 质数定义：大于1的自然数，除了1和它本身外，没有其他因数
     *
     * @param value 待判断的整数（long类型）
     * @return true表示是质数，false表示不是质数
     */
    private boolean isPrime(long value) {
        // 小于2的数都不是质数（0和1不是质数）
        if (value < 2)
            return false;

        // 处理偶数情况：2是唯一的偶质数，其他偶数都不是质数
        if (value % 2 == 0)
            return value == 2;  // 如果value是2返回true，否则返回false

        // 检查奇数因子（从3开始，只检查奇数）
        // 循环条件 divisor <= value / divisor 等价于 divisor * divisor <= value
        // 这样写可以避免 divisor * divisor 溢出（虽然long溢出概率低，但更安全）
        // 优化：只需要检查到 sqrt(value) 即可
        for (long divisor = 3; divisor <= value / divisor; divisor += 2) {
            // 如果value能被divisor整除，说明存在因数，不是质数
            if (value % divisor == 0)
                return false;
        }
        // 如果循环结束都没有找到因数，说明是质数
        return true;
    }

    /**
     * 寻找一个数的两个质数因子，满足：因子1 × 因子2 = num
     * 专门查找能分解成两个质数乘积的数（即半质数，semiprime）
     *
     * 例如：num = 21，返回 [3, 7]，因为 3 × 7 = 21，且3和7都是质数
     *       num = 15，返回 [3, 5]
     *       num = 22，返回 [2, 11]
     *
     * @param num 待分解的整数
     * @return long[2] 包含两个质数因子，如果找不到则返回 [-1, -1]
     */
    long[] primeProductFactors2(long num) {
        // 只有 num >= 4 才可能有质数因子对
        // 因为最小的质数因子对是 2 × 2 = 4
        if (num >= 4) {
            // 从小到大的顺序遍历可能的因子
            // divisor <= num / divisor 等价于 divisor * divisor <= num
            // 只需要检查到 sqrt(num) 即可，因为因子是成对出现的
            // 例如 21 的因子对是 3 和 7，只需要找到3就能确定7
            for (long divisor = 2; divisor <= num / divisor; divisor++) {
                // 检查三个条件：
                // 1. divisor 能整除 num（是因子）
                // 2. divisor 本身是质数
                // 3. num / divisor（另一个因子）也是质数
                if (num % divisor == 0 && isPrime(divisor) && isPrime(num / divisor)) {
                    // 找到符合条件的质数因子对，返回
                    return new long[] {divisor, num / divisor};
                }
            }
        }
        // 没有找到符合条件的质数因子对
        // 可能情况：num < 4，或者num本身是质数，或者质因子数量不对
        return new long[] {-1, -1};
    }


    public static void main(String[] args) {

    }

}
