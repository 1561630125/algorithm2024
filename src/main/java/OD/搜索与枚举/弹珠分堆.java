package OD.搜索与枚举;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 14:45
 */
public class 弹珠分堆 {

    long dfs(long cur) {
        if (cur <= 2) {
            return 1;
        }

        return dfs(cur / 2) + dfs((cur + 1) / 2);
    }


    long[] splitMarbles(long marbles) {

        return new long[] {0, 1 };
    }


    /**
     * 将 marbles 个弹珠按"每次尽量对半分"的规则不断拆分，
     * 返回拆分后的【内部节点数, 叶子堆总数】。
     *
     * 例如 marbles = 5：
     *        5
     *      /   \
     *     2     3
     *    / \   / \
     *   1   1 1   2
     *             / \
     *            1   1
     * 内部节点 = 4，叶子堆 = 5 → 返回 {4, 5}
     */
    long[] splitMarbles2(long marbles) {
        // 记忆化表：value -> 该堆最终拆出的叶子数，避免重复计算相同子问题
        java.util.Map<Long, Long> memo = new java.util.HashMap<>();

        // 计算 marbles 最终能拆出多少个叶子堆
        long parts = leaves(marbles, memo);

        // 关键性质：一棵二叉树中，叶子数 = 内部节点数 + 1
        // 所以 内部节点数 = parts - 1
        return new long[]{parts - 1, parts};
    }

    /**
     * 递归计算：一堆 value 个弹珠最终能拆成多少个"不可再分"的叶子堆。
     * 终止条件：value <= 2 时视为一个叶子，不再拆分。
     */
    private long leaves(long value, java.util.Map<Long, Long> memo) {
        // 递归基：<= 2 的堆直接算作 1 个叶子（不可再分）
        if (value <= 2) return 1;

        // 查记忆表，命中则直接返回，避免重复递归
        Long cached = memo.get(value);
        if (cached != null) return cached;

        // 拆成两半：左半 value/2（向下取整），右半 (value+1)/2（向上取整）
        // 两者之和恒等于 value，保证不重不漏
        long result = leaves(value / 2, memo)
                + leaves((value + 1) / 2, memo);

        // 记录结果到记忆表后再返回
        memo.put(value, result);
        return result;
    }

}
