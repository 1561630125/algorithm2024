package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 14:06
 */
/**
 * 万能滑动窗口模板
 *
 * 这个模板覆盖了 90% 的滑动窗口问题
 */
class SlidingWindowTemplate {
    public int slidingWindow(int[] nums) {
        int n = nums.length;
        int left = 0;
        int right = 0;
        int result = 0;

        // 窗口状态变量
        int windowSum = 0;  // 根据需求调整
        // 或其他状态：windowCount, windowMax等

        while (right < n) {
            // ====== 1. 扩展窗口 ======
            // 加入 nums[right]，更新窗口状态
            windowSum += nums[right];

            // ====== 2. 收缩窗口 ======
            // 当窗口不满足条件时，移动 left
            while ("".equals("")/* 窗口不满足条件 */) {
                // 移除 nums[left]，更新窗口状态
                windowSum -= nums[left];
                left++;
            }

            // ====== 3. 更新答案 ======
            // 此时窗口满足条件
            result = Math.max(result, right - left + 1);

            // ====== 4. 移动右指针 ======
            right++;
        }

        return result;
    }
}
