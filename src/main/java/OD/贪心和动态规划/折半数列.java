package OD.贪心和动态规划;

/**
 * Fenwick树（树状数组）
 *
 * @author faming.yang@hand-china.com 2026-09-04 9:54
 */
public class 折半数列 {
    static class Solution {
        /**
         * 统计"可构造的序列"个数。
         * <p>
         * 题意（根据代码反推）：
         * 构造一个正整数序列，满足某种"相邻元素大小关系"的约束，
         * 且序列的最大值恰好为 n。求这样的序列总数。
         * <p>
         * 从代码的两类状态来看，约束是：
         * - same 类：相邻元素保持同一种奇偶/大小性质
         * - alternating 类：相邻元素在两种性质间交替
         * 实际上更可能是经典的"摆动序列 / 交替序列"计数：
         * 相邻元素的大小关系必须交替（上升、下降、上升……）。
         * <p>
         * 代码用树状数组（BIT）做前缀和查询，动态维护 DP 值。
         */
        long countConstructibleSequences(int n) {
            // 边界：n = 0 或 1 时只有 1 种序列
            if (n <= 1)
                return 1;

            // 四棵树状数组，分别维护四类 DP 值的前缀和：
            //   sameOdd[v]        ：以奇数 v 结尾、且保持"相同趋势"的序列数
            //   sameEven[v]       ：以偶数 v 结尾、且保持"相同趋势"的序列数
            //   alternatingOdd[v] ：以奇数 v 结尾、且处于"交替趋势"的序列数
            //   alternatingEven[v]：以偶数 v 结尾、且处于"交替趋势"的序列数
            long[] sameOdd = new long[n + 1];
            long[] sameEven = new long[n + 1];
            long[] alternatingOdd = new long[n + 1];
            long[] alternatingEven = new long[n + 1];

            long answer = 1;

            // 从小到大枚举序列的最大值 value（即序列末尾元素的值）
            for (int value = 1; value <= n; value++) {
                int limit = value / 2;   // 只能从"小于等于 value/2"的前驱转移过来

                long same, alternating;

                if ((value & 1) == 1) {
                    // ---- value 为奇数 ----
                    // same：从"偶数"前驱转移（保证与前驱同趋势）
                    same = 1 + query(sameOdd, limit);
                    // alternating：从"奇数"前驱转移（保证与前驱交替趋势）
                    alternating = 1 + query(alternatingEven, limit);

                    update(sameOdd, value, same);
                    update(alternatingOdd, value, alternating);

                } else {
                    // ---- value 为偶数 ----
                    same = 1 + query(sameEven, limit);
                    alternating = 1 + query(alternatingOdd, limit);

                    update(sameEven, value, same);
                    update(alternatingEven, value, alternating);
                }

                // 当 value 恰好等于 n 时，记录以 n 结尾的两类序列总数
                // 减 1 是因为 same 与 alternating 中有重复计数（通常是最短的单个元素序列）
                if (value == n)
                    answer = same + alternating - 1;
            }

            return answer;
        }

        /**
         * 树状数组前缀和查询：求 tree[1..index] 的和
         */
        private long query(long[] tree, int index) {
            long result = 0;
            while (index > 0) {
                result += tree[index];
                index -= index & -index;   // 去掉最低位的 1，跳到上一个覆盖区间
            }
            return result;
        }

        /**
         * 树状数组单点更新：把 tree[index] 增加 value
         */
        private void update(long[] tree, int index, long value) {
            while (index < tree.length) {
                tree[index] += value;
                index += index & -index;   // 加上最低位的 1，跳到下一个覆盖区间
            }
        }
    }

    // 最朴素的暴力：DFS 枚举所有序列
    class Solution3 {
        int n;
        long count;

        long countConstructibleSequences(int n) {
            if (n <= 1) return 1;
            this.n = n;
            this.count = 0;

            // 枚举序列的起点：1..n
            for (int start = 1; start <= n; start++) {
                // dfs(last, mode)
                //   last : 当前序列最后一个元素的值
                //   mode : 0 = 无（起点，还没有趋势）
                //          1 = same 趋势（下一步应与当前同奇偶）
                //          2 = alternating 趋势（下一步应与当前反奇偶）
                dfs(start, 0);
            }
            return count;
        }

        /**
         * @param last 当前序列末尾元素
         * @param mode 当前趋势：
         *             0 = 起点（无趋势）
         *             1 = same 趋势
         *             2 = alternating 趋势
         */
        void dfs(int last, int mode) {
            // 到达 n，说明找到一条以 n 结尾的合法序列
            if (last == n) {
                count++;
                return;
            }

            // 关键约束：下一个元素 next 必须 >= 2 * last
            for (int next = 2 * last; next <= n; next++) {
                int nextMode;

                if (mode == 0) {
                    // 起点后第一步：可以走 same，也可以走 alternating
                    // 枚举两种趋势
                    dfs(next, 1);
                    dfs(next, 2);
                    continue;
                }

                if (mode == 1) {
                    // 当前是 same 趋势：next 必须与 last 同奇偶
                    if ((next & 1) == (last & 1)) {
                        nextMode = 1;
                    } else {
                        continue;   // 不满足同奇偶，剪枝
                    }
                } else {
                    // 当前是 alternating 趋势：next 必须与 last 反奇偶
                    if ((next & 1) != (last & 1)) {
                        nextMode = 2;
                    } else {
                        continue;   // 不满足反奇偶，剪枝
                    }
                }

                dfs(next, nextMode);
            }
        }
    }

    // DP 但不用树状数组
    class Solution2 {
        long countConstructibleSequences(int n) {
            if (n <= 1) return 1;

            // dp[i][0] = 以 i 结尾、趋势为"相同"的序列数
            // dp[i][1] = 以 i 结尾、趋势为"交替"的序列数
            // 这里把原代码的奇偶拆分也保留，用二维数组直接模拟
            long[][] sameOdd = new long[n + 1][2];  // [value][?] 仅占位
            long[] same = new long[n + 1];
            long[] alt = new long[n + 1];

            long answer = 1;

            for (int value = 1; value <= n; value++) {
                int limit = value / 2;
                long s, a;

                if ((value & 1) == 1) {
                    // 暴力前缀和：把 1..limit 全部加一遍
                    long sumSame = 0, sumAlt = 0;
                    for (int k = 1; k <= limit; k++) {
                        if ((k & 1) == 1) sumSame += same[k];   // 从奇数 same 转移
                        if ((k & 1) == 0) sumAlt += alt[k];    // 从偶数 alt 转移
                    }
                    s = 1 + sumSame;
                    a = 1 + sumAlt;
                } else {
                    long sumSame = 0, sumAlt = 0;
                    for (int k = 1; k <= limit; k++) {
                        if ((k & 1) == 0) sumSame += same[k];
                        if ((k & 1) == 1) sumAlt += alt[k];
                    }
                    s = 1 + sumSame;
                    a = 1 + sumAlt;
                }

                same[value] = s;
                alt[value] = a;

                if (value == n)
                    answer = s + a - 1;
            }

            return answer;
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.countConstructibleSequences(6));
    }

}
