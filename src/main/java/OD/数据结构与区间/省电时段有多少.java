package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 17:55
 */
public class 省电时段有多少 {


    public long countLowPowerPeriods(int[] power, long target, int maxHour) {
        // write code here

        int left = 0;
        for(int i = 0; i < power.length; i++) {

        }


        return 0;
    }

    public long countLowPowerPeriods2(int[] power, long target, int maxHour) {
        int left = 0;              // 滑动窗口左边界
        long total = 0;            // 当前窗口内所有元素的和
        long answer = 0;           // 统计满足条件的子数组个数

        // right 是滑动窗口右边界，逐步向右扩展
        for (int right = 0; right < power.length; right++) {

            // 1. 把当前元素加入窗口
            total += power[right];

            // 2. 当窗口不合法时，收缩左边界
            // 不合法条件有两个：
            //   a) 窗口和 total 超过 target
            //   b) 窗口长度超过 maxHour
            while (left <= right && (total > target || right - left + 1 > maxHour)) {
                total -= power[left++];   // 移出左边界元素，左指针右移
            }

            // 3. 此时窗口 [left, right] 合法
            // 以 right 结尾的合法子数组个数 = right - left + 1
            // 因为 [left..right]、[left+1..right]、...、[right..right] 都合法
            if (right >= left)
                answer += right - left + 1;
        }

        return answer;
    }

}
