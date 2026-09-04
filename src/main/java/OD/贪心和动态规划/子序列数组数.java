package OD.贪心和动态规划;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-03 17:22
 */
public class 子序列数组数 {
    int maxSubsequenceCopies(String source, String target) {
        if (target.isEmpty()) return 0;
        java.util.Map<Character, Integer> positions = new java.util.HashMap<>();
        for (int index = 0; index < target.length(); index++) positions.put(target.charAt(index), index);
        int[] counts = new int[target.length()];
        for (int index = 0; index < source.length(); index++) {
            Integer position = positions.get(source.charAt(index));
            if (position == null) continue;
            if (position == 0) counts[0]++;
            else if (counts[position - 1] > 0) { counts[position - 1]--; counts[position]++; }
        }
        return counts[counts.length - 1];
    }
}
