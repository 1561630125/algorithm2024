package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 12:27
 */
public class 错位或运算 {

    /**
     * 统计在给定的前 bitCount 位中，能够通过交换操作修复的"故障对"数量。
     *
     * 场景理解：给定两个二进制字符串 first 和 second，前 bitCount 位中，
     * 某些位置 first=1 且 second=0（需要修复），某些位置 first=0 且 second=0（可作为接收方）。
     * 通过交换 first 中的 1 和 0，使得 second 中为 0 的位置在 first 中也变为 0。
     *
     * 公式含义：统计所有可以将 first 中的 '1' 移动到 second 为 '0' 的位置的方案数。
     *
     * @param first     第一个二进制字符串
     * @param second    第二个二进制字符串
     * @param bitCount  只考虑前 bitCount 位
     * @return          符合条件的配对方案总数（long 防止溢出）
     */
    static long countFaultyOrSwaps(String first, String second, int bitCount) {
        // 参数校验：bitCount 必须在有效范围内
        if (bitCount <= 0 || bitCount > first.length() || bitCount > second.length())
            return 0L;

        // 统计变量（使用 long 防止乘法溢出）
        long ones = 0L;              // first 中 '1' 的总数
        long zeroes = 0L;            // first 中 '0' 的总数
        long firstOneSecondZero = 0L; // first=1 且 second=0 的位置数（需要被修复的故障位）
        long bothZero = 0L;          // first=0 且 second=0 的位置数（可以接收 1 的空位）

        // 遍历前 bitCount 位进行统计
        for (int index = 0; index < bitCount; index++) {
            if (first.charAt(index) == '1') {
                ones++;
                if (second.charAt(index) == '0')
                    firstOneSecondZero++;  // 这是一个"故障位"：first 有 1 但 second 要求 0
            } else {  // first.charAt(index) == '0'
                zeroes++;
                if (second.charAt(index) == '0')
                    bothZero++;            // 这是一个"空位"：双方都是 0
            }
        }

        /**
         * 核心计算公式（容斥原理）：
         *
         * 目标：将 first 中多余的 '1'（位于 second=0 的位置）与 first 中多余的 '0'（位于 second=0 的位置）交换，
         *       使得所有 second=0 的位置在 first 中也变为 0。
         *
         * bothZero * ones         ：所有空位 (0,0) 可以与任意 first=1 的位置配对，
         *                           表示将某个 '1' 移动到空位（暂时不考虑是否合理）
         * firstOneSecondZero * zeroes：所有故障位 (1,0) 可以与任意 first=0 的位置配对，
         *                           表示将故障位的 '1' 与某个 '0' 交换
         * - bothZero * firstOneSecondZero：减去重复计数，
         *                           即同时被两个集合包含的配对（空位和故障位之间的配对）
         *
         * 最终结果 = 有效交换方案数
         */
        return bothZero * ones + firstOneSecondZero * zeroes - bothZero * firstOneSecondZero;
    }

    public static void main(String[] args) {
        long l = countFaultyOrSwaps("001011", "001011", 6);
        System.out.println(l);
    }

}
