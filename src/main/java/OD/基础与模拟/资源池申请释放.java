package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 20:18
 */
public class 资源池申请释放 {

    /**
     * 从资源池中为操作序列分配或回收资源
     *
     * 核心逻辑：
     * - 资源池有多个节点，每个节点有固定总容量
     * - 分配时采用"最适分配"策略（Best Fit）：选择剩余容量刚好满足需求且浪费最少的节点
     * - 回收时根据分配记录归还资源
     *
     * @param capacities  各资源节点的总容量数组，索引0对应节点1
     * @param operations  操作序列，每个操作是长度为2的long数组
     *                     - operation[0] == 1: 分配操作，operation[1]为请求的资源量
     *                     - operation[0] != 1: 回收操作，operation[1]为之前分配操作的序号（从1开始）
     * @return            每次操作的结果数组：分配成功返回节点编号（从1开始），失败返回0
     */
    int[] allocatePooledResources(long[] capacities, long[][] operations) {
        // free数组：记录每个资源节点当前剩余的可用容量（初始值 = 总容量）
        long[] free = java.util.Arrays.copyOf(capacities, capacities.length);

        // allocations列表：记录每次分配操作的结果
        // 每个元素为 long[]{节点编号, 分配的资源量}，索引位置对应操作序号（从0开始）
        // 节点编号0表示分配失败，此时分配量为0
        java.util.ArrayList<long[]> allocations = new java.util.ArrayList<>();

        // result列表：记录每次操作（分配或回收）的返回结果
        // 分配成功返回节点编号（1-based），失败返回0；回收操作不产生有效结果，但也会追加0占位
        java.util.ArrayList<Integer> result = new java.util.ArrayList<>();

        // ========== 遍历处理每一个操作 ==========
        for (long[] operation : operations) {
            // 防御性检查：跳过无效操作
            if (operation == null || operation.length < 2) continue;

            long type = operation[0];   // 操作类型：1=分配，其他=回收
            long value = operation[1];  // 分配时的请求量，或回收时的分配操作序号

            // ---------- 情况1：分配操作 (type == 1) ----------
            if (type == 1L) {
                int best = -1;           // 最佳匹配节点的索引（-1表示未找到）
                long bestRemaining = 0L; // 最佳节点的剩余容量

                // 遍历所有节点，采用"最适分配"（Best Fit）策略：
                // 选择剩余容量 >= 请求量，且剩余量最小的节点（碎片最小化）
                for (int index = 0; index < free.length; index++) {
                    if (free[index] < value) continue; // 容量不足，跳过
                    long remaining = free[index] - value;
                    // 首次找到可用节点，或找到剩余容量更小的节点
                    if (best < 0 || remaining < bestRemaining) {
                        best = index;
                        bestRemaining = remaining;
                    }
                }

                // 未找到合适节点：分配失败
                if (best < 0) {
                    allocations.add(new long[]{0L, 0L}); // 记录失败记录（节点0，分配量0）
                    result.add(0);                       // 返回0表示失败
                }
                // 找到合适节点：执行分配
                else {
                    free[best] -= value;                 // 扣减该节点剩余容量
                    allocations.add(new long[]{best + 1L, value}); // 记录分配详情（节点编号从1开始）
                    result.add(best + 1);                // 返回分配的节点编号
                }
            }

            // ---------- 情况2：回收操作 (type != 1) ----------
            // 回收的逻辑：value表示要回收的"第几次分配操作"（从1开始计数）
            else if (value >= 1L && value <= allocations.size()) {
                // 取出对应分配操作的记录
                long[] allocation = allocations.get((int)value - 1);
                // 仅当该分配记录有效（节点编号 > 0）时才执行回收
                if (allocation[0] > 0L) {
                    // 将资源归还到对应的节点
                    free[(int)allocation[0] - 1] += allocation[1];
                }
                // 注意：回收操作不向result追加有效值，但根据代码逻辑，这里也没有追加0
                // 这意味着result的长度实际上只记录了分配操作的结果，回收操作不产生输出
                // 这可能是设计意图，也可能是bug（注释中会说明）
            }
            // 注意：如果type != 1且value无效（<1或>allocations.size()），操作被静默忽略
        }

        // ========== 将ArrayList结果转换为int数组返回 ==========
        int[] output = new int[result.size()];
        for (int index = 0; index < output.length; index++) {
            output[index] = result.get(index);
        }
        return output;
    }

}
