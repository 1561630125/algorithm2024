package OD.贪心和动态规划;

/**
 * 考点：贪心 + 优先队列
 *
 * @author faming.yang@hand-china.com 2026-09-03 17:52
 */
public class 限时赚积分 {
    class Solution {
        /**
         * 在给定可用时间 availableTime 下，选择任务使总得分最大。
         * <p>
         * tasks[i] = {deadline, points}：
         * - task[0]：该任务要求的时间点/截止时间（容量约束）
         * - task[1]：完成该任务获得的分数
         * <p>
         * 核心思路（贪心 + 优先队列）：
         * 1. 按 deadline 升序排列任务，逐个处理；
         * 2. 用一个最小堆维护"当前已选任务的分数"；
         * 3. 每加入一个任务后，如果已选任务数超过了当前允许的容量 capacity，
         * 就弹出堆中分数最小的任务，保证在每一步都选到"当前最优"；
         * 4. 最终 total 即为最大总得分。
         * <p>
         * 这是一个典型的"带截止时间的任务调度 / 最大收益"贪心问题。
         */
        long maxTaskPoints(int availableTime, long[][] tasks) {
            // 复制一份，避免修改传入的 tasks 数组
            long[][] ordered = tasks.clone();

            // 按任务的 deadline（task[0]）升序排序
            java.util.Arrays.sort(
                    ordered,
                    java.util.Comparator.comparingLong(task -> task[0])
            );

            // 最小堆：维护当前已选任务的分数，堆顶是分数最小的任务
            java.util.PriorityQueue<Long> selected = new java.util.PriorityQueue<>();

            long total = 0;  // 当前已选任务的总得分

            for (long[] task : ordered) {
                // 当前任务可用的容量：
                //   - 受该任务 deadline(task[0]) 限制
                //   - 同时不能超过全局可用时间 availableTime
                //   - 负数（如 availableTime < 0）时用 0 兜底
                long capacity = Math.min(task[0], Math.max(availableTime, 0));

                // 容量无效或分数非正，直接跳过该任务
                if (capacity <= 0 || task[1] <= 0)
                    continue;

                // 先把当前任务加入已选集合
                selected.add(task[1]);
                total += task[1];

                // 如果已选任务数超过了当前容量，就淘汰分数最小的任务
                // （贪心：保留分数大的，丢弃分数小的）
                if (selected.size() > capacity)
                    total -= selected.remove();   // remove() 弹出堆顶（最小值）
            }

            return total;   // 返回最大总得分
        }
    }
}
