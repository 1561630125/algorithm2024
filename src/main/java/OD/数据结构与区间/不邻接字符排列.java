package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 22:50
 */
public class 不邻接字符排列 {

    class Solution {

        // counts[c]：字符 c 还剩多少个可用（c = 0..25 对应 'a'..'z'）
        private final int[] counts = new int[26];

        /**
         * 统计用 characters 中的字符，构成长度为 length 的字符串，
         * 且相邻字符不能相同的方案数
         *
         * @param characters 可用的字符集合（可重复）
         * @param length     目标字符串长度
         * @return 方案数
         */
        long countNonAdjacentStrings(String characters, int length) {

            // 长度非法
            if (length < 0 || length > characters.length())
                return 0;

            // 1. 统计每个字符的出现次数
            java.util.Arrays.fill(counts, 0);
            for (int index = 0; index < characters.length(); index++)
                counts[characters.charAt(index) - 'a']++;

            // 2. 从空串开始，last = -1 表示前面没有字符
            return search(length, -1);
        }

        /**
         * 回溯：还需要选 remaining 个字符，前一个选的字符是 last
         *
         * @param remaining 还需要选的字符个数
         * @param last      上一个选中的字符（0..25），-1 表示还没选
         * @return 方案数
         */
        private long search(int remaining, int last) {

            // 选够了，得到一个合法方案
            if (remaining == 0)
                return 1;

            long result = 0;

            // 尝试每一个字符作为当前位置
            for (int index = 0; index < 26; index++) {

                // 不能和上一个相同
                if (index == last) continue;

                // 该字符没有剩余了
                if (counts[index] == 0) continue;

                // 选这个字符
                counts[index]--;

                // 递归选剩下的，新的 last 是 index
                result += search(remaining - 1, index);

                // 回溯：放回去
                counts[index]++;
            }

            return result;
        }
    }

}
