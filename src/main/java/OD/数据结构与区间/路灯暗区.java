package OD.数据结构与区间;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 考点：单调栈合并区间
 *
 * @author faming.yang@hand-china.com 2026-09-09 14:54
 */
public class 路灯暗区 {


    long unlitRoadLength(long[] radii) {
        long res = 0;
        for (int i = 0; i < radii.length - 1; i++) {
            long next = radii[i] + radii[i + 1];
            res = res + Math.max(0, 100 - next);
        }

        return res;
    }


    /**
     * 计算道路未被照亮的长度（版本2：使用单调栈合并区间）
     * <p>
     * 题目背景：
     * 在一条直线上有 n 个路灯，每个路灯的位置是 index * 100
     * 每个路灯有半径 radii[index]，可以照亮 [位置 - 半径, 位置 + 半径] 的区间
     * 需要计算道路上有多少长度是未被任何路灯照亮的
     * <p>
     * 例如：
     * radii = [2, 3, 1]
     * 路灯0: 位置0，半径2 → 照亮 [-2, 2]
     * 路灯1: 位置100，半径3 → 照亮 [97, 103]
     * 路灯2: 位置200，半径1 → 照亮 [199, 201]
     * <p>
     * 合并照亮区间：[-2,2] + [97,103] + [199,201]
     * 未照亮部分：[2,97] 长度95 + [103,199] 长度96 = 191
     *
     * @param radii 每个路灯的半径数组
     * @return 未被照亮的道路总长度
     */
    long unlitRoadLength2(long[] radii) {
        // ========== 1. 参数校验 ==========
        // 如果路灯数量 <= 1，没有未照亮的区间
        if (radii.length <= 1) return 0L;

        // ========== 2. 数据结构初始化 ==========
        // leftStack: 存储合并后每个区间的左边界
        // rightStack: 存储合并后每个区间的右边界
        // size: 栈中有效元素数量
        long[] leftStack = new long[radii.length];
        long[] rightStack = new long[radii.length];
        int size = 0;

        // ========== 3. 遍历每个路灯，合并重叠区间 ==========
        for (int index = 0; index < radii.length; index++) {
            // 当前路灯的照亮范围
            // 位置 = index * 100（路灯间距100米）
            long left = index * 100L - radii[index];   // 左边界
            long right = index * 100L + radii[index];  // 右边界

            // ====== 关键：合并重叠区间 ======
            // 如果当前区间的左边界 <= 上一个区间的右边界，说明重叠
            // 需要合并：取左边界的最小值，右边界的最大值
            while (size > 0 && rightStack[size - 1] >= left) {
                size--;  // 弹出上一个区间

                // 合并区间：取并集
                left = Math.min(left, leftStack[size]);   // 左边界取更小的
                right = Math.max(right, rightStack[size]); // 右边界取更大的
            }

            // 将合并后的区间压入栈
            leftStack[size] = left;
            rightStack[size] = right;
            size++;
        }

        // ========== 4. 计算未照亮的长度 ==========
        // 现在 leftStack[0..size-1] 和 rightStack[0..size-1] 存储了
        // 所有不重叠的照亮区间
        //
        // 未照亮部分 = 每个区间的左边界 - 上一个区间的右边界
        // 即：leftStack[i] - rightStack[i-1]
        long result = 0L;
        for (int index = 1; index < size; index++) {
            result += leftStack[index] - rightStack[index - 1];
        }
        return result;
    }

    class Solution2 {

        /**
         * 计算"未照亮的道路总长度"。
         * <p>
         * 每个灯 i 照亮区间 [i*100 - radii[i], i*100 + radii[i]]。
         * 区间可能重叠，需要合并成互不重叠的区间；
         * 再累加相邻区间之间的空隙，即为未照亮长度。
         * <p>
         * 用单调栈（ArrayDeque 当栈）合并重叠区间：
         * 新来一个区间，若与栈顶重叠，就弹出栈顶并合并，直到不重叠。
         *
         * @param radii 每个灯的照射半径
         * @return 未照亮路段的总长度
         */
        long unlitRoadLength(long[] radii) {

            // 0 个或 1 个灯 → 没有"相邻区间空隙"，返回 0
            if (radii.length <= 1)
                return 0L;

            // 用 Deque 当栈，元素是 {left, right}
            Deque<long[]> stack = new ArrayDeque<>();

            for (int index = 0; index < radii.length; index++) {

                // 当前灯照亮的区间
                long left = index * 100L - radii[index];
                long right = index * 100L + radii[index];

                // ---------- 合并所有与当前区间重叠的栈顶 ----------
                while (!stack.isEmpty() && stack.peek()[1] >= left) {
                    long[] top = stack.pop();          // 弹出栈顶
                    left = Math.min(left, top[0]);     // 合并左端
                    right = Math.max(right, top[1]);   // 合并右端
                }

                // 合并后的区间入栈
                stack.push(new long[]{left, right});
            }

            // ---------- 累加相邻区间之间的空隙 ----------
            long result = 0L;
            long[] previous = null;
            // 栈是"后进先出"，遍历时需要按"入栈顺序"（从栈底到栈顶）
            // ArrayDeque 的 descendingIterator() 可从栈底到栈顶遍历
            for (java.util.Iterator<long[]> it = stack.descendingIterator(); it.hasNext(); ) {
                long[] current = it.next();
                if (previous != null)
                    result += current[0] - previous[1];   // 当前左端 - 上一个右端
                previous = current;
            }

            return result;
        }
    }

}
