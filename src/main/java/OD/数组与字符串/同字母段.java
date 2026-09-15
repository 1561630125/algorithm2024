package OD.数组与字符串;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 12:59
 */
public class 同字母段 {
    class Solution {
        int kthLongestCharacterRun(String value, int k) {
            java.util.Map<Character, Integer> best = new java.util.HashMap<>();
            char previous = 0;
            int count = 0;
            for (int i = 0; i < value.length(); i++) {
                char character = value.charAt(i);
                count = i > 0 && character == previous ? count + 1 : 1;
                previous = character;
                best.put(character, Math.max(best.getOrDefault(character, 0), count));
            }
            java.util.List<Integer> values = new java.util.ArrayList<>(best.values());
            values.sort(java.util.Collections.reverseOrder());
            return k >= 1 && k <= values.size() ? values.get(k - 1) : -1;
        }
    }
}
