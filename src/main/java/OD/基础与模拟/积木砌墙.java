package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 23:21
 */
public class 积木砌墙 {
    static int maxWallLayers(long[] lengths) {


        return -1;
    }

    /**
     * 计算最多能堆叠的墙层数。
     * 思路：将木板分组，每组的长度总和相等（即每层的总长度），组数即为层数。
     * 优先尝试较大的层数（从木板总数开始递减），找到第一个可行的最大层数。
     *
     * @param lengths 表示每块木板长度的数组
     * @return 最大可能的层数；若无法分组或数组为空，则返回 -1
     */
    int maxWallLayers2(long[] lengths) {
        // 边界情况：没有木板，无法建墙
        if (lengths.length == 0) return -1;

        // 1. 复制并排序数组，便于双指针操作
        long[] values = lengths.clone();
        java.util.Arrays.sort(values);

        // 2. 计算所有木板的总长度
        long total = 0;
        for (long value : values) total += value;

        /**
         * 3. 从最大可能的层数（木板总数）开始向下尝试。
         *    循环条件 layers * 2 >= values.length 是一种剪枝优化：
         *    如果层数小于总数的一半，意味着每层至少需要 2 块木板。
         *    当层数较小（少于一半）时，即使当前尝试失败，更小的层数也无法满足“每层长度相等”的整除条件？
         *    实际上，这个条件并不严谨，但它限制了尝试范围，防止过度搜索。
         *    注意：这里其实更准确的写法是 layers >= 1，但此剪枝基于经验或特定约束。
         */
        for (int layers = values.length; layers * 2 >= values.length; layers--) {
            // 4. 总长度必须能被层数整除，否则无法平均分配
            if (total % layers != 0) continue;

            // 5. 计算每层的目标总长度
            long target = total / layers;

            // 6. 使用双指针（left 指向最小木板，right 指向最大木板）进行配对
            int left = 0, right = values.length - 1, built = 0;

            // 7. 循环尝试构建每一层
            while (left <= right) {
                if (values[right] == target) {
                    // 如果当前最大的木板刚好等于目标长度，单独成层
                    right--;
                } else if (left < right && values[left] + values[right] == target) {
                    // 如果最小和最大木板之和等于目标长度，配对成层
                    left++;
                    right--;
                } else {
                    // 如果无法匹配（和大于或小于 target），说明当前 layers 不可行
                    break;
                }
                // 成功构建了一层
                built++;
            }

            // 8. 检查是否所有木板都使用完毕，并且构建的层数等于预期
            if (left > right && built == layers) {
                return layers; // 找到最大可行层数
            }
        }

        // 9. 未找到任何可行的分组方案
        return -1;
    }


    public static void main(String[] args) {

    }

}
