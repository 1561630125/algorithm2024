package OD.基础与模拟;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 14:02
 */
public class 过号叫号 {

    static class InterviewApplicant {
        String id, name;          // 应聘者编号、姓名
        boolean priority;         // 是否优先面试（true=优先，false=普通）
        int order;               // 预约顺序（数值越小越靠前）
        int misses;              // 剩余过号次数（>0表示当前叫号会过号）
        int missedCalls;         // 累计过号次数（用于计算步长2^(missedCalls-1)）
        int inputOrder;          // 输入顺序（作为相同order时的次要排序依据）

        InterviewApplicant(
                String id, String name, boolean priority, int order, int misses, int inputOrder) {
            this.id = id;
            this.name = name;
            this.priority = priority;
            this.order = order;
            this.misses = misses;
            this.inputOrder = inputOrder;
        }
    }

    static String[] simulateInterviewCalls(String[] records) {
        // 1. 读取所有应聘者数据
        java.util.List<InterviewApplicant> applicants = new java.util.ArrayList<>();
        for (String record : records) {
            // 按空格分割记录（注意：正则\s+匹配一个或多个空白字符）
            String[] parts = record.trim().split("\\s+");
            if (parts.length == 0 || parts[0].equals("Exit"))
                break;  // 遇到Exit结束读取

            // 创建应聘者对象：编号, 姓名, 是否优先(true/false), 预约顺序, 过号次数, 输入顺序
            applicants.add(new InterviewApplicant(parts[0], parts[1], parts[2].equals("true"),
                    Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), applicants.size()));
        }

        // 2. 初始排序：优先者排在前面，同优先级按预约顺序升序，相同预约顺序按输入顺序升序
        applicants.sort((first, second) -> {
            // 比较优先级：优先(true) > 普通(false)，所以second在前返回正数
            int priority = Boolean.compare(second.priority, first.priority);
            if (priority != 0)
                return priority;
            // 比较预约顺序：升序
            int order = Integer.compare(first.order, second.order);
            return order != 0 ? order : Integer.compare(first.inputOrder, second.inputOrder);
        });

        // 3. 用LinkedList作为队列（支持在中间位置插入）
        java.util.LinkedList<InterviewApplicant> queue = new java.util.LinkedList<>(applicants);
        java.util.List<String> result = new java.util.ArrayList<>();

        // 4. 模拟叫号过程
        while (!queue.isEmpty()) {
            // 4.1 取出队首（当前被叫到的应聘者）
            InterviewApplicant current = queue.removeFirst();

            // 4.2 输出结果：如果misses>0说明本次是过号，标记为Y，否则N
            result.add(current.id + ":" + current.name + ":" + (current.misses > 0 ? "Y" : "N"));

            // 4.3 处理过号逻辑
            if (current.misses > 0) {
                // 本次过号：剩余过号次数减1
                current.misses--;
                // 累计过号次数加1（用于计算下次的步长）
                current.missedCalls++;

                // 计算步长：2^(missedCalls-1)
                // 如果missedCalls>=31，防止整数溢出，直接放到队尾
                int step = current.missedCalls >= 31 ? queue.size() : 1 << (current.missedCalls - 1);

                // 插入到队列中：从当前位置往后数step个位置
                // Math.min(step, queue.size()) 确保不超过队列长度
                queue.add(Math.min(step, queue.size()), current);

                // ⚠️ 注意：这里缺少一个关键逻辑
                // 题目要求：优先者过号一次后要取消优先资格
                // 但当前代码没有实现 priority = false
                // 这可能导致优先者过号后仍然保持优先状态
            }
            // 如果misses=0，直接输出，不放回队列（正常叫号结束）
        }

        return result.toArray(new String[0]);
    }


    static String[] simulateInterviewCalls2(String[] records) {
        for(int i = 0; i < records.length; i++) {
            String[] split = records[i].split("\\s");
            String nov = split[0];
            String name = split[1];
            String prio = split[2];
            Integer order = Integer.getInteger(split[3]);
            Integer left = Integer.getInteger(split[4]);

            if (records[i].equals("Exit")) {

            }

        }
        return new String[0];
    }


    public static void main(String[] args) {

        String[] records = new String[]{"Ae5 Echo true 1 1","B02 Beta false 2 0","Exit"};
        String[] strings = simulateInterviewCalls(records);
        System.out.println(Arrays.toString(strings));

    }

}
