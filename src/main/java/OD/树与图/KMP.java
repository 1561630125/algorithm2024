package OD.树与图;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-13 23:17
 */
public class KMP {

    /**
     * 构建 next 数组（前缀函数）。
     * next[i] = pat[0..i-1] 的最长相同前后缀长度。
     */
    static int[] buildNext(String pat) {
        int m = pat.length();
        int[] next = new int[m + 1];
        next[0] = 0;
        if (m > 0) next[1] = 0;

        int j = 0;   // j = 当前最长前后缀长度

        for (int i = 1; i < m; i++) {
            // 失配：回退 j
            while (j > 0 && pat.charAt(i) != pat.charAt(j))
                j = next[j];

            // 匹配：j 前进
            if (pat.charAt(i) == pat.charAt(j))
                j++;

            next[i + 1] = j;
        }
        return next;
    }

    /**
     * 在 text 中查找 pat 第一次出现的位置。
     * @return 起始下标；找不到返回 -1
     */
    static int search(String text, String pat) {
        if (pat.isEmpty()) return 0;

        int[] next = buildNext(pat);
        int n = text.length(), m = pat.length();
        int j = 0;   // 模式串已匹配长度

        for (int i = 0; i < n; i++) {
            // 失配：j 沿 next 回退
            while (j > 0 && text.charAt(i) != pat.charAt(j))
                j = next[j];

            // 匹配：j 前进
            if (text.charAt(i) == pat.charAt(j))
                j++;

            // 完全匹配
            if (j == m) {
                return i - m + 1;   // 起始位置
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        String text = "ABABDABACDABABCABAB";
        String pat  = "ABABCABAB";

        int pos = search(text, pat);
        System.out.println("第一次出现位置：" + pos);   // 10

        int[] next = buildNext(pat);
        System.out.println(java.util.Arrays.toString(next));
        // [0, 0, 0, 1, 2, 0, 1, 2, 3, 4]
    }
}
