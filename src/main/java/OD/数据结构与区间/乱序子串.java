package OD.数据结构与区间;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 12:13
 */
public class 乱序子串 {

    static long associatedSubstringIndex(String pattern, String text) {
        int[] pa = new int[26];
        for(int i = 0; i < pattern.length(); i++) {
            pa[(int)pattern.charAt(i) - 'a'] = pa[(int)pattern.charAt(i) - 'a'] + 1;
        }

        int[] arr = new int[26];
        int left = 0;
        for(int i = 0; i < text.length(); i++) {
            arr[(int)text.charAt(i) - 'a'] = arr[(int)text.charAt(i) - 'a'] + 1;
            if (Arrays.equals(pa,arr)) {
                return left;
            }

            if (i - left + 1 == pattern.length()) {
                arr[(int)text.charAt(left) - 'a'] = arr[(int)text.charAt(left) - 'a'] - 1;
                left++;
            }
        }

        return -1;
    }


    long associatedSubstringIndex2(String pattern, String text) {
        if (pattern.length() > text.length())
            return -1;
        int[] wanted = new int[26], window = new int[26];
        for (char value : pattern.toCharArray())
            wanted[value - 'a']++;
        for (int i = 0; i < text.length(); i++) {
            window[text.charAt(i) - 'a']++;
            if (i >= pattern.length())
                window[text.charAt(i - pattern.length()) - 'a']--;
            if (i + 1 >= pattern.length() && java.util.Arrays.equals(wanted, window))
                return i - pattern.length() + 1;
        }
        return -1;
    }


    public static void main(String[] args) {
        System.out.println(associatedSubstringIndex("abc","iiiabcihgfe"));
        System.out.println(associatedSubstringIndex("abc","fghicbaiiie"));
    }

}
