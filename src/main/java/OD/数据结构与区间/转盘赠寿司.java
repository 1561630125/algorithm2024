package OD.数据结构与区间;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 15:05
 */
public class 转盘赠寿司 {

    long[] discountedSushiTotals(long[] prices) {
        long[] res = new long[prices.length];
        for(int i = 0; i < prices.length; i++) {
            int di = ((i % prices.length) + prices.length) % prices.length;
            if (prices[di] < prices[i] && di != i) {
                res[i] = prices[di] + prices[i];
            }else {
                res[i] = prices[i];
            }
        }
        return res;
    }


    /**
     * 计算折扣后的寿司总价（循环数组 + 单调栈）
     *
     * 题目背景：
     * 有一排寿司围成圆圈，每个寿司有价格 prices[i]
     * 对于每个寿司，找到它后面第一个价格比它小的寿司
     * 折扣价 = 当前寿司价格 + 那个更小寿司的价格
     * 如果没有找到更小的，则保持原价
     *
     * 例如：
     * prices = [4, 5, 3, 2, 6]
     *
     * 寿司0（价格4）：后面第一个更小的是索引2（价格3）
     *   总价 = 4 + 3 = 7
     *
     * 寿司1（价格5）：后面第一个更小的是索引2（价格3）
     *   总价 = 5 + 3 = 8
     *
     * 寿司2（价格3）：后面第一个更小的是索引3（价格2）
     *   总价 = 3 + 2 = 5
     *
     * 寿司3（价格2）：后面没有更小的（循环到末尾）
     *   总价 = 2（保持不变）
     *
     * 寿司4（价格6）：后面第一个更小的是索引0（价格4）因为是循环
     *   总价 = 6 + 4 = 10
     *
     * 返回：[7, 8, 5, 2, 10]
     *
     * @param prices 寿司价格数组（环形排列）
     * @return 每个寿司的折扣后总价
     */
    static long[] discountedSushiTotals2(long[] prices) {
        int count = prices.length;

        // ========== 1. 初始化 ==========
        // 结果数组：先复制原始价格（如果没有折扣，保持原价）
        long[] result = prices.clone();
        if (count == 0) return result;

        // ========== 2. 单调栈（存储索引） ==========
        // unresolved：存储尚未找到更小价格的寿司索引
        // 栈底到栈顶：价格递增（单调递增栈）
        int[] unresolved = new int[count];
        int size = 0;  // 栈的大小

        // ========== 3. 循环遍历（模拟环形） ==========
        // 需要遍历 2*count - 1 次，保证每个元素都能看到后面的元素
        // 包括循环到数组开头
        for (int step = 0; step < count * 2 - 1; step++) {
            int index = step % count;  // 环形索引

            // ====== 关键：单调栈处理 ======
            // 当前价格小于栈顶价格时，说明找到了栈顶元素的"下一个更小元素"
            while (size > 0 && prices[index] < prices[unresolved[size - 1]]) {
                int original = unresolved[--size];  // 弹出栈顶
                // 折扣价 = 原价 + 当前更小价格
                result[original] = prices[original] + prices[index];
            }

            // ====== 只在前 count 次时入栈 ======
            // 第一次遍历：所有元素入栈
            // 第二次遍历：只用于查找更小元素，不再入栈
            if (step < count) {
                unresolved[size++] = index;
            }
        }

        // ========== 4. 返回结果 ==========
        // 没有找到更小价格的元素，保持原价（已经在 result 中）
        return result;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(discountedSushiTotals2(new long[]{5, 5, 4})));
    }

}
