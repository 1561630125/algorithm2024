package OD.数据结构与区间;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 23:18
 */
public class 差值靠中 {

    static class Win {
        long sum;
        int index;

        public Win(long sum, int index) {
            this.sum = sum;
            this.index = index;
        }
    }

    static long closestExpressionIndex(long[] numbers, int windowLength) {

        int left = 0;
        long cur = 0;
        LinkedList<Win> wins = new LinkedList<>();
        for (int i = 0; i < numbers.length; i++) {
            cur += numbers[i];

            if (i - left + 1 == windowLength) {
                wins.add(new Win(numbers[left] - (cur - numbers[left]), left));
                cur = cur - numbers[left];
                left++;
            }
        }

        Arrays.sort(numbers);
        long mid = numbers[numbers.length / 2];

        wins.sort((a, b) -> a.sum != b.sum ? (int) (Math.abs(a.sum - mid) - Math.abs(b.sum - mid)) : b.index - a.index);


        return wins.get(0).index;
    }


    /**
     * 寻找使表达式 |numbers[left] - suffix - median| 最小的起始索引
     * <p>
     * 表达式含义：
     * - numbers[left]：窗口的第一个元素
     * - suffix：窗口中除第一个元素外的其他元素之和
     * - median：整个数组的中位数
     * <p>
     * 滑动窗口从数组末尾向开头移动，每次移动：
     * 1. 移除窗口的最后一个元素（从 suffix 中减去）
     * 2. 将新的左边界元素加入 suffix
     * 3. 更新 left 和 right 指针
     * <p>
     * 时间复杂度：O(n log n) 主要来自排序
     * 空间复杂度：O(n) 克隆数组
     *
     * @param numbers      输入数组
     * @param windowLength 窗口大小（需要连续的元素个数）
     * @return 使表达式最小的起始索引，如果没有符合条件的返回 -1
     */
    static public long closestExpressionIndex2(long[] numbers, int windowLength) {
        // 边界检查
        if (numbers.length == 0 || windowLength <= 0 || windowLength > numbers.length) {
            return -1;
        }

        /**
         * 第一步：计算整个数组的中位数
         *
         * 中位数定义：排序后位于中间位置的元素
         * 例如：[1,2,3,4,5] 的中位数是 3
         *      [1,2,3,4] 的中位数是 2（取下标 4/2 = 2，即第3个元素）
         *
         * 注意：对于偶数长度，这里取的是上中位数
         */
        long[] ordered = numbers.clone();
        java.util.Arrays.sort(ordered);
        long median = ordered[numbers.length / 2];

        /**
         * 第二步：初始化窗口
         *
         * 初始窗口位置：数组的最后 windowLength 个元素
         *   left = numbers.length - windowLength
         *   right = numbers.length - 1
         *   窗口：numbers[left] 到 numbers[right]
         *
         * suffix 初始化为窗口之外的所有元素之和
         * 注意：suffix 不包括 numbers[left]
         *
         * 例如：numbers=[1,2,3,4,5], windowLength=3
         *   left = 2, right = 4
         *   窗口：[3,4,5]
         *   suffix = numbers[3] + numbers[4] = 4 + 5 = 9
         *   或者：suffix = 从 left+1 到末尾的所有元素之和
         */
        int left = numbers.length - windowLength;
        int right = numbers.length - 1;
        long suffix = 0;

        // 累加窗口之外的所有元素（从 left+1 到末尾）
        // 这些元素是窗口中除第一个元素外的其他元素
        for (int i = left + 1; i < numbers.length; i++) {
            suffix += numbers[i];
        }

        /**
         * 第三步：滑动窗口，寻找最小表达式值
         *
         * 当前表达式：current = |numbers[left] - suffix - median|
         *
         * 窗口滑动过程（每次向左移动一步）：
         *   1. 计算当前位置的表达式值
         *   2. 更新答案
         *   3. 滑动窗口到下一个位置：
         *      a. 从 suffix 中移除窗口最后一个元素 numbers[right]
         *      b. 将新的左边界元素 numbers[left] 加入 suffix
         *      c. left--, right--
         *
         * 为什么这样更新 suffix？
         *   移动前：窗口 [left, left+1, ..., right]
         *          suffix = numbers[left+1] + ... + numbers[right] + numbers[right+1] + ... + numbers[n-1]
         *
         *   移动后：窗口 [left-1, left, ..., right-1]
         *          新suffix = numbers[left] + ... + numbers[right-1] + numbers[right] + ... + numbers[n-1]
         *
         *   所以：新suffix = 原suffix - numbers[right] + numbers[left]
         */
        long result = -1;
        long difference = Long.MAX_VALUE;

        while (left >= 0) {
            /**
             * 计算当前表达式值
             *
             * numbers[left]：窗口第一个元素
             * suffix：窗口之外的元素之和（窗口中除第一个元素外的其他元素）
             * median：整个数组的中位数
             *
             * 取绝对值，值越小表示该位置越"理想"
             */
            long current = Math.abs(numbers[left] - suffix - median);

            // 更新最优解（取最小值）
            if (current < difference) {


                difference = current;
                result = left;
            }

            /**
             * 滑动窗口向左移动一位
             *
             * 移动前：
             *   窗口：[left, left+1, ..., right]
             *   suffix：窗口之外的元素之和
             *
             * 移动后：
             *   窗口：[left-1, left, ..., right-1]
             *   新suffix：原suffix - numbers[right] + numbers[left]
             *
             * 示例：numbers=[1,2,3,4,5], windowLength=3
             *   移动前：left=2, right=4, suffix=9 (4+5)
             *   移动后：left=1, right=3, suffix=9-5+3=7 (3+4)
             *   验证：新窗口 [2,3,4]，suffix = 3+4=7 ✓
             */
            suffix -= numbers[right--];  // 移除窗口最后一个元素
            suffix += numbers[left--];   // 加入新的左边界元素
        }

        return result;
    }


    public static void main(String[] args) {
        long[] numbers = new long[]{1, 2, 9, 8, 3};
        int windowLength = 3;

//        System.out.println(closestExpressionIndex(numbers, windowLength));

        long[] numbers1 = new long[]{1, 2, 9, 8, 3};
        System.out.println(closestExpressionIndex2(numbers1, windowLength));

//        System.out.println(closestExpressionIndex4(numbers, windowLength));
    }

}
