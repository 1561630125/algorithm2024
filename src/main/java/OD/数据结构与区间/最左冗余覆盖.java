package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 13:36
 */
public class 最左冗余覆盖 {


    /**
     * 寻找最左侧的冗余覆盖子串
     *
     * 题目背景：
     * 给定模式串 pattern 和文本串 text，以及一个额外字符数 extra
     * 需要在 text 中找到一个最短的子串，使得：
     * 1. 这个子串包含 pattern 的所有字符（可多不可少）
     * 2. 子串长度 ≤ pattern.length() + extra
     * 3. 返回这个子串的起始位置（最左侧的那个）
     *
     * 例如：
     * pattern = "abc", text = "xyzabcde", extra = 2
     * 包含"abc"的最短子串是 "abc"，长度为3
     * pattern.length() + extra = 5
     * 3 ≤ 5 ✅ 返回起始位置 3
     *
     * pattern = "ab", text = "ba", extra = 0
     * 包含"ab"的最短子串是 "ba"，长度为2
     * pattern.length() + extra = 2
     * 2 ≤ 2 ✅ 返回起始位置 0
     *
     * @param pattern 模式串（需要包含的字符）
     * @param text 文本串（搜索范围）
     * @param extra 允许的额外字符数
     * @return 最左侧满足条件的子串起始位置，如果不存在返回 -1
     */
    static int leftmostRedundantCover(String pattern, String text, int extra) {
        // ========== 1. 参数校验 ==========
        if (pattern.isEmpty()) {
            // 空模式串：只需长度限制满足即可
            return extra >= 0 ? 0 : -1;
        }

        long maximumLength = (long) pattern.length() + extra;
        // 如果最大长度小于模式串长度（不可能，因为extra>=0）
        // 或者最大长度超过文本串长度，直接返回 -1
        if (maximumLength < pattern.length() || maximumLength > text.length()) {
            return -1;
        }

        // ========== 2. 统计模式串中每个字符的需求量 ==========
        int[] needed = new int[26];  // 模式串需要的字符频次
        int[] window = new int[26];  // 滑动窗口中各字符的频次

        for (int index = 0; index < pattern.length(); index++) {
            int letter = pattern.charAt(index) - 'a';
            if (letter < 0 || letter >= 26) return -1;  // 非字母字符
            needed[letter]++;
        }

        // ========== 3. 滑动窗口寻找冗余覆盖 ==========
        int remaining = pattern.length();  // 还需要匹配的字符数量
        int left = 0;  // 窗口左边界

        for (int right = 0; right < text.length(); right++) {
            // ====== 3.1 右边界扩展：加入新字符 ======
            int letter = text.charAt(right) - 'a';
            if (letter >= 0 && letter < 26) {  // 只处理字母
                window[letter]++;
                // 如果该字符的数量还没超过需求，减少剩余计数
                if (window[letter] <= needed[letter]) {
                    remaining--;
                }
            }

            // ====== 3.2 左边界收缩：保持窗口不超过最大长度 ======
            while ((long) right - left + 1 > maximumLength) {
                letter = text.charAt(left) - 'a';
                if (letter >= 0 && letter < 26) {
                    // 如果要移除的字符在窗口中的数量小于等于需求量
                    // 说明它之前是"有用"的，移除后需要增加剩余计数
                    if (window[letter] <= needed[letter]) {
                        remaining++;
                    }
                    window[letter]--;
                }
                left++;  // 左边界右移
            }

            // ====== 3.3 检查当前窗口是否满足条件 ======
            // 条件1：窗口长度恰好等于最大长度（在收缩后保证）
            // 条件2：所有模式串字符都匹配上了（remaining == 0）
            if ((long) right - left + 1 == maximumLength && remaining == 0) {
                return left;  // 因为从左向右扫描，第一个找到的就是最左侧的
            }
        }

        // ========== 4. 未找到满足条件的子串 ==========
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(leftmostRedundantCover("a","b",0));
    }


}
