package OD.搜索与枚举;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 17:45
 */
public class 凑和数组 {

    int dfs(int[] values, int cur, int target, int min){
        if (target == cur) {
            return 1;
        }
        if (min != Integer.MAX_VALUE){
            for(int i = 1; i < min; i++) {
                if (cur + i == target) return 1;
            }
        }


        for(int i = 0; i < values.length; i++) {
            if (cur + values[i] > target) break;
            dfs(values,cur + values[i], target, Math.min(values[i], min));

        }

        return 0;
    }

    int countAssembledArrays(int[] numbers, int target) {
        // 排序nums

        dfs(numbers, 0, target, Integer.MAX_VALUE);

        return 0;
    }


    /**
     * 统计用 numbers 里的数拼出 target 的方案数
     * 要求：拼出的数组里，每个数都必须在 numbers 中，且数组长度 >= 1
     * 额外的尾部处理：允许在末尾追加一个「小于 minimum 且不在 numbers 中」的数
     *
     * @param numbers 可用的数
     * @param target  目标和
     * @return 方案数
     */
    int countAssembledArrays2(int[] numbers, int target) {

        // 参数校验
        if (target < 0 || numbers == null || numbers.length == 0)
            return 0;

        int minimum = Integer.MAX_VALUE;          // numbers 中的最小值
        boolean[] present = new boolean[target + 1]; // present[v] 表示 v 在 numbers 中

        for (int value : numbers) {
            // 出现非正数，直接判无效
            if (value <= 0)
                return 0;

            minimum = Math.min(minimum, value);

            // 只关心 <= target 的数
            if (value <= target)
                present[value] = true;
        }

        // ways[s]：用 numbers 中的数拼出和 s 的方案数（完全背包，顺序无关）
        long[] ways = new long[target + 1];
        ways[0] = 1;  // 和为 0 的方案：什么都不选

        // 完全背包：外层枚举「用哪个数」，内层枚举和
        // 这样保证每个数可以重复用，且组合不重复计数
        for (int value = 1; value <= target; value++)
            if (present[value])
                for (int sum = value; sum <= target; sum++)
                    ways[sum] += ways[sum - value];

        // 主结果：恰好拼出 target 的方案数
        long result = ways[target];

        // 额外部分：在末尾追加一个 outside（1 <= outside < minimum）
        // 这个 outside 不在 numbers 中（因为比最小值还小）
        // 追加后总和 = target，所以前面部分要拼出 target - outside
        for (int outside = 1; outside < minimum && outside <= target; outside++)
            result += ways[target - outside];

        return (int) result;
    }

}
