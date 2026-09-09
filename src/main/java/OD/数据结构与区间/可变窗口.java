package OD.数据结构与区间;

import java.util.HashSet;
import java.util.Set;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 22:09
 */
public class 可变窗口 {

    // 最长无重复子串
    static public int lengthOfLongestSubstring(String s) {
        int left = 0, maxLen = 0;
        Set<Character> set = new HashSet<>();

        for (int right = 0; right < s.length(); right++) {
            // 窗口不合法时，不断缩小左边界
            int loop = 0;
            while (set.contains(s.charAt(right))) {
                loop++;
                if (loop > 1) {
                    System.out.println(right);
                }
                set.remove(s.charAt(left));
                left++;  // ← 可能移动多次，用 while
            }
            set.add(s.charAt(right));
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }

    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring("abcabcbb"));
    }

}
