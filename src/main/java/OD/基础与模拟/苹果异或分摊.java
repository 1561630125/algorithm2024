package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 20:13
 */
public class 苹果异或分摊 {

    /**
     * 计算“最大苹果份额”问题的解。
     * 问题背景：将一堆苹果（每个有不同重量）分成两堆，使得两堆的异或值相等。
     * 如果可能，返回其中一堆能获得的最大总重量；否则返回 -1。
     *
     * 数学原理：
     * - 如果两堆的异或值相等，则所有苹果的异或值必须为 0（因为 A ^ A = 0）
     * - 当总异或值为 0 时，为了使其中一堆的重量最大，应该将最轻的苹果单独作为一堆，
     *   其余所有苹果作为另一堆。这样较重的那堆重量 = 总重量 - 最轻苹果重量。
     *
     * @param weights 整数数组，表示每个苹果的重量
     * @return 如果可行，返回较大一堆的最大总重量；否则返回 -1
     */
    int maxAppleShare(int[] weights) {
        int xorSum = 0;    // 所有苹果重量的异或值
        int total = 0;     // 所有苹果的总重量
        int minimum = weights[0];  // 最轻苹果的重量，初始为第一个元素

        // 遍历所有苹果，计算异或和、总重量、最小重量
        for (int weight : weights) {
            xorSum ^= weight;      // 累加异或
            total += weight;       // 累加总重量
            minimum = Math.min(minimum, weight);  // 更新最小值
        }

        // 如果总异或值不为 0，则无法分成两堆异或值相等，返回 -1
        // 如果总异或值为 0，则最大的一堆重量 = 总重量 - 最轻苹果的重量
        return xorSum == 0 ? total - minimum : -1;
    }

}
