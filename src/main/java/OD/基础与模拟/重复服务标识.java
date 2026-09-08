package OD.基础与模拟;

import java.util.LinkedList;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 20:07
 */
public class 重复服务标识 {

    public char[] findRepeateService(String record) {
        // write code here

        int[] count = new int[26];
        char[] chars = record.toCharArray();
        for (char chr : chars) {
            count[(int)chr - 'a']++;
        }
        LinkedList<String> res = new LinkedList<>();
        for (char chr : chars) {
            if (count[(int)chr - 'a'] <= 1) {
                continue;
            }

            res.add(String.valueOf(chr));
        }

        return new char[0];
    }


    /**
     * 查找字符串中所有重复出现的字符，并按首次出现顺序返回
     *
     * @param record 输入的字符串（假设只包含小写英文字母 a-z）
     * @return 重复字符数组，按首次出现的顺序排列
     *         例如: "abca" → ['a']; "abcabc" → ['a', 'b', 'c']
     */
    public char[] findRepeateService2(String record) {
        // counts[26]: 统计每个字母出现的次数（索引 0 代表 'a'，25 代表 'z'）
        int[] counts = new int[26];

        // first[26]: 记录每个字母首次出现的位置（索引）
        // 初始化为 -1 表示该字母尚未出现
        int[] first = new int[26];
        java.util.Arrays.fill(first, -1);

        // 第一次遍历：统计每个字母出现次数，并记录首次出现位置
        for (int index = 0; index < record.length(); index++) {
            // 将字符转为 0-25 的索引值
            // 'a' → 0, 'b' → 1, ..., 'z' → 25
            int value = record.charAt(index) - 'a';

            // counts[value]++ == 0：如果该字母是首次出现（counts 从 0 开始）
            // 注意：++ 后置运算符，先判断再自增
            if (counts[value]++ == 0)
                first[value] = index;  // 记录首次出现的位置
        }

        // 存储所有重复的字母（按字母顺序临时存储）
        java.util.List<Character> answer = new java.util.ArrayList<>();

        // 第二次遍历：找出所有出现次数 > 1 的字母
        for (int index = 0; index < 26; index++)
            if (counts[index] > 1)
                answer.add((char)('a' + index));  // 将索引转回字母

        // 【关键】按首次出现位置排序，而不是按字母顺序
        // left - 'a' 和 right - 'a' 获取字母对应的索引值
        // first[index] 存储该字母首次出现的位置
        answer.sort(
                (left, right) -> Integer.compare(first[left - 'a'], first[right - 'a'])
        );

        // 将 List<Character> 转换为 char[] 数组返回
        char[] result = new char[answer.size()];
        for (int index = 0; index < result.length; index++)
            result[index] = answer.get(index);

        return result;
    }

    public static void main(String[] args) {

    }

}
