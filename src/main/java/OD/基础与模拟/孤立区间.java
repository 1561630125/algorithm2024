package OD.基础与模拟;

import java.util.Arrays;
import java.util.Comparator;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-07 14:47
 */
public class 孤立区间 {

    public int getIsolationInterval(int[][] intervals) {
        // 排序
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        // 遍历
        int count = 0;
        for(int i = 0; i < intervals.length; i++) {
            int start1 = intervals[i][0];
            int end1 = intervals[i][1];
            int j;
            for(j = i+1; j < intervals.length; j++) {
                int start2 = intervals[j][0];
                int end2 = intervals[j][1];
                if (end1 >= start2 && start1 <= end2) break;
            }
            if (j == intervals.length) count++;
        }

        return count;
    }

    /**
     * 计算给定区间集合中"孤立区间"的数量
     *
     * 孤立区间定义：该区间与所有其他区间都没有重叠（包括端点不接触）
     * 即：该区间的右端点 < 所有其他区间的左端点，或该区间的左端点 > 所有其他区间的右端点
     *
     * @param intervals 二维数组，每行 [left, right] 表示一个闭区间
     * @return 孤立区间的数量
     */
    public int getIsolationInterval2(int[][] intervals) {
        // 1. 边界情况：没有区间
        if (intervals.length == 0)
            return 0;

        // 2. 复制并排序：按左端点升序，左端点相同则按右端点升序
        int[][] values = intervals.clone();
        java.util.Arrays.sort(
                values,
                java.util.Comparator.<int[]>comparingInt(row -> row[0])
                        .thenComparingInt(row -> row[1])
        );

        // 3. 构建前缀数组：prefix[i] = values[0..i] 中右端点的最大值
        int[] prefix = new int[values.length];
        prefix[0] = values[0][1];
        for (int index = 1; index < values.length; index++) {
            prefix[index] = Math.max(prefix[index - 1], values[index][1]);
        }

        // 4. 构建后缀数组：suffix[i] = values[i..n-1] 中左端点的最小值
        int[] suffix = new int[values.length];
        suffix[values.length - 1] = values[values.length - 1][0];
        for (int index = values.length - 2; index >= 0; index--) {
            suffix[index] = Math.min(suffix[index + 1], values[index][0]);
        }

        // 5. 遍历每个区间，判断是否为孤立区间
        int answer = 0;
        for (int index = 0; index < values.length; index++) {
            // 条件1：左边没有区间与当前区间重叠
            // 即：当前区间左边的所有区间中，最大的右端点 < 当前区间的左端点
            boolean leftIsolated = (index == 0) || (prefix[index - 1] < values[index][0]);

            // 条件2：右边没有区间与当前区间重叠
            // 即：当前区间右边的所有区间中，最小的左端点 > 当前区间的右端点
            boolean rightIsolated = (index + 1 == values.length) || (suffix[index + 1] > values[index][1]);

            // 只有左右两边都满足条件，当前区间才是孤立的
            if (leftIsolated && rightIsolated) {
                answer++;
            }
        }

        return answer;
    }


    public static void main(String[] args) {

    }

}
