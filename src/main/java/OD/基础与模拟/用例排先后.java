package OD.基础与模拟;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-05 12:46
 */
public class 用例排先后 {

    static class Group {
        long sum;
        int index;

        public Group(long sum, int index) {
            this.sum = sum;
            this.index = index;
        }
    }

    static int[] planTestExecution(long[] priorities, int[][] testCases) {

        LinkedList<Group> groups = new LinkedList<>();
        for (int i = 0; i < testCases.length; i++) {
            int[] testCase = testCases[i];
            long sum = 0;
            for (int ca : testCase) {
                if (ca - 1 < 0) continue;
                long priority = priorities[ca - 1];
                sum += priority;
            }
            groups.add(new Group(sum, i + 1));
        }

        groups.sort((a ,b) -> a.sum != b.sum ? (int) (b.sum - a.sum) : (a.index - b.index));


        int[] res = new int[groups.size()];
        for(int i = 0; i < groups.size(); i++) {
            res[i] = groups.get(i).index;
        }
        return res;
    }


    /**
     * 根据测试用例覆盖的功能优先级总分，对测试用例进行排序（降序）。
     * 排序规则：总分高的排前面；总分相同时，测试用例ID（原始行号+1）小的排前面。
     *
     * @param priorities 各功能点的优先级权重数组，索引0对应功能1，以此类推。
     *                    若功能ID无效（超出范围），则忽略该功能（不加分）。
     * @param testCases  二维数组，每个子数组代表一个测试用例，包含其覆盖的功能ID列表。
     * @return 排序后的测试用例原始ID（从1开始）数组，按执行优先级从高到低排列。
     */
    int[] planTestExecution2(long[] priorities, int[][] testCases) {
        // 1. 初始化得分数组 [总分, 原始用例ID]
        long[][] scores = new long[testCases.length][2];

        // 2. 遍历每个测试用例，计算其总分
        for (int row = 0; row < testCases.length; row++) {
            long total = 0L;
            // 遍历当前用例覆盖的每个功能ID
            for (int featureId : testCases[row]) {
                // 只统计有效功能ID（在优先数组长度范围内）
                if (featureId >= 1 && featureId <= priorities.length) {
                    total += priorities[featureId - 1]; // 累加该功能的权重
                }
                // 无效ID直接忽略（不报错，不加分）
            }
            scores[row][0] = total;     // 存储总分
            scores[row][1] = row + 1;   // 存储原始用例编号（从1开始）
        }

        // 3. 自定义排序（降序总分，升序ID）
        java.util.Arrays.sort(scores,
                (left, right)
                        -> left[0] != right[0] ? Long.compare(right[0], left[0])  // 总分不同 → 降序（高分在前）
                        : Long.compare(left[1], right[1]) // 总分相同 → 升序（ID小的在前）
        );

        // 4. 提取排序后的用例ID，作为结果返回
        int[] result = new int[scores.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (int) scores[i][1]; // 将long转为int（ID范围安全）
        }
        return result;
    }


    public static void main(String[] args) {
        long[] priorities = new long[]{1, 1, 2, 3, 5};
        int[][] testCases = new int[][]{{1, 2, 3}, {1, 5}, {3, 4, 5}, {2, 3, 4}};

        System.out.println(Arrays.toString(planTestExecution(priorities, testCases)));
    }

}
