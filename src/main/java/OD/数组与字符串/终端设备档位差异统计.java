package OD.数组与字符串;

import java.util.Arrays;
import java.util.HashSet;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-18 20:20
 */
public class 终端设备档位差异统计 {
    public static int countProfilePairs(int[] profiles, int diff) {
        // write code here

        Arrays.sort(profiles);

        HashSet<String> seen = new HashSet<>();
        for (int i = 0; i < profiles.length; i++) {
            for (int j = i + 1; j < profiles.length; j++) {
                if (Math.abs(profiles[i] - profiles[j]) == diff) {
                    seen.add(profiles[i] + "," + profiles[j]);
                }
            }
        }
        return seen.size();
    }

    static public class Solution {

        /**
         * 统计满足条件的“配对”数量。
         *
         * 参数：
         *   profiles —— 原始数组（可能含重复值）
         *   diff     —— 目标差值
         *
         * 返回值：
         *   当 diff == 0 时：返回“出现次数 >= 2 的不同数值”的个数
         *   当 diff != 0 时：返回“差值恰好等于 diff”的无序数对个数
         *
         * 核心思路：
         *   先排序 + 去重，再利用双指针在有序唯一数组上线性扫描。
         */
        public int countProfilePairs(int[] profiles, int diff) {

            // ------------------------------------------------------------
            // 第一步：拷贝并排序
            // 不直接修改原数组，避免副作用。
            // 排序后相同元素相邻，方便去重和双指针扫描。
            // ------------------------------------------------------------
            int[] ordered = profiles.clone();
            Arrays.sort(ordered);

            int unique = 0;   // 去重后写入 ordered 的位置指针
            int answer = 0;   // 最终答案

            // ------------------------------------------------------------
            // 第二步：遍历有序数组，统计重复次数并原地去重
            //
            // ordered[0..unique-1] 最终保存的是去重后的升序唯一值。
            // 对于 diff == 0 的情况，只要某个值出现次数 >= 2，
            // 就说明存在一对相同值，answer++。
            // ------------------------------------------------------------
            for (int index = 0; index < ordered.length; ) {

                // 找到与 ordered[index] 相同的连续区间 [index, end)
                int end = index + 1;
                while (end < ordered.length && ordered[end] == ordered[index]) {
                    end++;
                }

                // 该值出现次数 = end - index
                // diff == 0 时，出现次数 >= 2 就构成一对
                if (diff == 0 && end - index >= 2) {
                    answer++;
                }

                // 原地去重：把当前唯一值写到 ordered[unique]
                ordered[unique++] = ordered[index];

                // 跳到下一个不同值
                index = end;
            }

            // ------------------------------------------------------------
            // 第三步：diff == 0 的特殊情况已经统计完，直接返回
            // 此时 answer = 出现次数 >= 2 的不同数值个数
            // ------------------------------------------------------------
            if (diff == 0) {
                return answer;
            }

            // ------------------------------------------------------------
            // 第四步：diff != 0，在去重后的有序唯一数组上做双指针扫描
            //
            // ordered[0..unique-1] 是升序唯一值。
            // left 和 right 是两个下标，始终保持 left < right。
            // 计算 gap = ordered[right] - ordered[left]：
            //   gap < diff ：需要更大差值 -> right 右移
            //   gap > diff ：需要更小差值 -> left 右移
            //   gap == diff：找到一对 -> answer++，left 和 right 同时右移
            //
            // 注意：因为数组已去重，每个值只出现一次，
            // 所以每对 (left, right) 只会计数一次，不会重复。
            // ------------------------------------------------------------
            for (int left = 0, right = 1; right < unique; ) {

                // 用 long 计算差值，避免 int 溢出（例如 Integer.MAX_VALUE - (-1)）
                long gap = (long) ordered[right] - ordered[left];

                if (gap < diff) {
                    // 差值太小，右指针右移以增大差值
                    right++;
                } else if (gap > diff) {
                    // 差值太大，左指针右移以减小差值
                    left++;
                } else {
                    // 差值正好等于 diff，找到一对
                    answer++;
                    left++;
                    right++;
                }

                // 保证 left < right
                // 当 left 追上 right 时，把 right 推后一位
                if (left == right) {
                    right++;
                }
            }

            return answer;
        }
    }

    public static int countProfilePairs2(int[] profiles, int diff) {
        if (diff < 0) diff = -diff;
        int[] ordered = profiles.clone();
        Arrays.sort(ordered);

        // 去重
        int unique = 0;
        for (int i = 0; i < ordered.length; ) {
            int j = i + 1;
            while (j < ordered.length && ordered[j] == ordered[i]) j++;
            ordered[unique++] = ordered[i];
            i = j;
        }

        int answer = 0;
        int left = 0, right = 1;
        while (right < unique) {
            long gap = (long) ordered[right] - ordered[left];
            if (gap < diff) right++;
            else if (gap > diff) left++;
            else { answer++; left++; right++; }
            if (left == right) right++;
        }
        return answer;
    }


    public static void main(String[] args) {
        int[] profiles = new int[]{1,5,3,4,2};
        System.out.println(countProfilePairs(profiles, 2));

        int[] profiles2 = new int[]{5,5,5};
        System.out.println(countProfilePairs(profiles2, 0));
        Solution solution = new Solution();
        System.out.println(solution.countProfilePairs(profiles2, 0));
    }
}
