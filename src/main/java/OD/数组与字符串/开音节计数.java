package OD.数组与字符串;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-12 14:41
 */
public class 开音节计数 {

    class Solution {

        /**
         * 统计字符串数组中所有单词里"相对开音节"的总个数。
         *
         * 规则说明：
         *   1. 若单词全部由小写字母组成，则先将其翻转，再统计；
         *      否则保持原样统计。
         *   2. "相对开音节"的构成为：辅音 + 元音 + 辅音(非 r) + e
         *      即形如 "辅 元 辅 e"，如 cake 中的 "ake"、bike 中的 "ike"。
         *
         * @param words 待统计的单词数组
         * @return 所有单词中相对开音节的总数
         */
        int countRelativeOpenSyllables(String[] words) {
            int result = 0;  // 用于累计所有单词中相对开音节的总数

            // 逐个处理每个单词
            for (String original : words) {

                // ---------- 第一步：判断当前单词是否全部由小写字母组成 ----------
                // 初始值：非空才可能"全小写"（空串视为不满足条件）
                boolean lower = !original.isEmpty();

                // 遍历每个字符，只要有一个不是小写字母，lower 就变为 false
                // 使用 &= 而非 &&，可以确保循环完整执行（虽然对结果无影响）
                for (int index = 0; index < original.length(); index++) {
                    lower &= original.charAt(index) >= 'a' && original.charAt(index) <= 'z';
                }

                // ---------- 第二步：根据是否全小写决定是否翻转 ----------
                // 全小写 → 翻转后再统计（等价于从右往左找该模式）
                // 否则   → 保持原样统计
                String word = lower
                        ? new StringBuilder(original).reverse().toString()
                        : original;

                // ---------- 第三步：在（可能已翻转的）单词中统计相对开音节 ----------
                // 窗口大小为 4：word[index]、word[index+1]、word[index+2]、word[index+3]
                // 循环条件 index + 3 < word.length() 保证访问 index+3 时不越界
                for (int index = 0; index + 3 < word.length(); index++) {

                    // 依次判断 4 个位置是否满足"辅 元 辅(非 r) e"模式
                    if (consonant(word.charAt(index))               // 第 1 位：辅音
                            && vowel(word.charAt(index + 1))        // 第 2 位：元音
                            && consonant(word.charAt(index + 2))    // 第 3 位：辅音
                            && word.charAt(index + 2) != 'r'        // 第 3 位不能是 'r'
                            && word.charAt(index + 3) == 'e') {     // 第 4 位：'e'
                        result++;  // 找到一个相对开音节，计数 +1
                    }
                }
            }
            return result;  // 返回统计结果
        }

        /**
         * 判断字符是否为元音字母。
         * 元音集合：a、e、i、o、u
         *
         * @param value 待判断的字符
         * @return 是元音返回 true，否则返回 false
         */
        private boolean vowel(char value) {
            return "aeiou".indexOf(value) >= 0;
        }

        /**
         * 判断字符是否为（小写）辅音字母。
         * 条件：是 a~z 之间的小写字母，且不是元音。
         *
         * @param value 待判断的字符
         * @return 是小写辅音返回 true，否则返回 false
         */
        private boolean consonant(char value) {
            return value >= 'a' && value <= 'z' && !vowel(value);
        }
    }



    int countRelativeOpenSyllables(String[] words) {

        int count = 0;
        for(int i = 0; i < words.length; i++) {
            String reverse = new StringBuffer(words[i]).reverse().toString();
            if (reverse.length() == 4) {
                boolean flag = !isY(reverse.charAt(0))
                        && isY(reverse.charAt(1)) &&
                        (!isY(reverse.charAt(1)) && 'r' != reverse.charAt(2))
                        && 'e' == reverse.charAt(3);
                if (flag) {
                    count++;
                }

            }

        }

        return count;
    }

    boolean isY(char c){
        if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
            return true;
        }
        return false;
    }


}
