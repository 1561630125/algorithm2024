package OD.数组与字符串;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 13:03
 */
public class 字符次数表 {
    class Solution {

        /**
         * 统计字符串中每个字符出现的次数，并按指定规则排序后输出。
         *
         * 排序规则（优先级从高到低）：
         *   1. 出现次数多的排前面（降序）
         *   2. 次数相同时，小写字母排在大写字母前面（小写组=0，大写组=1，升序）
         *   3. 同组内按字符的字典序（ASCII）升序
         *
         * 输出格式：每个字符 "字符:次数;" 拼接，例如 "a:3;b:2;C:1;"
         *
         * @param value 待统计的字符串
         * @return      排序后的统计字符串
         */
        String characterFrequencyOrder(String value) {

            // ---- 1. 统计每个字符的出现次数 ----
            java.util.Map<Character, Integer> counts = new java.util.HashMap<>();
            for (char character : value.toCharArray())
                counts.put(character, counts.getOrDefault(character, 0) + 1);

            // ---- 2. 取出所有不同的字符，准备排序 ----
            java.util.List<Character> keys = new java.util.ArrayList<>(counts.keySet());

            // ---- 3. 按"次数降序 → 小写优先 → 字典序升序"排序 ----
            keys.sort((left, right) -> {
                // 规则1：出现次数降序（次数多的在前）
                int byCount = Integer.compare(counts.get(right), counts.get(left));
                if (byCount != 0)
                    return byCount;

                // 规则2：次数相同时，小写组(=0) 排在大写组(=1) 前面
                int leftGroup  = Character.isUpperCase(left)  ? 1 : 0,
                        rightGroup = Character.isUpperCase(right) ? 1 : 0;
                if (leftGroup != rightGroup)
                    return Integer.compare(leftGroup, rightGroup);

                // 规则3：同组内按字符本身（ASCII）升序
                return Character.compare(left, right);
            });

            // ---- 4. 拼接输出 ----
            StringBuilder out = new StringBuilder();
            for (char key : keys)
                out.append(key).append(':').append(counts.get(key)).append(';');
            return out.toString();
        }
    }
}
