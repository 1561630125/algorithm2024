package OD.搜索与枚举;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 14:14
 */
public class 卡路里组合 {

    Long dfs(long[] values, int start, long target, int chosen, boolean[] vis, LinkedList<Long> temp) {

        if (temp.size() == chosen) {
            Optional<Long> sum = temp.stream().reduce(Long::sum);
            if (sum.isPresent() && sum.get() == target) {
                return 1L;
            }
        }else {
            return 0L;
        }

        long res = 0;
        long cur = 0;
        for(int i = start; i < values.length; i++) {
            cur += values[i];
            if (cur > target) break;


            temp.add(values[i]);
            vis[i] = true;
            res += dfs(values, i + 1, target - cur, chosen - temp.size() , vis, temp);
            cur -= values[i];
            temp.remove(temp.size() - 1);
            vis[i] = false;
        }

        return res;
    }

    long countWorkoutPlans(long[] calories, long target, int chosen) {
        long[] values = calories.clone();
        Arrays.sort(values);
        boolean[] vis = new boolean[values.length];
        long cur = 0;
        LinkedList<Long> temp = new LinkedList<>();
        long res = 0;


        return res;
    }



    /**
     * 统计从 calories 中选 chosen 个数、和恰好等于 target 的方案数
     *
     * @param calories 候选数值数组
     * @param target   目标和
     * @param chosen   要选几个数
     * @return 方案数
     */
    long countWorkoutPlans2(long[] calories, long target, int chosen) {
        // chosen 必须合法：至少 1 个，且不能超过数组长度
        if (chosen <= 0 || chosen > calories.length) return 0;

        // 从下标 0 开始选，还需选 chosen 个，当前和为 0
        return search(calories, target, 0, chosen, 0);
    }

    /**
     * 回溯：从 calories[start..] 中选 remaining 个数，看和是否等于 target
     *
     * @param calories  候选数值数组
     * @param target    目标和
     * @param start     当前可以从哪个下标开始选（保证组合不重复）
     * @param remaining 还需要选几个数
     * @param total     当前已选数的和
     * @return 满足条件的方案数
     */
    private long search(long[] calories, long target, int start, int remaining, long total) {

        // 已经选够 remaining 个数，判断和是否等于 target
        if (remaining == 0) return total == target ? 1 : 0;

        long count = 0;

        // 从 start 开始枚举下一个选中的数
        // index <= calories.length - remaining：保证后面还有足够的数可选
        for (int index = start; index <= calories.length - remaining; index++) {

            // 选 calories[index]，继续从 index+1 选剩下的 remaining-1 个
            count += search(calories, target, index + 1, remaining - 1,
                    total + calories[index]);
        }

        return count;
    }



    long countWorkoutPlans3(long[] calories, long target, int chosen) {
        if (chosen <= 0 || chosen > calories.length) return 0;

        // 1. 排序，方便剪枝
        long[] values = calories.clone();
        java.util.Arrays.sort(values);

        // 2. 排序后，如果最小的 chosen 个数之和都 > target，直接无解
        long minSum = 0;
        for (int i = 0; i < chosen; i++) minSum += values[i];
        if (minSum > target) return 0;

        // 3. 如果最大的 chosen 个数之和都 < target，直接无解
        long maxSum = 0;
        for (int i = values.length - chosen; i < values.length; i++) maxSum += values[i];
        if (maxSum < target) return 0;

        return search3(values, target, 0, chosen, 0);
    }

    private long search3(long[] values, long target, int start, int remaining, long total) {

        // 剪枝1：和已经超过 target，后面只会更大（数组升序，都是正数）
        if (total > target) return 0;

        // 剪枝2：已选够个数，判断和
        if (remaining == 0) return total == target ? 1 : 0;

        long count = 0;

        // 剪枝3：index <= length - remaining，保证后面数够选
        for (int index = start; index <= values.length - remaining; index++) {

            // 剪枝4：如果加上当前数已经超过 target，后面更大，直接 break
            if (total + values[index] > target) break;

            count += search3(values, target, index + 1, remaining - 1,
                    total + values[index]);
        }

        return count;
    }

}
