package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 17:39
 */
public class 错峰充电 {

    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     *
     * 错峰充电
     * @param priceRecords int — 电价记录数量
     * @param hours int — 连续充电小时数
     * @param prices int[] — 每小时电价
     * @return int
     */
    public int bestChargingStart(int priceRecords, int hours, int[] prices) {
        // write code here

        int right = prices.length-1;
        int cur = 0 ;
        for(int i = prices.length-1; i>0; i--) {
            cur = cur + prices[i];
            if (right - i == hours) {
               right--;
            }

            cur = cur - prices[right] + prices[i];

        }

        return 0;
    }

    public int bestChargingStart3(int priceRecords, int hours, int[] prices) {
        // 1. 先算最右边的窗口：[priceRecords-hours, priceRecords-1]
        long total = 0;
        for (int i = priceRecords - hours; i < priceRecords; i++) {
            total += prices[i];
        }

        long best = total;
        int answer = priceRecords - hours;  // 最右窗口的起始下标

        // 2. 窗口左移：start 从 priceRecords-hours-1 递减到 0
        for (int start = priceRecords - hours - 1; start >= 0; start--) {
            // 新加入左边 prices[start]，移出右边 prices[start + hours]
            total += prices[start] - prices[start + hours];

            if (total < best) {
                best = total;
                answer = start;
            }
        }

        return answer;
    }


    public int bestChargingStart2(int priceRecords, int hours, int[] prices) {
        // 1. 先计算第一个窗口（从下标 0 开始，长度 hours）的总和
        long total = 0;
        for (int index = 0; index < hours; index++)
            total += prices[index];

        long best = total;  // 记录目前最小的窗口和
        int answer = 0;     // 记录最小和对应的起始下标

        // 2. 滑动窗口：从 start = 1 开始，每次窗口右移一位
        // start + hours <= priceRecords 保证窗口不越界
        for (int start = 1; start + hours <= priceRecords; start++) {

            // 窗口右移：加上新进入的元素 prices[start + hours - 1]
            //          减去离开窗口的元素 prices[start - 1]
            total += prices[start + hours - 1] - prices[start - 1];

            // 如果当前窗口和更小，更新最优解
            if (total < best) {
                best = total;
                answer = start;
            }
        }

        return answer;
    }

}
