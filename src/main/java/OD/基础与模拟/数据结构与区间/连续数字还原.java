package OD.基础与模拟.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 11:50
 */
public class 连续数字还原 {

    /**
     * 还原连续数字序列的起始数字
     * 题目：给定一个被打乱字符顺序的字符串（由连续正整数拼接而成），还原出起始数字
     *
     * 例如：digits = "19801211", length = 5
     *       连续5个数是 8,9,10,11,12，拼接为 "89101112"，打乱后为 "19801211"
     *       答案：8
     *
     * @param digits 被打乱的数字字符串（只包含0-9）
     * @param length 连续正整数的个数（序列长度）
     * @return 起始数字（最小的那个数字），如果无解或多个解则返回 -1
     */
    int restoreSequenceStart(String digits, int length) {
        // ========== 参数合法性校验 ==========
        if (digits == null || digits.isEmpty() || length <= 0 || length > 1000)
            return -1;

        // ========== 统计目标字符串中每个数字（0-9）的出现次数 ==========
        int[] target = new int[10];
        for (int index = 0; index < digits.length(); index++) {
            char character = digits.charAt(index);
            // 如果包含非数字字符，直接返回 -1
            if (character < '0' || character > '9')
                return -1;
            target[character - '0']++;
        }

        int match = -1;  // 记录找到的起始数字，用于检测是否唯一

        // ========== 枚举所有可能的起始数字 ==========
        // 根据题目约束，length <= 1000，每个数字最多4位（如1000），最大长度不超过4000
        // 而 digits.length() <= 200，因此起始数字范围不会太大
        // 这里将上限设为 1000 - length + 1，保证序列的最大值不超过 1000
        for (int start = 1; start + length - 1 <= 1000; start++) {

            // ========== 统计当前窗口（start 到 start+length-1）的数字频次 ==========
            int[] counts = new int[10];
            int characters = 0;  // 当前窗口拼接后的总字符数

            // 遍历窗口中的每个数字
            for (int value = start; value < start + length; value++) {
                String text = String.valueOf(value);
                characters += text.length();  // 累计字符数
                // 统计当前数字中每个字符的出现次数
                for (int index = 0; index < text.length(); index++) {
                    counts[text.charAt(index) - '0']++;
                }
            }

            // ========== 判断是否匹配 ==========
            // 两个条件：1) 总字符数相同；2) 每个数字出现次数完全相同
            if (characters == digits.length() && java.util.Arrays.equals(counts, target)) {
                // 如果已经找到过一个匹配，说明解不唯一，返回 -1
                if (match != -1)
                    return -1;
                match = start;  // 记录第一个匹配的起始数字
            }
        }

        // 返回找到的唯一起始数字，如果没找到则返回 -1
        return match;
    }

    public static void main(String[] args) {

    }

}
