package OD.基础与模拟;

import java.util.Arrays;

/**
 * https://www.nowcoder.com/discuss/683081820291821568
 *
 * @author faming.yang@hand-china.com 2026-09-04 21:54
 */
public class 处理器亲和组合 {

    /**
     * 递归生成从给定数组中选取指定数量元素的所有组合。
     * <p>
     * 这是一个标准的组合生成算法（无重复、不考虑顺序），
     * 使用深度优先搜索（DFS）和回溯法。
     *
     * @param values   源数组（元素可能重复，但每个位置只能使用一次）
     * @param requested 需要选取的元素个数（组合的大小）
     * @param start    当前搜索的起始索引（避免重复组合）
     * @param depth    当前已选取的元素个数（递归深度）
     * @param path     存储当前组合的临时数组
     * @param result   存放所有组合结果（每个组合是一个 int[]）
     */
    public static void processorGroups(int[] values, int requested, int start, int depth, int[] path,
                                 java.util.List<int[]> result) {
        // 已选够 requested 个元素，将当前组合（克隆）加入结果集
        if (depth == requested) {
            result.add(path.clone());
            return;
        }
        // 从 start 开始遍历，避免产生重复组合（如 [1,2] 和 [2,1]）
        for (int index = start; index < values.length; index++) {
            path[depth] = values[index];          // 选择当前元素
            processorGroups(values, requested, index + 1, depth + 1, path, result); // 递归选择下一个
        }
    }

    /**
     * 根据特定规则处理处理器组合，生成满足条件的处理器分配方案。
     * <p>
     * 处理流程：
     * 1. 对输入的处理器数组进行排序，并分为两组：
     *    - 第一组（links[0]）：所有值 < 4 的处理器（小值组）
     *    - 第二组（links[1]）：所有值 >= 4 的处理器（大值组）
     * 2. 根据 requested 参数的不同取值，采用不同的组合策略：
     *    - requested == 4：如果任一组恰好包含 4 个元素，则返回该组
     *    - requested == 8：必须两组都恰好包含 4 个元素，才返回整个排序后的数组
     *    - requested == 1 或 2：按优先级顺序（requested=1 时优先 [1,3,2,4]；
     *                            requested=2 时优先 [2,4,3]）查找符合长度的组，
     *                            然后对该组生成所有 requested 大小的组合
     * 3. 返回结果数组（二维 int 数组）
     *
     * @param processors 输入的处理器数组（整数数组）
     * @param requested  请求的处理器数量（仅支持 1、2、4、8）
     * @return 满足条件的组合数组，每个组合是一个 int[]，若无结果则返回空数组
     *
     * @example
     * // 示例 1：requested == 4
     * aiProcessorCombinations(new int[]{1, 2, 3, 4, 5, 6}, 4)
     * // 排序后：[1,2,3,4,5,6]，links[0]=[1,2,3]（长度3），links[1]=[4,5,6]（长度3）
     * // 无任一组长度为4 → 返回 []
     *
     * aiProcessorCombinations(new int[]{1, 2, 3, 4, 5}, 4)
     * // 排序后：[1,2,3,4,5]，links[0]=[1,2,3]（长度3），links[1]=[4,5]（长度2）
     * // 无任一组长度为4 → 返回 []
     *
     * aiProcessorCombinations(new int[]{1, 2, 3, 4}, 4)
     * // 排序后：[1,2,3,4]，links[0]=[1,2,3]（长度3），links[1]=[4]（长度1）
     * // 无任一组长度为4 → 返回 []
     * // 注意：即使总长度为4，但分散在两组中，不符合规则
     *
     * // 示例 2：requested == 8
     * aiProcessorCombinations(new int[]{1,2,3,4,5,6,7,8}, 8)
     * // 排序后：[1,2,3,4,5,6,7,8]，links[0]=[1,2,3]（长度3），links[1]=[4,5,6,7,8]（长度5）
     * // 不满足两组长度都为4 → 返回 []
     *
     * aiProcessorCombinations(new int[]{1,2,3,4,5,6,7,8}, 8) // 若值为 [1,2,3,5,4,6,7,8]
     * // 排序后：[1,2,3,4,5,6,7,8]，同上，不满足
     *
     * // 示例 3：requested == 1
     * aiProcessorCombinations(new int[]{1, 2, 3, 4, 5}, 1)
     * // 排序后：[1,2,3,4,5]，links[0]=[1,2,3]（长度3），links[1]=[4,5]（长度2）
     * // 优先级 [1,3,2,4]：先检查长度1→无，长度3→links[0]符合，对 [1,2,3] 生成所有 1 个元素的组合
     * // 结果：[[1], [2], [3]]
     *
     * // 示例 4：requested == 2
     * aiProcessorCombinations(new int[]{1, 2, 3, 4, 5, 6}, 2)
     * // 排序后：[1,2,3,4,5,6]，links[0]=[1,2,3]（长度3），links[1]=[4,5,6]（长度3）
     * // 优先级 [2,4,3]：先检查长度2→无，长度4→无，长度3→links[0]和links[1]都符合
     * // 对两个组分别生成所有 2 个元素的组合
     * // 结果：[[1,2], [1,3], [2,3], [4,5], [4,6], [5,6]]
     */
    public static int[][] aiProcessorCombinations2(int[] processors, int requested) {
        // 1. 排序并克隆，避免修改原数组
        int[] sorted = processors.clone();
        java.util.Arrays.sort(sorted);

        // 2. 统计小于 4 的元素个数，将数组分为两组
        int firstCount = 0;
        for (int value : sorted)
            if (value < 4)
                firstCount++;
        int[][] links = {
                java.util.Arrays.copyOfRange(sorted, 0, firstCount),      // 值 < 4 的组
                java.util.Arrays.copyOfRange(sorted, firstCount, sorted.length) // 值 >= 4 的组
        };

        // 3. 存储最终结果
        java.util.List<int[]> result = new java.util.ArrayList<>();

        // 4. 根据 requested 值执行不同的策略
        if (requested == 4) {
            // 策略：如果某组恰好有 4 个元素，则直接返回该组（克隆以避免外部修改）
            for (int[] link : links)
                if (link.length == 4)
                    result.add(link.clone());

        } else if (requested == 8) {
            // 策略：必须两组都恰好有 4 个元素，才返回整个排序数组
            if (links[0].length == 4 && links[1].length == 4)
                result.add(sorted);

        } else if (requested == 1 || requested == 2) {
            // 策略：按优先级顺序查找符合长度的组，然后生成组合
            // requested=1 时优先顺序：[1, 3, 2, 4]（先找长度为1，然后3，然后2，最后4）
            // requested=2 时优先顺序：[2, 4, 3]（先找长度为2，然后4，最后3）
            int[] preferences = requested == 1 ? new int[] {1, 3, 2, 4} : new int[] {2, 4, 3};

            // 遍历优先级列表
            for (int size : preferences) {
                // 检查两组中是否有长度等于 size 的
                if (links[0].length == size || links[1].length == size) {
                    // 对每个长度匹配的组，生成所有 requested 个元素的组合
                    for (int[] link : links)
                        if (link.length == size)
                            processorGroups(link, requested, 0, 0, new int[requested], result);
                    break; // 只匹配第一个符合的 size（优先级最高的）
                }
            }
        }
        // 注意：如果 requested 是其他值（如 3、5 等），则 result 保持为空

        // 5. 将 List 转换为二维数组返回
        return result.toArray(new int[0][]);
    }


    public static int[][] aiProcessorCombinations(int[] processors, int requested) {

        return new int[0][];
    }

    public static void main(String[] args) {
        int[] processors = new int[]{0,2,4,5,6,7};
        int requested = 1;
        int[][] ints = aiProcessorCombinations2(processors, requested);
        System.out.println(Arrays.deepToString(ints));
    }

}
