package OD.数据结构与区间;

import java.util.LinkedList;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 12:13
 */
public class 动态兵营窗口 {

    /**
     * 监控敌方营地（滑动窗口最小值查询 + 单点更新）
     *
     * 题目背景：
     * 有 n 个营地，每个营地有一定数量的敌军。需要执行两类操作：
     * 1. 更新操作：增加或减少某个营地的敌军数量
     * 2. 查询操作：在指定区间内，所有长度为 windowSize 的连续子区间中，
     *    敌军总数的最小值是多少
     *
     * 例如：
     * camps = [1, 3, 5, 7, 9], windowSize = 3
     * 查询 [2, 4]（索引1到3，即 [3, 5, 7]）
     *   子区间：长度为3的子区间只有 [3, 5, 7]，总和 = 15
     *   返回：15
     *
     * 查询 [1, 5]（所有营地 [1, 3, 5, 7, 9]）
     *   子区间：长度为3的子区间有：
     *     [1, 3, 5] = 9
     *     [3, 5, 7] = 15
     *     [5, 7, 9] = 21
     *   返回：9
     *
     * @param windowSize 滑动窗口的大小
     * @param camps 初始各营地的敌军数量
     * @param operations 操作数组，每个操作格式：
     *                   ["Add", "1", "10"]  → 第1个营地增加10
     *                   ["Sub", "2", "5"]   → 第2个营地减少5（最少到0）
     *                   ["Query", "1", "4"] → 查询区间[1,4]内的最小窗口和
     * @return 所有查询操作的结果数组
     */
    long[] monitorEnemyCamps(int windowSize, long[] camps, String[][] operations) {
        // ========== 复制初始数据，用于后续更新 ==========
        long[] values = java.util.Arrays.copyOf(camps, camps.length);

        // ========== 存储所有查询结果 ==========
        java.util.ArrayList<Long> result = new java.util.ArrayList<>();

        // ========== 遍历所有操作 ==========
        for (String[] operation : operations) {
            // 跳过无效操作
            if (operation == null || operation.length < 3)
                continue;

            String command = operation[0];      // 操作类型：Add/Sub/Query
            int first = Integer.parseInt(operation[1]);   // 第一个参数（位置）
            long second = Long.parseLong(operation[2]);   // 第二个参数（数值或结束位置）

            // ========== 操作1: Add - 增加敌军 ==========
            if (command.equals("Add")) {
                if (first >= 1 && first <= values.length)
                    values[first - 1] += second;  // 转换为0-based索引
            }
            // ========== 操作2: Sub - 减少敌军（最低为0） ==========
            else if (command.equals("Sub")) {
                if (first >= 1 && first <= values.length)
                    values[first - 1] = Math.max(0L, values[first - 1] - second);
            }
            // ========== 操作3: Query - 查询最小窗口和 ==========
            else {
                int start = first - 1;           // 区间起始位置（0-based）
                int end = (int) second - 1;      // 区间结束位置（0-based）

                // ========== 参数校验 ==========
                // windowSize 不能为负数
                // start 必须在有效范围内
                // start + windowSize 不能超过数组长度（保证第一个窗口完整）
                if (windowSize < 0 || start < 0 || start + windowSize > values.length) {
                    result.add(0L);  // 无效查询返回 0
                    continue;
                }

                // ========== 计算第一个窗口的和 ==========
                long current = 0L;
                for (int i = start; i < start + windowSize; i++)
                    current += values[i];
                long minimum = current;  // 初始最小值

                // ========== 滑动窗口：逐个向右移动 ==========
                // 从 start+1 开始，直到 end - windowSize + 1（最后一个完整窗口的起始位置）
                // 同时确保窗口不超出数组边界
                for (int i = start + 1;
                     i <= end - windowSize + 1 && i + windowSize - 1 < values.length;
                     i++) {
                    // 滑动窗口：移除左边的元素，添加右边的元素
                    current = current - values[i - 1] + values[i + windowSize - 1];
                    // 更新最小值
                    minimum = Math.min(minimum, current);
                }

                // 记录查询结果
                result.add(minimum);
            }
        }

        // ========== 将 ArrayList<Long> 转换为 long[] ==========
        long[] output = new long[result.size()];
        for (int i = 0; i < output.length; i++)
            output[i] = result.get(i);
        return output;
    }


    /**
     * 统计 [i, j] 范围内所有长度为 K 的窗口和
     *
     * @param arr 原始数组
     * @param K 窗口大小
     * @param i 区间左边界（0-based）
     * @param j 区间右边界（0-based）
     * @return 所有窗口和的列表
     */
    java.util.List<Long> getAllWindowSums(long[] arr, int K, int i, int j) {
        int n = arr.length;

        // ========== 步骤1: 预处理所有窗口和 ==========
        long[] windowSums = new long[n - K + 1];

        // 计算第一个窗口和
        long currentSum = 0;
        for (int idx = 0; idx < K; idx++) {
            currentSum += arr[idx];
        }
        windowSums[0] = currentSum;

        // 滑动窗口计算所有窗口和
        for (int idx = 1; idx < windowSums.length; idx++) {
            currentSum = currentSum - arr[idx - 1] + arr[idx + K - 1];
            windowSums[idx] = currentSum;
        }

        // ========== 步骤2: 提取 [i, j] 范围内的窗口和 ==========
        java.util.List<Long> result = new java.util.ArrayList<>();

        // 计算在 [i, j] 范围内，窗口的起始位置范围
        int startWin = Math.max(0, i);           // 窗口起始至少为 i
        int endWin = Math.min(j - K + 1, n - K); // 窗口结束最多为 j-K+1

        for (int winStart = startWin; winStart <= endWin; winStart++) {
            result.add(windowSums[winStart]);
        }

        return result;
    }

    java.util.List<Long> getAllWindowSums2(long[] arr, int K, int i, int j) {
        // 利用前缀和快速计算任意窗口和
        long[] prefix = new long[arr.length + 1];
        for (int idx = 0; idx < arr.length; idx++) {
            prefix[idx + 1] = prefix[idx] + arr[idx];
        }

        // 查询时动态计算每个窗口
        for (int start = i; start <= j - K + 1; start++) {
            long windowSum = prefix[start + K] - prefix[start];
            // 处理 windowSum
        }
        return new LinkedList<>();
    }


}
