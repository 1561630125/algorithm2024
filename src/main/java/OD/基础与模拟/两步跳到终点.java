package OD.基础与模拟;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 20:56
 */
public class 两步跳到终点 {

    public  static long[] minimumIndexPairSteps(long[] steps, long target) {
        long[] res = new long[]{0,0};
        long sumIndex = 0;
        for (int i = 0; i < steps.length; i++) {
            long remain = target - steps[i];
            for (int j = i + 1; j < steps.length; j++) {
                if (remain != steps[j]) {
                    continue;
                }
                if (sumIndex < i + j) {
                    sumIndex = i + j;
                    res = new long[]{steps[i], steps[j]};
                }
            }
        }
        return res;
    }


    /**
     * 在数组中找到一对数，使它们的和等于目标值，且索引和最小
     *
     * 核心逻辑：
     * 1. 使用哈希表记录每个值最早出现的索引
     * 2. 遍历数组时，检查是否存在互补值（target - 当前值）
     * 3. 若存在，计算索引和，保留最小的索引和
     * 4. 返回这对值（按原始数组中的顺序）
     *
     * @param steps  输入的数组
     * @param target 目标和
     * @return       长度为2的数组，包含配对的两个值
     *               如果没有找到，返回 {0L, 0L}
     */
    long[] minimumIndexPairSteps2(long[] steps, long target) {
        // ========== 数据结构初始化 ==========
        // earliest: 记录每个值在数组中最早出现的索引
        // 键：数组元素值，值：该值首次出现的索引
        java.util.Map<Long, Integer> earliest = new java.util.HashMap<>();

        // minimum: 记录当前找到的最小索引和
        // 初始值为 steps.length * 2，比任何可能的索引和都大
        int minimum = steps.length * 2;

        // result: 存储最终结果的两个值
        long[] result = {0L, 0L};

        // ========== 遍历数组 ==========
        for (int index = 0; index < steps.length; index++) {
            long value = steps[index];
            long other = target - value;  // 需要找的互补值

            // 检查互补值是否已经在数组中出现过
            Integer otherIndex = earliest.get(other);

            if (otherIndex != null && otherIndex + index < minimum) {
                // 找到了更优的配对：索引和更小
                minimum = otherIndex + index;
                result[0] = other;   // 先出现的值
                result[1] = value;   // 后出现的值
            }

            // 记录当前值的最早出现位置
            // putIfAbsent: 如果键不存在则插入，存在则保持原有值
            // 这确保了earliest中保存的是每个值首次出现的索引
            earliest.putIfAbsent(value, index);
        }

        return result;
    }


    public static void main(String[] args) {
        long[] steps = new long[]{1,7,2,2,5};
        long[] longs = minimumIndexPairSteps(steps, 4);
        System.out.println(Arrays.toString(Arrays.stream(longs).toArray()));

    }

}
