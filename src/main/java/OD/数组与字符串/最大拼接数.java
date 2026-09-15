package OD.数组与字符串;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 13:09
 */
public class 最大拼接数 {

    class Solution {

        /**
         * 把一组数字字符串拼接成一个"数值最大"的字符串。
         *
         * 核心：不是按字典序排，而是按 "right+left 与 left+right 的比较" 排序。
         *
         * 例：["3", "30", "34", "5", "9"]
         *   普通字典序："3" < "30" < "34" < "5" < "9" → "3303459"  （不是最大）
         *   本题排序：  "9" > "5" > "34" > "3" > "30" → "9534330" （最大）
         *
         * @param numbers 数字字符串数组
         * @return        拼接后的最大字符串
         */
        String largestConcatenatedValue(String[] numbers) {

            // ---- 0. 克隆一份，避免修改调用方传入的数组 ----
            numbers = numbers.clone();

            // ---- 1. 按"拼接后谁更大"排序 ----
            // 对任意两个串 left、right：
            //   如果 right+left > left+right，说明 right 应该排在 left 前面
            //   即按 (right+left) 与 (left+right) 的降序排列
            java.util.Arrays.sort(numbers, (left, right) -> (right + left).compareTo(left + right));

            // ---- 2. 拼接所有字符串 ----
            StringBuilder result = new StringBuilder();
            for (String value : numbers)
                result.append(value);

            // ---- 3. 去掉前导零 ----
            // 例如 ["0", "0"] → "00" → 去前导零 → "0"
            // 注意保留 1 个字符，避免结果变成空串
            while (result.length() > 1 && result.charAt(0) == '0')
                result.deleteCharAt(0);

            return result.toString();
        }
    }

}
