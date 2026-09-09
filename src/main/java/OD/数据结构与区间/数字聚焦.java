package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 23:05
 */
public class 数字聚焦 {

    /**
     * 计算将所有小于threshold的元素聚集在一起所需的最小交换次数
     * <p>
     * 问题本质：在数组中找一个固定大小的窗口，使窗口内"坏元素"最少
     * - 目标元素：值 < threshold 的元素
     * - 非目标元素：值 >= threshold 的元素
     * - 窗口大小 = 目标元素的总个数
     * - 最小交换次数 = 窗口内非目标元素的最小数量
     * <p>
     * 为什么这样算？
     * 窗口内非目标元素数量 = 需要被交换出去的元素数量
     * 窗口外目标元素数量 = 需要被交换进来的元素数量
     * 两者相等，所以交换次数 = 窗口内非目标元素数量
     * <p>
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     *
     * @param numbers   输入数组
     * @param threshold 阈值，小于该值的元素需要聚集
     * @return 最小交换次数
     */
    public long minimumGroupingSwaps(long[] numbers, long threshold) {
        /**
         * 第一步：计算窗口大小
         * 所有小于threshold的元素都需要被聚集到同一个连续区间
         * 所以窗口大小 = 这些元素的总个数
         */
        int windowSize = 0;
        for (long value : numbers) {
            if (value < threshold) {
                windowSize++;
            }
        }

        /**
         * 边界情况：
         * - windowSize == 0：没有需要聚集的元素，不需要交换
         * - windowSize == numbers.length：所有元素都需要聚集，已经全部聚集（整个数组），不需要交换
         */
        if (windowSize == 0 || windowSize == numbers.length) {
            return 0;
        }

        long answer = numbers.length;  // 最小交换次数，初始化为最大值
        int inside = 0;               // 当前窗口内目标元素（< threshold）的数量
        int left = 0;                // 滑动窗口左边界

        /**
         * 第二步：滑动窗口寻找最优位置
         * 窗口大小固定为 windowSize
         * 每次移动一步，计算窗口内非目标元素的数量
         * 非目标元素数量 = windowSize - inside
         *
         * 这个值就是需要交换的次数：
         * - 窗口内非目标元素需要被交换出去
         * - 窗口外目标元素需要被交换进来
         * - 两者数量相等
         */
        for (int right = 0; right < numbers.length; right++) {
            // 1. 将右边界元素加入窗口
            if (numbers[right] < threshold) {
                inside++;
            }

            // 2. 当窗口达到目标大小时，计算并更新答案
            if (right - left + 1 == windowSize) {
                // 当前窗口需要的交换次数 = 窗口大小 - 窗口内目标元素数
                // = 窗口内非目标元素数
                answer = Math.min(answer, windowSize - inside);

                // 3. 滑动窗口：移除左边界元素
                if (numbers[left] < threshold) {
                    inside--;
                }
                left++;
            }
        }

        return answer;
    }
}
