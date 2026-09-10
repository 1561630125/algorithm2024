package OD.数据结构与区间;

import java.util.HashSet;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 16:53
 */
public class 双能源连续时段 {

    /**
     *
     * 双能源连续时段
     * @param types int[] — 各时刻能源类型
     * @return int
     */
    static public int longestEnergyPeriod(int[] types) {
        // write code here

        HashSet<Integer> type  = new HashSet<>();
        int left = 0;
        int res = 0;
        for(int i = 0; i < types.length; i++) {
            type.add(types[i]);
            if (type.size() > 2) {
                res =  Math.max(i - left + 1, res);
                int type1 = types[left];
                type.remove(type1);
                while (left < i && type1 == types[left]) {
                    left++;
                }
            }
        }

        res =  Math.max(types.length - left, res);


        return res;
    }

    public int longestEnergyPeriod2(int[] types) {
        // counts: 记录当前窗口内每种类型出现的次数
        java.util.Map<Integer, Integer> counts = new java.util.HashMap<>();

        int left = 0;    // 滑动窗口左边界
        int answer = 0;  // 记录满足条件的最长窗口长度

        // right 是滑动窗口右边界，逐步向右扩展
        for (int right = 0; right < types.length; right++) {

            // 1. 把当前元素加入窗口，计数 +1
            counts.put(types[right], counts.getOrDefault(types[right], 0) + 1);

            // 2. 如果窗口内类型数超过 2，就收缩左边界，直到类型数 <= 2
            while (counts.size() > 2) {
                int value = types[left++];              // 取出左边界元素，左指针右移
                int count = counts.get(value) - 1;      // 该元素计数 -1
                if (count == 0)
                    counts.remove(value);               // 计数归零就移除，保证 size() 反映真实类型数
                else
                    counts.put(value, count);
            }

            // 3. 此时窗口内最多 2 种类型，更新答案
            answer = Math.max(answer, right - left + 1);
        }

        return answer;
    }


    public static void main(String[] args) {
        System.out.println(longestEnergyPeriod(new int[]{1,2,2,3,2,2,1}));
    }

}
