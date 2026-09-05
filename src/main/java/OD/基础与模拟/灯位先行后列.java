package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 14:57
 */
public class 灯位先行后列 {

    /**
     * 排序面板灯（先行后列）
     * @param lights 二维数组，每行表示一个灯：[id, x1, y1, x2, y2]
     * @return 排序后的灯id数组
     */
    long[] sortPanelLights(long[][] lights) {
        // 边界情况：空数组或null检查
        if (lights.length == 0)
            return new long[0];

        // 校验每个灯的数据完整性
        for (long[] light : lights)
            if (light == null || light.length < 5)
                return new long[0];

        // 【问题1】这里计算高度时，假设最后一个灯就是右下角的灯
        // 但实际上应该使用基准灯的半径，而不是固定使用最后一个灯的高度
        // 因为灯的大小可能不同，而且最后一个灯不一定是基准灯
        double height = (lights[lights.length - 1][4] - lights[lights.length - 1][2]) * 0.5;

        // 创建待处理列表，优化数据结构存储关键信息
        java.util.ArrayList<long[]> pending = new java.util.ArrayList<>();
        for (int index = 0; index < lights.length; index++) {
            // 存储格式：[y1, 原始索引, id, x1]
            // 这样方便按y1排序，同时保留id和x1信息
            pending.add(new long[]{
                    lights[index][2],  // 索引0: y1（纵坐标）
                    index,              // 索引1: 原始索引（用于追溯）
                    lights[index][0],   // 索引2: id
                    lights[index][1]    // 索引3: x1（横坐标）
            });
        }

        // 按 y1 从小到大排序（从上到下）
        pending.sort(java.util.Comparator.comparingLong(item -> item[0]));

        java.util.ArrayList<Long> result = new java.util.ArrayList<>();

        // 循环处理每一行
        while (!pending.isEmpty()) {
            // 【问题2】这里取出第一个元素作为基准灯
            // 但是：当前实现是假设pending按y1排序后，第一个就是基准
            // 而题目要求：应该从所有未处理的灯中找y1最小的，这个逻辑本身没问题
            // 但问题在于，取出的基准灯不应该从pending中移除（这里先移除了）
            long[] first = pending.remove(0);

            java.util.ArrayList<long[]> row = new java.util.ArrayList<>();
            row.add(first);

            // 【问题3】同行判定逻辑错误
            // 问题：这里使用固定高度值进行比较，但应该使用基准灯的半径
            // 且比较的间隔应该是 Math.abs(y1 - first[0]) <= 半径
            // 但这里的写法假设 pending 已经按 y1 排序，且只比较相邻元素
            // 这会导致如果 y1 跳跃超过高度，后面的元素就不再检查了
            // 但实际上，可能存在 y1 差距较小但被漏掉的情况吗？
            // 因为已经按 y1 排序，如果当前元素与基准差距 > 高度，
            // 后续元素差距只会更大，所以break是合理的
            // 但问题出在：基准灯不应该被移除，且高度应该是基准灯的高度/2
            while (!pending.isEmpty() && pending.get(0)[0] - first[0] <= height)
                row.add(pending.remove(0));

            // 同一行内按 x1 从小到大排序（从左到右）
            row.sort(java.util.Comparator.comparingLong(item -> item[3]));

            // 收集该行所有灯的id
            for (long[] item : row)
                result.add(item[2]);
        }

        // 将ArrayList转换为long数组返回
        long[] output = new long[result.size()];
        for (int i = 0; i < output.length; i++)
            output[i] = result.get(i);
        return output;
    }

}
