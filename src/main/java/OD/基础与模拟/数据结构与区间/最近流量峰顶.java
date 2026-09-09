package OD.基础与模拟.数据结构与区间;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 11:21
 */
public class 最近流量峰顶 {


    /**
     * 寻找数组中“峰点”的最小跨度（span）
     * “峰点”定义：数组中某个元素，其左侧存在比它小的元素，右侧也存在比它小的元素
     * 跨度 = 右侧比它小的元素的位置 - 左侧比它小的元素的位置
     *
     * 本质上是寻找所有满足“左侧有更小值，右侧也有更小值”的元素，
     * 并计算其左右两侧最近的小值之间的距离，取最小值
     */
    static int minPeakSpan(int[] samples) {
        int count = samples.length;
        // 数组长度小于3时，不可能存在峰点（需要左右各有一个元素）
        if (count < 3) return -1;

        // left[i]：i 左侧最近的小于 samples[i] 的元素下标
        // right[i]：i 右侧最近的小于 samples[i] 的元素下标
        int[] left = new int[count];
        int[] right = new int[count];
        java.util.Arrays.fill(left, -1);
        java.util.Arrays.fill(right, -1);

        // 使用单调递增栈（从栈底到栈顶递增），
        // 栈中存储数组下标，对应的值严格递增（>= 时弹出）
        int[] stack = new int[count];
        int top = -1;

        // ========== 第一次遍历：从左向右，计算 left[i] ==========
        for (int index = 0; index < count; index++) {
            // 弹出所有 >= samples[index] 的元素，
            // 因为我们要找的是“小于”当前元素的值，相等不算小于
            while (top >= 0 && samples[stack[top]] >= samples[index]) {
                top--;
            }
            // 如果栈不为空，栈顶就是左侧最近的小于 samples[index] 的元素
            if (top >= 0) {
                left[index] = stack[top];
            }
            // 当前索引入栈
            stack[++top] = index;
        }

        System.out.println(Arrays.toString(stack));

        // ========== 第二次遍历：从右向左，计算 right[i] ==========
        top = -1;
        for (int index = count - 1; index >= 0; index--) {
            // 同样，弹出所有 >= samples[index] 的元素
            while (top >= 0 && samples[stack[top]] >= samples[index]) {
                top--;
            }
            // 栈顶就是右侧最近的小于 samples[index] 的元素
            if (top >= 0) {
                right[index] = stack[top];
            }
            stack[++top] = index;
        }
        System.out.println(Arrays.toString(stack));
        // ========== 第三步：遍历所有元素，计算最小跨度 ==========
        int best = count + 1; // 初始值设为比最大可能跨度大1
        for (int index = 0; index < count; index++) {
            // 左右两侧都存在比它小的元素时，才构成“峰点”
            if (left[index] >= 0 && right[index] >= 0) {
                // 跨度 = 右侧小值下标 - 左侧小值下标
                best = Math.min(best, right[index] - left[index]);
            }
        }

        // 如果没有找到任何峰点，返回 -1；否则返回最小跨度
        return best == count + 1 ? -1 : best;
    }

    public static void main(String[] args) {
        System.out.println(minPeakSpan(new int[]{1,2,3,4,5}));
    }


}
