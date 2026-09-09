package OD.数据结构与区间;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 21:48
 */
public class 双人车最少数 {

    int minVehicles(int capacity, int[] weights) {
        // weights排序
        Arrays.sort(weights);

        int res = 0;
        int left = 0;
        int right = weights.length - 1;

        while (left <= right) {
            if (weights[right] == capacity) {
                res++;
                right--;
            } else if (weights[right] + weights[left] > capacity) {
                res++;
                right--;
            } else if (weights[right] + weights[left] <= capacity) {
                res++;
                left++;
                right--;
            }
        }
        return res;
    }


    /**
     * 计算运输所有货物所需的最少车辆数
     *
     * 问题场景：每辆车有最大载重 capacity，每件货物有重量 weights[i]
     * 目标：用最少的车辆运完所有货物
     *
     * 核心策略：贪心算法
     * 1. 最重的货物必须用一辆车，尽量搭配最轻的货物
     * 2. 排序后使用双指针从两端向中间移动
     *
     * 时间复杂度：O(n log n) - 排序开销
     * 空间复杂度：O(n) - 复制数组
     *
     * @param capacity 每辆车的最大载重量
     * @param weights  每件货物的重量数组
     * @return 最少需要的车辆数
     */
    public int minVehicles2(int capacity, int[] weights) {
        // 复制并排序，避免修改原数组
        int[] ordered = java.util.Arrays.copyOf(weights, weights.length);
        java.util.Arrays.sort(ordered);  // 升序排列：[轻, ..., 重]

        int left = 0;                    // 指向最轻的货物
        int right = ordered.length - 1;  // 指向最重的货物
        int result = 0;                  // 已使用的车辆数

        /**
         * 双指针从两端向中间靠拢
         *
         * 每次循环处理一辆车：
         * - 最重的货物必须单独或搭配最轻的货物
         * - 如果能搭配（重量和 <= capacity），则轻的也上车
         * - 否则最重的单独上车
         */
        while (left <= right) {
            /**
             * 尝试将最轻和最重的货物装在同一辆车上
             *
             * 如果两者重量之和 <= capacity：
             *   - 可以搭配，最轻的也上车，left右移
             *   例如：capacity=10, weights=[2, 3, 7, 8]
             *         left=0(2), right=3(8), 2+8=10 <= 10 ✓
             *         所以把2和8装在同一辆车上
             *
             * 否则：
             *   - 最重的太重，无法搭配任何轻货物
             *   - 最重的单独装一辆车
             *   例如：capacity=10, weights=[3, 9]
             *         left=0(3), right=1(9), 3+9=12 > 10 ✗
             *         所以9单独装一辆车
             */
            if (ordered[left] + ordered[right] <= capacity) {
                left++;  // 最轻的货物也装上了，指针右移
            }
            // 无论是否搭配成功，最重的货物都装上了
            right--;      // 最重的货物已处理，指针左移
            result++;     // 使用了一辆车
        }

        return result;
    }

}
