package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-08 15:58
 */
public class 十位数字子串 {

    /**
     * 统计满足条件的子串数量
     * 条件：子串中恰好包含 lowercaseCount 个小写字母，且至少包含一个数字（0-9）
     *
     * 使用技巧：恰好 = 最多 - 最多(limit-1)
     * countExactly(k) = countAtMost(k) - countAtMost(k-1)
     *
     * @param text 待处理的字符串
     * @param lowercaseCount 需要恰好包含的小写字母数量
     * @return 满足条件的子串总数
     */
    static long countMatchingSubstrings(String text, int lowercaseCount) {
        // 恰好包含 lowercaseCount 个小写字母
        // = 最多包含 lowercaseCount 个 - 最多包含 lowercaseCount-1 个

        long atMost = countAtMost(text, lowercaseCount);
        long atMost1 = countAtMost(text, lowercaseCount - 1);
        return atMost - atMost1;
    }

    /**
     * 统计最多包含 limit 个小写字母，且至少包含一个数字的子串数量
     *
     * 核心思想：滑动窗口 + 最近数字位置追踪
     *
     * @param text 待处理的字符串
     * @param limit 最多允许的小写字母数量
     * @return 满足条件的子串总数
     */
    static private long countAtMost(String text, int limit) {
        // 如果限制为负数，不可能有满足条件的子串
        if (limit < 0) return 0;

        // 记录每个数字（0-9）最后一次出现的位置
        // 初始化为 -1 表示尚未出现
        int[] lastDigit = new int[10];
        java.util.Arrays.fill(lastDigit, -1);

        int left = 0;        // 滑动窗口左边界
        int letters = 0;     // 当前窗口内小写字母的数量
        long total = 0;      // 满足条件的子串总数

        // 滑动窗口：right 作为右边界不断扩展
        for (int right = 0; right < text.length(); right++) {
            char current = text.charAt(right);

            // 1. 更新窗口内容
            if (current >= 'a' && current <= 'z') {
                letters++;  // 小写字母计数增加
            } else if (current >= '0' && current <= '9') {
                lastDigit[current - '0'] = right;  // 更新数字的最后出现位置
            }
            // 其他字符（大写字母、特殊字符）被忽略

            // 2. 维护窗口：如果小写字母数量超过限制，收缩左边界
            while (letters > limit) {
                char removed = text.charAt(left++);
                if (removed >= 'a' && removed <= 'z') {
                    letters--;  // 移出的小写字母从窗口中移除
                }
                // 注意：移出的数字不需要更新 lastDigit
                // 因为 lastDigit 记录的是最后一次出现位置
                // 即使被移出窗口，只要后面没有再次出现，仍然在窗口外
                // 但如果该数字在窗口内再次出现，lastDigit 会更新为更靠右的位置
            }

            // 3. 计算以当前 right 为结尾的满足条件的子串数量
            // 找到所有数字中最近（最靠右）出现的位置
            int earliestDigit = lastDigit[0];
            for (int digit = 1; digit < 10; digit++) {
                earliestDigit = Math.min(earliestDigit, lastDigit[digit]);
            }

            // 如果至少有一个数字出现在窗口中（且位置 >= left）
            // 那么所有以 [left, earliestDigit] 范围内的位置作为起点的子串都满足条件
            if (earliestDigit >= left) {
                total += earliestDigit - left + 1L;
            }
            // 如果没有数字出现（earliestDigit == -1），则不加
        }

        return total;
    }

    long countMatchingSubstrings2(String text, int lowercaseCount) {
        // arr[i,j]统计字母个数、数字个数

        int[] nums = new int[10];
        int count = 0;
        for(int i = 0; i < text.length(); i++) {
            char charAt = text.charAt(i);
            if (Character.isLetter(charAt) && Character.isLowerCase(charAt)) {
                count++;
            }
            if ('0' <= charAt && '9' >= charAt) {
                nums[(int)charAt - '0']++;
            }


            int right = i + 9 + lowercaseCount;
            for(int j = right; j < text.length(); j++) {


            }

        }


        return 0L;
    }


    public static void main(String[] args) {
        String text= "aaa0123456789";
        System.out.println(countMatchingSubstrings(text,2));
    }
}
