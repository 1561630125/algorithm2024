package OD.基础与模拟;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 14:35
 */
public class 把数字分为两组 {

    /**
     * 计算将数组分成两组后，两组极差之和的最小值
     *
     * 极差 = 组内最大值 - 组内最小值
     *
     * @param n    数组长度（实际使用的元素个数）
     * @param nums 原始数组
     * @return 两组极差之和的最小值
     */
    public long minGroupValue(int n, int[] nums) {
        // 1. 复制前n个元素并排序
        int[] values = java.util.Arrays.copyOf(nums, n);
        java.util.Arrays.sort(values);

        // 2. 特殊情况：少于等于2个元素，无法分成两组都有元素
        if (n <= 2)
            return 0;

        // 3. 初始答案：所有元素在一组时的极差（最大-最小）
        long answer = (long)values[n - 1] - values[0];

        // 4. 枚举所有可能的分割点（将排序后的数组分成左右两组）
        //    分割点 index 表示：左组包含 values[0..index]，右组包含 values[index+1..n-1]
        for (int index = 0; index + 1 < n; index++) {
            // 左组极差：values[index] - values[0]
            // 右组极差：values[n - 1] - values[index + 1]
            // 两组极差之和
            long current = (long)values[index] - values[0]
                    + values[n - 1] - values[index + 1];

            // 更新最小值
            answer = Math.min(answer, current);
        }

        return answer;
    }

    public long minGroupValue2(int n, int[] nums) {
        //sort
        Arrays.sort(nums);
        if (nums.length % 2 == 0) {

        }else {

        }

        // [全小、全大]、
        return 0;
    }

    public static void main(String[] args) {

    }

}
