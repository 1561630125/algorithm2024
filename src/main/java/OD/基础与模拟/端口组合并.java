package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 14:29
 */
public class 端口组合并 {

    /**
     * 判断两个已排序的端口数组是否存在至少2个相同的端口
     * 前提：两个数组都已经升序排序
     * @param first  第一个端口组（已排序）
     * @param second 第二个端口组（已排序）
     * @return true 表示有2个及以上相同端口，需要合并
     */
    private boolean portSharesTwo(long[] first, long[] second) {
        int left = 0, right = 0, matched = 0;
        // 双指针遍历两个有序数组，类似归并排序中的比较
        while (left < first.length && right < second.length) {
            if (first[left] < second[right])
                left++;           // 第一个数组的当前值更小，移动左指针
            else if (first[left] > second[right])
                right++;          // 第二个数组的当前值更小，移动右指针
            else {
                // 相等，说明找到了一个共同端口
                left++;
                right++;
                if (++matched == 2)   // 找到第2个相同的端口，立即返回true
                    return true;
            }
        }
        return false;  // 遍历完都没有找到2个相同端口
    }

    /**
     * 合并所有满足条件的端口组
     * @param groups 输入的二维数组，每个子数组是一个端口组
     * @return 合并后的端口组二维数组
     */
    long[][] mergeAssociatedPortGroups(long[][] groups) {
        // ========== 第1步：输入校验 ==========
        // M（端口组个数）必须在1~10之间
        if (groups.length < 1 || groups.length > 10)
            return new long[][] {new long[0]};  // 返回 [[]]

        java.util.ArrayList<long[]> values = new java.util.ArrayList<>();

        // ========== 第2步：预处理每个端口组 ==========
        for (long[] group : groups) {
            // 每个端口组长度必须在1~100之间
            if (group == null || group.length < 1 || group.length > 100)
                return new long[][] {new long[0]};

            // 复制并排序（方便后续双指针比较）
            long[] copy = java.util.Arrays.copyOf(group, group.length);
            java.util.Arrays.sort(copy);
            values.add(copy);
        }

        // ========== 第3步：迭代合并 ==========
        boolean changed = true;
        while (changed) {
            changed = false;
            // 从后往前遍历，避免索引变化带来的问题
            for (int later = values.size() - 1; later >= 0; later--) {
                for (int earlier = later - 1; earlier >= 0; earlier--) {
                    // 检查这两个端口组是否有2个及以上相同端口
                    if (portSharesTwo(values.get(later), values.get(earlier))) {
                        // 合并两个数组：先把第一个复制出来，再追加第二个
                        long[] first = values.get(earlier);
                        long[] second = values.get(later);
                        long[] merged = java.util.Arrays.copyOf(first, first.length + second.length);
                        System.arraycopy(second, 0, merged, first.length, second.length);

                        // 合并后排序
                        java.util.Arrays.sort(merged);

                        // 用合并后的结果替换 earlier 位置，删除 later 位置
                        values.set(earlier, merged);
                        values.remove(later);

                        changed = true;
                        break;  // 跳出内层循环，重新从头开始扫描
                    }
                }
                if (changed) break;  // 跳出外层循环，重新开始 while
            }
        }

        // ========== 第4步：去重并构造结果 ==========
        long[][] result = new long[values.size()][];
        for (int row = 0; row < values.size(); row++) {
            long[] group = values.get(row);
            long[] unique = new long[group.length];
            int count = 0;
            // 利用数组已排序的特性，跳过重复元素
            for (long value : group) {
                if (count == 0 || unique[count - 1] != value) {
                    unique[count++] = value;
                }
            }
            // 截取有效部分
            result[row] = java.util.Arrays.copyOf(unique, count);
        }
        return result;
    }

    public static void main(String[] args) {

    }

}
