package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 17:41
 */
public class 字符数字和 {
    class Solution {
        /**
         * 从字符串中解析所有整数并求它们的和。
         * 规则（基于代码逻辑推断）：
         *   - 遇到 '-' 且其后紧跟数字时，把 '-数字' 当作一个负数整体加入总和；
         *   - 其它位置出现的单个数字字符，按"个位数"逐个累加；
         *   - 非数字、非负号的字符忽略。
         *
         * @param value 输入字符串
         * @return 所有解析出的整数之和
         */
        long minimumIntegerSum(String value) {
            long result = 0L;   // 累加结果，用 long 防止溢出

            // 手动控制下标，因为解析负数时需要"连读多位数字"
            for (int index = 0; index < value.length();) {

                // ---------- 情况 1：负号 + 紧跟数字 → 解析成一个负数 ----------
                if (value.charAt(index) == '-' && index + 1 < value.length()
                        && Character.isDigit(value.charAt(index + 1))) {
                    index++;                 // 跳过 '-'，指向第一个数字

                    long amount = 0L;        // 累积这个多位数的绝对值
                    while (index < value.length() && Character.isDigit(value.charAt(index))) {
                        // 逐位构造数字：amount = amount * 10 + 当前数字
                        amount = amount * 10L + value.charAt(index) - '0';
                        index++;
                    }
                    result -= amount;        // 负数，减去绝对值
                }
                // ---------- 情况 2：普通字符（只取单个数字字符） ----------
                else {
                    // 若是数字字符，按"个位数值"加入总和（注意：不是多位数）
                    if (Character.isDigit(value.charAt(index)))
                        result += value.charAt(index) - '0';
                    index++;                 // 非负数情况统一后移一位
                }
            }
            return result;
        }
    }


}
