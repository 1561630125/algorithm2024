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
        for(int i = totalTasks; i >= 0; i--) {
            int use = min * i + max * (totalTasks - i);
            list.add(use);
        }

        return list.stream().mapToInt(Integer::intValue).toArray();
    }


    public static void main(String[] args) {
        int taskA = 2;
        int taskB = 2;
        int totalTasks = 3;
        System.out.println(Arrays.toString(possibleTaskCounts(taskA, taskB, totalTasks)));

    }

}
