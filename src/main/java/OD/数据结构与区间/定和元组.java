package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 12:29
 */
public class 定和元组 {


    /**
     * 计算数组中所有 K 数组合（K-Sum）的和等于目标值的组合数量
     *
     * 题目背景：
     * 从数组中找出 K 个不同的元素（通过索引区分），使得它们的和等于 target
     * 注意：数组可能有重复元素，但组合不能重复计数（即相同的数值组合只算一次）
     *
     * 例如：numbers = [1, 1, 2, 3], k = 3, target = 5
     * 可能的组合：{1, 1, 3} 和 {1, 2, 2}（但数组中没有2个2）
     * 实际：{1(index=0), 1(index=1), 3} → 1种
     *       {1, 2, 3} → 2种（两个1都可以和2,3组合）
     *       总共 3 种
     *
     * @param numbers 输入数组
     * @param k 需要选择的元素个数
     * @param target 目标和
     * @return 满足条件的组合数量
     */
    long countKSumTuples(int[] numbers, int k, long target) {
        // ========== 参数校验 ==========
        if (numbers == null || k < 2 || k > numbers.length)
            return 0;

        // ========== 排序：便于剪枝和去重 ==========
        int[] values = numbers.clone();
        java.util.Arrays.sort(values);

        // ========== 递归搜索 ==========
        return search(values, 0, k, target);
    }

    /**
     * 递归搜索所有 K 数组合
     *
     * @param values 已排序的数组
     * @param start 当前搜索的起始位置（避免重复使用同一元素）
     * @param remaining 还需要选择几个数
     * @param target 剩余目标和
     * @return 满足条件的组合数量
     */
    private long search(int[] values, int start, int remaining, long target) {
        // ========== 基础情况：只剩下2个数需要选择 ==========
        // 使用双指针法在 O(n) 时间内找到所有和为 target 的数对
        if (remaining == 2) {
            int left = start, right = values.length - 1;
            long result = 0;

            while (left < right) {
                long total = (long) values[left] + values[right];

                if (total < target) {
                    left++;  // 和太小，左指针右移增大和
                } else if (total > target) {
                    right--; // 和太大，右指针左移减小和
                } else {
                    // ========== 找到一对满足条件的数 ==========
                    result++;

                    int leftValue = values[left];
                    int rightValue = values[right];

                    // ========== 关键：跳过重复元素，避免重复计数 ==========
                    // 跳过所有等于 leftValue 的元素
                    while (left < right && values[left] == leftValue)
                        left++;
                    // 跳过所有等于 rightValue 的元素
                    while (left < right && values[right] == rightValue)
                        right--;
                }
            }
            return result;
        }

        // ========== 递归情况：需要选择 >2 个数 ==========
        long result = 0;

        // ========== 枚举当前要选择的第一个数 ==========
        // 注意：values.length - remaining 确保剩余位置足够
        for (int index = start; index <= values.length - remaining; index++) {
            // ========== 剪枝1：跳过重复元素 ==========
            // 如果当前元素和前一个相同，跳过（避免重复组合）
            if (index > start && values[index] == values[index - 1])
                continue;

            // ========== 剪枝2：最小值剪枝 ==========
            // 计算当前选择下，剩余元素的最小可能和
            // 如果最小值 > target，说明后面所有组合都太大，可以直接退出
            long minimum = values[index];
            for (int offset = 1; offset < remaining; offset++)
                minimum += values[index + offset];
            if (minimum > target)
                break;  // 因为数组已排序，后面的值只会更大

            // ========== 剪枝3：最大值剪枝 ==========
            // 计算当前选择下，剩余元素的最大可能和
            // 如果最大值 < target，说明当前元素太小，不可能达到目标
            long maximum = values[index];
            for (int offset = 1; offset < remaining; offset++)
                maximum += values[values.length - offset];
            if (maximum < target)
                continue;  // 尝试下一个更大的元素

            // ========== 递归搜索剩余的数 ==========
            // 选择 values[index] 作为当前组合的第一个数
            // 然后在 index+1 之后选择 remaining-1 个数，目标和为 target-values[index]
            result += search(values, index + 1, remaining - 1, target - values[index]);
        }

        return result;
    }

}
