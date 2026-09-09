package OD.数据结构与区间;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-08 15:24
 */
public class 打印任务队列 {


    /**
     * 模拟多台打印机的任务调度系统
     * 每台打印机维护一个任务队列，按照优先级（数值越大优先级越高）和文件ID（越小越先打印）进行调度
     *
     * @param events 操作指令数组，格式示例：
     *               "IN 1 5"  表示向1号打印机添加优先级为5的文件
     *               "OUT 1"   表示从1号打印机取出一个任务执行
     * @return 所有OUT操作输出的文件ID列表，如果没有任务则输出"NULL"
     */
    String[] simulatePrinterQueue(String[] events) {
        // 1. 初始化5台打印机的任务队列（索引0-4对应打印机1-5）
        // 每个队列存储 long[] 数组: [优先级, 文件ID]
        // 使用 ArrayList 便于动态添加和删除
        java.util.List<long[]>[] queues = new java.util.ArrayList[5];
        for (int printer = 0; printer < 5; printer++) {
            queues[printer] = new java.util.ArrayList<>();
        }

        // 存储所有OUT操作的输出结果
        java.util.List<String> outputs = new java.util.ArrayList<>();

        // 文件ID自增计数器，从1开始
        int nextId = 1;

        // 2. 遍历所有事件指令
        for (String event : events) {
            // 去除首尾空格并按空格分割，处理空字符串情况
            String[] parts = event.trim().isEmpty() ? new String[0] : event.trim().split("\\s+");
            if (parts.length == 0) continue; // 跳过空指令

            // ----- 处理 IN 指令：添加新任务到指定打印机 -----
            if (parts[0].equals("IN") && parts.length >= 3) {
                int fileId = nextId++;  // 分配唯一的文件ID（自增）
                int printer = Integer.parseInt(parts[1]) - 1; // 转换：用户输入1→索引0
                long priority = Long.parseLong(parts[2]);     // 优先级（数值越大优先级越高）

                // 校验打印机编号是否合法（1-5）
                if (printer >= 0 && printer < 5) {
                    // 将任务加入对应的打印机队列
                    // 存储格式：[优先级, 文件ID]
                    queues[printer].add(new long[]{priority, fileId});
                }
            }

            // ----- 处理 OUT 指令：从指定打印机取出任务 -----
            else if (parts[0].equals("OUT") && parts.length >= 2) {
                int printer = Integer.parseInt(parts[1]) - 1;

                // 检查打印机编号是否合法，以及队列是否为空
                if (printer < 0 || printer >= 5 || queues[printer].isEmpty()) {
                    outputs.add("NULL");  // 无任务可执行
                    continue;
                }

                // 3. 核心调度逻辑：寻找优先级最高的任务
                // 默认选择队列中第一个任务（索引0）作为当前最优
                int best = 0;
                for (int index = 1; index < queues[printer].size(); index++) {
                    long[] current = queues[printer].get(index);  // 当前遍历的任务
                    long[] chosen = queues[printer].get(best);    // 当前选中的最优任务

                    // 比较规则（优先级高的先执行）：
                    // 1. 优先级更高（数值更大）→ 替换
                    // 2. 优先级相同，但文件ID更小（先添加的）→ 替换
                    // 注意：因为文件ID是自增的，ID越小表示越早加入队列
                    if (current[0] > chosen[0] ||
                            (current[0] == chosen[0] && current[1] < chosen[1])) {
                        best = index;  // 更新最优任务的位置
                    }
                }

                // 4. 从队列中移除最优任务，并输出其文件ID
                // remove(best) 返回被移除的元素，取索引1位置的文件ID
                outputs.add(Long.toString(queues[printer].remove(best)[1]));
            }
        }

        // 5. 将输出列表转换为字符串数组返回
        return outputs.toArray(new String[0]);
    }


    /**
     * 使用优先队列优化打印机调度系统
     * 时间复杂度：IN操作为 O(log n)，OUT操作为 O(log n)
     * 相比原来的 O(n) 查找，性能大幅提升
     */
    String[] simulatePrinterQueue2(String[] events) {
        // 1. 初始化5台打印机的优先队列
        // 自定义比较器：优先级高的先出队，优先级相同则文件ID小的先出队
        PriorityQueue<Task>[] queues = new PriorityQueue[5];
        for (int printer = 0; printer < 5; printer++) {
            queues[printer] = new PriorityQueue<>((t1, t2) -> {
                // 优先级高的优先（数值大）
                if (t1.priority != t2.priority) {
                    return Long.compare(t2.priority, t1.priority); // 降序
                }
                // 优先级相同，文件ID小的优先（先到先得）
                return Integer.compare(t1.fileId, t2.fileId);
            });
        }

        List<String> outputs = new ArrayList<>();
        int nextId = 1;

        for (String event : events) {
            String[] parts = event.trim().isEmpty() ? new String[0] : event.trim().split("\\s+");
            if (parts.length == 0) continue;

            if (parts[0].equals("IN") && parts.length >= 3) {
                int fileId = nextId++;
                int printer = Integer.parseInt(parts[1]) - 1;
                long priority = Long.parseLong(parts[2]);

                if (printer >= 0 && printer < 5) {
                    // 直接添加到优先队列，自动按优先级排序
                    queues[printer].offer(new Task(fileId, priority));
                }
            }
            else if (parts[0].equals("OUT") && parts.length >= 2) {
                int printer = Integer.parseInt(parts[1]) - 1;

                if (printer < 0 || printer >= 5 || queues[printer].isEmpty()) {
                    outputs.add("NULL");
                    continue;
                }

                // 直接取出优先级最高的任务（堆顶）
                Task task = queues[printer].poll();
                outputs.add(Integer.toString(task.fileId));
            }
        }

        return outputs.toArray(new String[0]);
    }

    /**
     * 任务实体类
     * 封装文件ID和优先级
     */
    static class Task {
        int fileId;
        long priority;

        Task(int fileId, long priority) {
            this.fileId = fileId;
            this.priority = priority;
        }
    }


}
