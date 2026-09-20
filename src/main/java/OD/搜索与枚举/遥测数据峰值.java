package OD.搜索与枚举;

/**
 * 考点：时间窗口内的最大值
 *
 * @author faming.yang@hand-china.com 2026-09-13 16:56
 */
public class 遥测数据峰值 {

    public class Solution {

        /**
         * 对每个数据点，求时间窗口内的最大值。
         *
         * @param data     数据数组，data[i] = {时间戳, 数值}
         * @param interval 时间窗口长度
         * @return answer[i] = 时间落在 [data[i][0] - interval, data[i][0]] 内的最大值
         */
        public int[] getRealTimeMaxValue(int[][] data, int interval) {
            int[] answer = new int[data.length];

            // 对每个位置 index，独立地往前扫描
            for (int index = 0; index < data.length; index++) {
                int best = Integer.MIN_VALUE;

                // 从 0 扫到 index，找出所有落在窗口内的数据点
                for (int previous = 0; previous <= index; previous++) {
                    // 时间窗口条件：data[previous][0] ∈ [data[index][0] - interval, data[index][0]]
                    if (data[previous][0] >= data[index][0] - interval
                            && data[previous][0] <= data[index][0]) {
                        best = Math.max(best, data[previous][1]);   // 取数值最大值
                    }
                }

                answer[index] = best;
            }
            return answer;
        }
    }

}
