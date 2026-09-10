package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 17:27
 */
public class 台间区号整理 {

    static public String mergeStations(String stations) {
        // 1. 用 TreeSet 收集所有站点首字母，自动去重 + 升序排序
        java.util.SortedSet<Character> set = new java.util.TreeSet<>();

        // 按空白字符拆分字符串（\\s+ 匹配一个或多个空白）
        for (String token : stations.trim().split("\\s+")) {
            if (!token.isEmpty())
                set.add(token.charAt(0));  // 只取每个站点的首字母
        }

        // 2. 把 TreeSet 转成 List，方便用下标访问
        java.util.List<Character> letters = new java.util.ArrayList<>(set);

        StringBuilder answer = new StringBuilder();

        // 3. 扫描有序字母表，把连续字母合并成区间
        for (int left = 0; left < letters.size(); ) {

            // right 从 left 开始，尝试向右扩展到连续字母的末尾
            int right = left;
            while (
                    right + 1 < letters.size() &&
                            letters.get(right + 1) == letters.get(right) + 1
            )
                right++;

            // 此时 [left, right] 是一段连续字母

            // 不是第一个区间就加逗号分隔
            if (answer.length() > 0)
                answer.append(',');

            if (right - left >= 2) {
                // 区间长度 >= 3，用 "起-止" 表示，如 A-C
                answer.append(letters.get(left))
                        .append('-')
                        .append(letters.get(right));
            } else {
                // 区间长度 <= 2，直接列出每个字母
                answer.append(letters.get(left));
                if (right > left)
                    answer.append(',').append(letters.get(right));
            }

            // 跳到下一段连续区间的起点
            left = right + 1;
        }

        return answer.toString();
    }

    public static void main(String[] args) {
        System.out.println(mergeStations("A B C D K O P"));
    }

}
