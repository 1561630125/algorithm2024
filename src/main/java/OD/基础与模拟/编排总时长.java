package OD.基础与模拟;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 23:04
 */
public class 编排总时长 {

    static int[] possibleTaskCounts(int taskA, int taskB, int totalTasks) {
        int max = Math.max(taskA, taskB);
        int min = Math.min(taskA, taskB);

        int[] res = new int[]{};

        Set<Integer> list = new HashSet<>();
        for (int i = totalTasks; i >= 0; i--) {
            int use = min * i + max * (totalTasks - i);
            list.add(use);
        }

        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    class Solution {
        int[] possibleTaskCounts(int taskA, int taskB, int totalTasks) {
            // ---------- 1. 边界检查 ----------
            // 总任务数不能为负
            if (totalTasks < 0)
                return new int[0];

            // ---------- 2. TreeSet 去重 + 自动升序 ----------
            // TreeSet 保证元素唯一，且按自然顺序（升序）排列
            java.util.TreeSet<Integer> values = new java.util.TreeSet<>();

            // ---------- 3. 枚举 A 类任务的数量 ----------
            // countA 从 0 到 totalTasks
            // countB = totalTasks - countA
            // 总分 = countA * taskA + (totalTasks - countA) * taskB
            for (int countA = 0; countA <= totalTasks; countA++) {
                values.add(countA * taskA + (totalTasks - countA) * taskB);
            }

            // ---------- 4. TreeSet 转 int[] ----------
            int[] result = new int[values.size()];
            int index = 0;
            for (int value : values)
                result[index++] = value;

            return result;
        }
    }


    public static void main(String[] args) {
        int taskA = 2;
        int taskB = 2;
        int totalTasks = 3;
        System.out.println(Arrays.toString(possibleTaskCounts(taskA, taskB, totalTasks)));

    }

}
