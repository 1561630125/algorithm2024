package OD.基础与模拟;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-04 14:31
 */
public class 最短作业调度 {

    /**
     * 计算在多条流水线（并行处理单元）上完成所有任务所需的最短完成时间。
     *
     * <p>该算法采用贪心策略（List Scheduling）：将任务按升序排序后，依次分配给当前负载最小的流水线，
     * 以尽可能均衡各流水线的总处理时间，从而最小化最大完成时间（makespan）。
     *
     * <p><b>注意：</b> 这是一个近似算法，不保证全局最优解（该问题本质上是NP-hard的分区问题）。
     * 升序排序并非最优选择，改用降序（LPT算法）通常能获得更好的近似效果。
     *
     * @param pipelineCount 可用的流水线（并行处理器）数量，必须大于0
     * @param tasks         每个任务所需的处理时间数组（长整型），不能为空
     * @return 所有流水线中最大的总负载，即完成所有任务所需的最短时间；
     *         若参数无效（pipelineCount ≤ 0 或 tasks 为空），返回 0
     */
    long pipelineCompletionTime(int pipelineCount, long[] tasks) {
        // 边界条件：无流水线或无任务时，完成时间为0
        if (pipelineCount <= 0 || tasks.length == 0) return 0;

        // 克隆并排序任务数组（升序排列）
        long[] sorted = tasks.clone();
        java.util.Arrays.sort(sorted);

        // 初始化流水线负载数组，长度为 min(流水线数, 任务数)
        // 多余的流水线将处于空闲状态
        long[] loads = new long[Math.min(pipelineCount, sorted.length)];

        // 遍历所有任务，进行分配
        for (int index = 0; index < sorted.length; index++) {
            if (index < loads.length) {
                // 前 N 个任务直接分配给每条流水线（每个流水线各分配一个任务）
                loads[index] = sorted[index];
            } else {
                // 后续任务：选择当前负载最小的流水线进行分配（贪心策略）
                int smallest = 0;
                for (int line = 1; line < loads.length; line++) {
                    if (loads[line] < loads[smallest]) {
                        smallest = line;
                    }
                }
                // 将当前任务累加到负载最小的流水线上
                loads[smallest] += sorted[index];
            }
        }

        // 计算所有流水线中的最大负载，即为完成所有任务所需的总时间
        long result = 0;
        for (long load : loads) {
            result = Math.max(result, load);
        }
        return result;
    }


    /**
     * 计算多流水线完成所有任务的最短时间
     * @param pipelineCount 流水线数量
     * @param tasks 每个任务的处理时间
     * @return 总完成时间
     */
    public static long pipelineCompletionTime2(int pipelineCount, long[] tasks) {
        if (pipelineCount <= 0 || tasks == null || tasks.length == 0) {
            return 0;
        }

        // 特殊情况：流水线数 >= 任务数
        if (pipelineCount >= tasks.length) {
            long max = 0;
            for (long t : tasks) max = Math.max(max, t);
            return max;
        }

        // 按升序排序（短任务优先，与原代码一致）
        long[] sorted = tasks.clone();
        Arrays.sort(sorted);

        // 最小堆维护每条流水线的负载
        PriorityQueue<Long> minHeap = new PriorityQueue<>(pipelineCount);

        // 初始化：先分配前pipelineCount个任务（最短的）
        for (int i = 0; i < pipelineCount; i++) {
            minHeap.offer(sorted[i]);
        }

        // 分配剩余任务（从第pipelineCount个开始）
        for (int i = pipelineCount; i < sorted.length; i++) {
            long smallest = minHeap.poll();     // 取出负载最小的流水线
            smallest += sorted[i];              // 分配当前任务
            minHeap.offer(smallest);
        }

        // 找到堆中最大值
        long maxLoad = 0;
        for (long load : minHeap) {
            maxLoad = Math.max(maxLoad, load);
        }
        return maxLoad;
    }


    public static void main(String[] args) {
        long[] tasks = new long[]{8,4,3,2,9};
        long pipelineCompletionTime2 = pipelineCompletionTime2(3, tasks);
        System.out.println(pipelineCompletionTime2);
    }
}
