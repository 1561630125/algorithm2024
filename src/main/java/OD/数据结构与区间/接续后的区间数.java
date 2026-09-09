package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 17:52
 */
public class 接续后的区间数 {

    /**
     * 计算最少需要多少个连通区间段（用连接点将区间连通后）
     *
     * 问题描述：
     * 给定一组区间 [start, end] 和一组连接点（整数点），
     * 如果两个相邻区间之间的间隙 ≤ 可用连接点，则可以用一个连接点将它们连通。
     * 目标是：用尽可能多的连接点消除间隙，使连通区间段数最少。
     *
     * @param intervals 二维数组，每个元素是 [左端点, 右端点]（闭区间）
     * @param connectors 可用的连接点（整数数组）
     * @return 最少连通区间段数
     */
    int minimumConnectedIntervals(long[][] intervals, long[] connectors) {
        // 边界检查：没有区间 → 0个连通段
        if (intervals.length == 0)
            return 0;

        // 1. 复制并验证区间格式
        long[][] values = new long[intervals.length][2];
        for (int index = 0; index < intervals.length; index++) {
            if (intervals[index].length != 2)  // 每个区间必须正好两个端点
                return 0;
            values[index] = intervals[index].clone();  // 克隆避免修改原数组
        }

        // 2. 区间排序：按左端点升序，左端点相同按右端点升序
        java.util.Arrays.sort(values,
                (left, right)
                        -> left[0] != right[0] ? Long.compare(left[0], right[0])
                        : Long.compare(left[1], right[1]));

        // 3. 合并重叠区间（区间合并）
        java.util.ArrayList<long[]> merged = new java.util.ArrayList<>();
        merged.add(values[0]);
        for (int index = 1; index < values.length; index++) {
            long[] previous = merged.get(merged.size() - 1);
            // 当前区间左端点 ≤ 前一个区间右端点 → 重叠，合并
            if (values[index][0] <= previous[1])
                previous[1] = Math.max(previous[1], values[index][1]);
            else
                merged.add(values[index]);  // 不重叠，新增区间
        }

        // 4. 计算相邻合并区间之间的间隙
        // 间隙 = 下一个区间左端点 - 当前区间右端点（需要连接的长度）
        long[] gaps = new long[Math.max(0, merged.size() - 1)];
        for (int index = 1; index < merged.size(); index++)
            gaps[index - 1] = merged.get(index)[0] - merged.get(index - 1)[1];

        // 5. 贪心匹配：用最大的连接点填补最大的间隙
        long[] available = connectors.clone();  // 克隆避免修改原数组
        java.util.Arrays.sort(gaps);     // 间隙升序
        java.util.Arrays.sort(available); // 连接点升序

        int gap = gaps.length - 1;       // 从最大间隙开始
        int connector = available.length - 1;  // 从最大连接点开始
        int used = 0;  // 成功使用的连接点数量

        // 贪心策略：用尽可能大的连接点填补尽可能大的间隙
        while (gap >= 0 && connector >= 0) {
            // 如果最大连接点 ≥ 最大间隙，可以连通
            if (gaps[gap] <= available[connector]) {
                used++;          // 使用一个连接点
                connector--;     // 当前连接点已使用
            }
            gap--;  // 无论如何都处理当前间隙（要么被填，要么放弃）
        }

        // 6. 返回最少连通区间段数
        // 初始有 merged.size() 个区间段
        // 每使用一个连接点，就减少一个区间段
        return gaps.length + 1 - used;
    }

}
