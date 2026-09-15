package OD.数组与字符串;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-15 13:26
 */
public class 一次字典变 {

    class Solution {

        /**
         * 最多交换一次字符串中的两个字符，使得结果字典序最小。
         * 如果已经是最小（即已升序），原样返回。
         * <p>
         * 核心思路：
         * 从左到右找第一个"放错位置"的位置 i（value[i] != 排序后[i]）
         * 在 i 后面找一个字符，使得交换后 value[i] 变成正确的最小字符
         * 交换后立即结束（只交换一次）
         * <p>
         * 例：value = "dcba"
         * 排序后 sorted = "abcd"
         * i=0: 'd' != 'a'  →  在 i 后面找 'a'
         * 找到 j=3（output[3]='a'）
         * 交换 → "acbd"
         * 返回 "acbd"
         *
         * @param value 输入字符串
         * @return 交换一次后字典序最小的字符串（可能和原串相同）
         */
        String minimumSingleSwapString(String value) {

            // ---- 1. 生成"升序排列"的目标串，作为正确顺序的参照 ----
            char[] ordered = value.toCharArray();
            java.util.Arrays.sort(ordered);
            String sorted = new String(ordered);

            // ---- 2. 如果原串已经升序，就是最小的，直接返回 ----
            if (sorted.equals(value))
                return value;

            // ---- 3. 在 value 的副本上做一次交换 ----
            char[] output = value.toCharArray();

            // 从左往右扫描，找第一个"字符放错位置"的下标 i
            for (int i = 0; i < output.length; i++)
                if (value.charAt(i) != ordered[i]) {

                    // 在 i 之后找一个字符，使 output[i] 能变成 ordered[i]
                    // 从右往左找最后一个等于 ordered[i] 的字符
                    // （这样能把较小的字符换到前面，且尽量保留小的在后面）
                    int swap = -1;
                    for (int j = i + 1; j < output.length; j++)
                        if (output[j] == ordered[i])
                            swap = j;   // 不断更新，最终记录"最后一个"匹配位置

                    // 交换 i 和 swap
                    char temporary = output[i];
                    output[i] = output[swap];
                    output[swap] = temporary;

                    // 只交换一次，立刻结束
                    break;
                }

            return new String(output);
        }
    }

}
