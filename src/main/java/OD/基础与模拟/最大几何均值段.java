package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 16:28
 */
public class 最大几何均值段 {


    class Solution {
        // 二分查找的精度阈值（1e-10），用于控制迭代终止
        static final double BATCH64_EPS = 1e-10;

        // 辅助类，用于存储检查结果：是否找到、起始索引、长度
        class Check {
            boolean found;   // 是否找到满足条件的子数组
            int start;       // 子数组的起始索引
            int length;      // 子数组的长度

            Check(boolean f, int s, int l) {
                found = f;
                start = s;
                length = l;
            }
        }

        /**
         * 检查是否存在长度至少为 minimumLength 的子数组，
         * 使得该子数组的对数平均值 >= target（在浮点误差范围内）
         *
         * @param values 原始数组取对数后的值（double[]）
         * @param minimumLength 子数组的最小长度要求
         * @param target 当前尝试的目标对数平均值
         * @return Check 对象，包含是否找到、最优子数组的起始和长度
         */
        Check batch64Check(double[] values, int minimumLength, double target) {
            // 构建前缀和数组 prefix，prefix[i] 表示 values[0..i-1] 减去 target 的累计和
            double[] prefix = new double[values.length + 1];
            for (int i = 0; i < values.length; i++)
                prefix[i + 1] = prefix[i] + values[i] - target;

            double minimum = 0;          // 当前遇到的最小前缀和
            int minimumIndex = 0;        // 最小前缀和对应的索引
            int start = 0, length = values.length + 1; // 记录最佳子数组的起始和长度
            boolean found = false;

            // 遍历右端点 right，维护一个长度至少为 minimumLength 的窗口
            for (int right = minimumLength; right <= values.length; right++) {
                // 如果 prefix[right] - minimum >= -EPS，说明存在一个子数组（从 minimumIndex 到 right-1）
                // 其对数平均值 >= target（因浮点误差使用 -EPS 做宽松比较）
                if (prefix[right] - minimum >= -BATCH64_EPS) {
                    int current = right - minimumIndex; // 当前子数组长度
                    // 选择更短的长度，若长度相同则选择起始索引更小的
                    if (!found || current < length || (current == length && minimumIndex < start)) {
                        start = minimumIndex;
                        length = current;
                    }
                    found = true;
                }

                // 更新最小前缀和：考虑将 left 向右移动（保持窗口长度 >= minimumLength）
                int candidate = right - minimumLength + 1;
                if (prefix[candidate] < minimum) {
                    minimum = prefix[candidate];
                    minimumIndex = candidate;
                }
            }
            return new Check(found, start, length);
        }

        /**
         * 主方法：寻找具有最大几何平均值的子数组
         * （几何平均值 = exp(对数平均值)）
         *
         * @param numbers 原始正数数组（必须全为正数，因为要对数运算）
         * @param minimumLength 子数组的最小长度
         * @return long[] 包含两个元素：最佳子数组的起始索引和长度
         */
        long[] maximumGeometricSubarray(double[] numbers, int minimumLength) {
            if (numbers.length == 0)
                return new long[] {0, minimumLength}; // 空数组直接返回默认值

            // 将原始数组转换为对数形式（便于将几何平均问题转化为算术平均问题）
            double[] values = new double[numbers.length];
            double low = Double.POSITIVE_INFINITY, high = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < numbers.length; i++) {
                values[i] = Math.log(numbers[i]); // 取自然对数
                low = Math.min(low, values[i]);   // 对数最小值
                high = Math.max(high, values[i]); // 对数最大值
            }

            int bestStart = 0, bestLength = minimumLength; // 初始最佳解

            // 二分搜索最大可能的目标对数平均值
            while (high - low > BATCH64_EPS) {
                double target = (low + high) / 2; // 当前猜测的目标值
                Check result = batch64Check(values, minimumLength, target);

                if (result.found) {
                    // 如果存在满足条件的子数组，说明目标值可以更大，调整下界
                    low = target;
                    bestStart = result.start;
                    bestLength = result.length;
                } else {
                    // 否则目标值太大，调整上界
                    high = target;
                }
            }

            // 返回最佳子数组的起始索引和长度
            return new long[] {bestStart, bestLength};
        }
    }

}
