package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-08 15:12
 */
public class 低失率时段 {


    int[][] findLongestQualifiedRanges(long minAverageLost, long[] failureRates) {
        // 构建前缀和数组，prefix[i] 表示 failureRates[0] 到 failureRates[i-1] 的和
        // prefix[0] = 0, prefix[1] = failureRates[0], prefix[2] = failureRates[0] + failureRates[1], ...
        long[] prefix = new long[failureRates.length + 1];
        for (int index = 0; index < failureRates.length; index++) {
            prefix[index + 1] = prefix[index] + failureRates[index];
        }

        int bestLength = 0;  // 记录当前找到的最长合格区间长度
        java.util.ArrayList<int[]> result = new java.util.ArrayList<>();  // 存储所有最长合格区间的起始和结束索引

        // 枚举所有可能的子区间 [start, end]
        for (int start = 0; start < failureRates.length; start++) {
            for (int end = start; end < failureRates.length; end++) {
                int length = end - start + 1;  // 当前区间的长度

                // 计算当前区间的总和：prefix[end + 1] - prefix[start]
                // 判断是否合格：区间总和 <= minAverageLost * 区间长度
                // 即区间的平均值 <= minAverageLost
                if (prefix[end + 1] - prefix[start] <= minAverageLost * length) {
                    // 如果当前区间长度大于之前记录的最长长度
                    if (length > bestLength) {
                        bestLength = length;  // 更新最长长度
                        result.clear();       // 清空之前的结果，因为找到了更长的区间
                    }
                    // 如果当前区间长度等于最长长度，则添加到结果列表中
                    if (length == bestLength) {
                        result.add(new int[] {start, end});
                    }
                }
            }
        }

        // 将 ArrayList 转换为二维数组返回
        // 每个元素是一个 int[]，包含两个值：[起始索引, 结束索引]
        return result.toArray(new int[result.size()][]);
    }



}
