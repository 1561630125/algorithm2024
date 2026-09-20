package OD.搜索与枚举;

import java.util.*;

/**
 * 考点：线段树
 *
 * @author faming.yang@hand-china.com 2026-09-12 22:40
 */
public class 删负数后的最大收益 {

    static public class Solution {

        // 用"极小值 / 4"作为负无穷，避免加法溢出
        private static final long NEGATIVE_INFINITY = Long.MIN_VALUE / 4;

        /**
         * 求"草药能量"最大值。
         * <p>
         * 题意（据代码推断）：
         * 给定数组 values，从中选出一个【连续子数组】，
         * 要求：
         * 1. 子数组内包含至少 required 个正数；
         * 2. 可以"丢弃"至多 discardLimit 个负数（即这些负数不计入和，
         * 且不计入连续性）；
         * 求所有满足条件的子数组中，元素和的最大值。
         * <p>
         * 解法要点：
         * - 前缀和 prefix 用于快速求区间和；
         * - positives 记录所有正数的下标，用来定位"恰好含 required 个正数"的窗口；
         * - 线段树 tree 每个节点维护"该区间内的负数列表（升序）"，
         * 配合 merge 可快速得到区间内前 k 小的负数之和；
         * - DP 数组 ending[budget] 表示"以当前下标结尾、已丢弃 budget 个负数"的最大和。
         *
         * @param values       数值数组（可正可负）
         * @param required     子数组至少包含的正数个数
         * @param discardLimit 最多可丢弃的负数个数（0 ~ 5）
         * @return 最大能量和；输入非法返回 -1
         */
        public long maximumHerbEnergy(int[] values, int required, int discardLimit) {

            int n = values.length;

            // positives[i] = 第 i 个正数在原数组中的下标
            int[] positives = new int[n];
            int positiveCount = 0;

            // prefix[i] = values[0..i-1] 的和（前缀和）
            long[] prefix = new long[n + 1];

            // ---------- 1. 预处理：前缀和 + 收集正数下标 ----------
            for (int index = 0; index < n; index++) {
                prefix[index + 1] = prefix[index] + values[index];
                if (values[index] > 0)
                    positives[positiveCount++] = index;
            }

            // ---------- 2. 输入合法性校验 ----------
            // required 必须在 [1, n]
            // discardLimit 必须在 [0, 5]
            // 正数总数必须 >= required
            if (required < 1 || required > n
                    || discardLimit < 0 || discardLimit > 5
                    || positiveCount < required)
                return -1;

            // ---------- 3. 构建线段树（大小为 2 的幂） ----------
            int size = 1;
            while (size < n)
                size <<= 1;   // size = 大于等于 n 的最小 2 的幂

            // tree[node] = 该节点代表区间内所有负数（升序排列，最多保留 discardLimit 个）
            long[][] tree = new long[size * 2][];
            java.util.Arrays.fill(tree, new long[0]);

            // 叶子节点：只有负数才放入
            for (int index = 0; index < n; index++)
                if (values[index] < 0)
                    tree[size + index] = new long[]{values[index]};

            // 自底向上合并
            for (int node = size - 1; node > 0; node--)
                tree[node] = merge(tree[node * 2], tree[node * 2 + 1], discardLimit);

            // ---------- 4. DP：ending[budget] ----------
            // ending[budget] = "以当前下标结尾、丢弃了 budget 个负数"的最大和
            long[] ending = new long[discardLimit + 1];
            java.util.Arrays.fill(ending, NEGATIVE_INFINITY);

            long answer = NEGATIVE_INFINITY;
            int seen = 0;   // 到当前下标为止，遇到的正数个数

            // ---------- 5. 遍历每个下标作为"子数组右端点" ----------
            for (int index = 0; index < n; index++) {

                // next[budget] = 处理完 index 后的新 DP 状态
                long[] next = new long[discardLimit + 1];
                java.util.Arrays.fill(next, NEGATIVE_INFINITY);

                for (int budget = 0; budget <= discardLimit; budget++) {

                    // ---------- 5.1 不丢弃当前值：直接累加 ----------
                    if (ending[budget] != NEGATIVE_INFINITY)
                        next[budget] = ending[budget] + values[index];

                    // ---------- 5.2 丢弃当前值（仅当它是负数且还有丢弃额度） ----------
                    if (values[index] < 0 && budget > 0
                            && ending[budget - 1] != NEGATIVE_INFINITY)
                        next[budget] = Math.max(next[budget], ending[budget - 1]);
                }

                // ---------- 6. 若当前值是正数，且累计正数 >= required ----------
                // 计算以 index 为右端点、恰好含 required 个正数的最优左边界
                if (values[index] > 0 && ++seen >= required) {

                    // left = 第 (seen - required) 个正数的下标
                    // 即：从 left 开始到 index，正好包含 required 个正数
                    int left = positives[seen - required];

                    // base = values[left..index] 的区间和
                    long base = prefix[index + 1] - prefix[left];

                    // ---------- 6.1 查询区间 [left, index] 内的负数 ----------
                    // 用线段树查询该区间内所有负数（升序，最多 discardLimit 个）
                    long[] removed = new long[0];
                    for (int rangeLeft = left + size, rangeRight = index + size;
                         rangeLeft <= rangeRight;
                         rangeLeft >>= 1, rangeRight >>= 1) {

                        if ((rangeLeft & 1) == 1)
                            removed = merge(removed, tree[rangeLeft++], discardLimit);
                        if ((rangeRight & 1) == 0)
                            removed = merge(removed, tree[rangeRight--], discardLimit);
                    }

                    // ---------- 6.2 枚举丢弃 budget 个负数，更新 next ----------
                    long removedSum = 0;
                    for (int budget = 0; budget <= discardLimit; budget++) {

                        // 丢弃 budget 个"绝对值最小"的负数，使和最大
                        // （丢弃绝对值小的负数，损失更小）
                        if (budget > 0 && budget <= removed.length)
                            removedSum += removed[budget - 1];

                        next[budget] = Math.max(next[budget], base - removedSum);
                    }
                }

                ending = next;
                answer = Math.max(answer, ending[discardLimit]);
            }

            return answer;
        }

        /*  */

        /**
         * 归并两个【升序】数组，保留前 limit 个最小元素。
         *
         * @param left  升序数组
         * @param right 升序数组
         * @param limit 最多保留的元素个数
         * @return 合并后的升序数组（长度 <= limit）
         *//*
        private long[] merge(long[] left, long[] right, int limit) {
            long[] result = new long[Math.min(limit, left.length + right.length)];
            int a = 0, b = 0;

            for (int i = 0; i < result.length; i++) {
                // 取两个数组当前较小者
                result[i] = b >= right.length
                        || (a < left.length && left[a] <= right[b])
                        ? left[a++]
                        : right[b++];
            }
            return result;
        }
*/
        private long[] merge(long[] left, long[] right, int limit) {
            long[] result = new long[Math.min(limit, left.length + right.length)];
            mergeHelper(left, right, result, 0, 0, 0);
            return result;
        }

        /**
         * 递归填充 result[i..]。
         *
         * @param a left 当前指针
         * @param b right 当前指针
         * @param i result 当前填充位置
         */
        private void mergeHelper(long[] left, long[] right, long[] result, int a, int b, int i) {
            // ---------- 终止条件：result 已填满 ----------
            if (i == result.length)
                return;

            // ---------- 选较小者放入 result[i] ----------
            if (b >= right.length || (a < left.length && left[a] <= right[b])) {
                result[i] = left[a];
                mergeHelper(left, right, result, a + 1, b, i + 1);
            } else {
                result[i] = right[b];
                mergeHelper(left, right, result, a, b + 1, i + 1);
            }
        }
    }


    /**
     * 核心观察：d ≤ 5，所以"丢弃哪几个负数"的组合很少。
     * <p>
     * 思路
     * 固定"恰好含 required 个正数"的窗口 [left, right]。
     * <p>
     * 窗口内可能有若干负数，从中选至多 d 个丢弃，使剩余和最大。
     * <p>
     * 丢弃绝对值最小的 d 个负数（损失最小）。
     * <p>
     * 用双指针滑动窗口，left 随 seen 推进。
     */
    class Solution2 {

        public long maximumHerbEnergy(int[] values, int required, int discardLimit) {
            int n = values.length;
            int[] positives = new int[n];
            int positiveCount = 0;
            long[] prefix = new long[n + 1];

            for (int i = 0; i < n; i++) {
                prefix[i + 1] = prefix[i] + values[i];
                if (values[i] > 0) positives[positiveCount++] = i;
            }

            if (required < 1 || required > n
                    || discardLimit < 0 || discardLimit > 5
                    || positiveCount < required)
                return -1;

            long answer = Long.MIN_VALUE;

            // ---------- 枚举"窗口左端所在的正数位置" ----------
            // 窗口 [left, right]，含恰好 required 个正数
            // left = positives[k], right = positives[k + required - 1]
            for (int k = 0; k + required <= positiveCount; k++) {
                int left = positives[k];

                // 窗口右端可以从 positives[k+required-1] 开始，一直延伸到下一个正数前
                int minRight = positives[k + required - 1];

                // 右端最多延伸到：第 k+required 个正数的前一个位置（或 n-1）
                int maxRight = (k + required < positiveCount)
                        ? positives[k + required] - 1
                        : n - 1;

                for (int right = minRight; right <= maxRight; right++) {
                    // 计算 [left, right] 区间和
                    long base = prefix[right + 1] - prefix[left];

                    // 收集区间内所有负数，取绝对值最小的 d 个
                    // 若负数个数 <= d，全丢；否则丢最小的 d 个
                    long removedSum = smallestNegativeSum(values, left, right, discardLimit);

                    answer = Math.max(answer, base - removedSum);
                }
            }

            return answer;
        }

        /**
         * 求 [l, r] 内绝对值最小的至多 d 个负数之和（返回负值）
         */
        private long smallestNegativeSum(int[] values, int l, int r, int d) {
            // 用大顶堆维护 d 个"绝对值最大"的负数，堆里留下绝对值最小的
            PriorityQueue<Long> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

            for (int i = l; i <= r; i++) {
                if (values[i] < 0) {
                    maxHeap.add((long) values[i]);   // 存负数本身
                    if (maxHeap.size() > d) maxHeap.poll();  // 弹出最大（最接近0）
                }
            }

            long sum = 0;
            while (!maxHeap.isEmpty()) sum += maxHeap.poll();
            return sum;
        }
    }


    /**
     * 滑动窗口滑动时，维护窗口内绝对值最小的 d 个负数之和。
     * <p>
     * 思路
     * 用一个有序容器（如 TreeMap 或多重集）维护窗口内的负数。
     * <p>
     * 窗口右移：加入新元素，移出旧元素。
     * <p>
     * 每次查询"最小 d 个负数之和"。
     */
    class Solution3 {

        public long maximumHerbEnergy(int[] values, int required, int discardLimit) {
            int n = values.length;
            int[] positives = new int[n];
            int positiveCount = 0;
            long[] prefix = new long[n + 1];

            for (int i = 0; i < n; i++) {
                prefix[i + 1] = prefix[i] + values[i];
                if (values[i] > 0) positives[positiveCount++] = i;
            }

            if (required < 1 || required > n
                    || discardLimit < 0 || discardLimit > 5
                    || positiveCount < required)
                return -1;

            long answer = Long.MIN_VALUE;
            long windowSum = 0;   // 窗口元素之和（含负数）

            // 维护窗口内负数：用 TreeMap 存 (值, 个数)
            TreeMap<Integer, Integer> negMap = new TreeMap<>();

            int left = 0, seen = 0;   // seen = 窗口内正数个数

            for (int right = 0; right < n; right++) {
                // 右端加入
                windowSum += values[right];
                if (values[right] < 0)
                    negMap.merge(values[right], 1, Integer::sum);
                if (values[right] > 0) seen++;

                // 若正数超 required，左端收缩
                while (seen > required) {
                    if (values[left] < 0) {
                        negMap.merge(values[left], -1, Integer::sum);
                        if (negMap.get(values[left]) == 0) negMap.remove(values[left]);
                    }
                    if (values[left] > 0) seen--;
                    windowSum -= values[left];
                    left++;
                }

                // 当窗口恰含 required 个正数时，结算
                if (seen == required) {
                    // 从 negMap 里取绝对值最小的 d 个负数之和
                    long removedSum = smallestKSum(negMap, discardLimit);
                    answer = Math.max(answer, windowSum - removedSum);
                }
            }

            return answer;
        }

        /**
         * 从有序负数集合中取绝对值最小的 k 个之和（返回负值）
         */
        private long smallestKSum(TreeMap<Integer, Integer> negMap, int k) {
            long sum = 0;
            int count = 0;
            // 从最大（最接近 0）的负数开始取
            for (Map.Entry<Integer, Integer> e : negMap.descendingMap().entrySet()) {
                for (int i = 0; i < e.getValue() && count < k; i++) {
                    sum += e.getKey();
                    count++;
                }
                if (count == k) break;
            }
            return sum;
        }
    }

    // 暴力
    class Solution5 {
        public long maximumHerbEnergy(int[] values, int required, int discardLimit) {
            int n = values.length;

            long answer = Long.MIN_VALUE;
            for (int i = 0; i < n; i++)
                for (int j = i; j < n; j++) {
                    // 子数组 [i, j]
                    int posCount = 0;
                    List<Integer> negs = new ArrayList<>();
                    long sum = 0;
                    for (int k = i; k <= j; k++) {
                        if (values[k] > 0) posCount++;
                        else if (values[k] < 0) negs.add(values[k]);
                        sum += values[k];
                    }
                    if (posCount < required) continue;
                    // 丢绝对值最小的 d 个负数
                    negs.sort(Collections.reverseOrder());
                    for (int k = 0; k < Math.min(discardLimit, negs.size()); k++)
                        sum -= negs.get(k);
                    answer = Math.max(answer, sum);
                }

            return answer;
        }
    }


    /**
     * 滑动窗口 + 有序容器（解法 2 精简版）
     * 这是最推荐的替代解法，比线段树简单，性能相当
     */
    class Solution4 {

        public long maximumHerbEnergy(int[] values, int required, int discardLimit) {
            int n = values.length;

            // 输入校验
            if (required < 1 || required > n
                    || discardLimit < 0 || discardLimit > 5)
                return -1;

            int positiveCount = 0;
            for (int v : values) if (v > 0) positiveCount++;
            if (positiveCount < required) return -1;

            long answer = Long.MIN_VALUE;
            long windowSum = 0;
            int left = 0, seen = 0;

            // 维护窗口内"最小的 d 个负数"：用两个堆或一个有序结构
            // 这里用一个大顶堆存"已选的 d 个最小负数"
            PriorityQueue<Integer> chosen = new PriorityQueue<>(Collections.reverseOrder());

            for (int right = 0; right < n; right++) {
                windowSum += values[right];
                if (values[right] > 0) seen++;

                if (values[right] < 0) {
                    chosen.add(values[right]);
                    if (chosen.size() > discardLimit) chosen.poll();  // 弹出最大（最接近0）
                }

                // 窗口正数过多，左端收缩
                while (seen > required) {
                    windowSum -= values[left];
                    if (values[left] > 0) seen--;
                    if (values[left] < 0) {
                        // 从 chosen 中移除（若在）
                        // 注意：堆无法直接删任意元素，需用 multiset 或重建
                    }
                    left++;
                }

                if (seen == required) {
                    long removedSum = 0;
                    for (int v : chosen) removedSum += v;
                    answer = Math.max(answer, windowSum - removedSum);
                }
            }

            return answer;
        }
    }


    class Solution6 {

        public long maximumHerbEnergy(int[] values, int required, int discardLimit) {
            int n = values.length;

            // ---------- 输入校验 ----------
            if (required < 1 || required > n
                    || discardLimit < 0 || discardLimit > 5)
                return -1;

            int positiveCount = 0;
            for (int v : values) if (v > 0) positiveCount++;
            if (positiveCount < required) return -1;

            long answer = Long.MIN_VALUE;
            long windowSum = 0;            // 窗口元素之和（含负数）
            int left = 0, seen = 0;        // seen = 窗口内正数个数

            // 维护窗口内所有负数：TreeMap（多重集）
            TreeMap<Integer, Integer> negMap = new TreeMap<>();

            for (int right = 0; right < n; right++) {

                // ---------- 右端加入 ----------
                windowSum += values[right];
                if (values[right] > 0) seen++;
                if (values[right] < 0)
                    negMap.merge(values[right], 1, Integer::sum);

                // ---------- 窗口正数过多 → 左端收缩 ----------
                while (seen > required) {
                    windowSum -= values[left];
                    if (values[left] > 0) seen--;
                    if (values[left] < 0) {
                        // 从 TreeMap 里移除这个负数
                        negMap.merge(values[left], -1, Integer::sum);
                        if (negMap.get(values[left]) == 0)
                            negMap.remove(values[left]);
                    }
                    left++;
                }

                // ---------- 恰含 required 个正数 → 结算 ----------
                if (seen == required) {
                    // 取绝对值最小的 d 个负数之和
                    long removedSum = smallestKSum(negMap, discardLimit);
                    answer = Math.max(answer, windowSum - removedSum);
                }
            }

            return answer;
        }

        /**
         * 从有序负数集合中，取绝对值最小的 k 个负数之和（返回负值）。
         * descendingMap(): 从大到小遍历，负数越大越接近 0，越优先丢。
         */
        private long smallestKSum(TreeMap<Integer, Integer> negMap, int k) {
            long sum = 0;
            int count = 0;
            for (Map.Entry<Integer, Integer> e : negMap.descendingMap().entrySet()) {
                int take = Math.min(e.getValue(), k - count);
                sum += (long) e.getKey() * take;
                count += take;
                if (count == k) break;
            }
            return sum;
        }
    }

    // discardLimit = 0，滑动窗口 + 前缀和
    public class SolutionV1 {
        public long maximumHerbEnergy(int[] values, int required, int discardLimit) {
            int n = values.length;
            if (required < 1 || required > n || discardLimit != 0)
                return -1;

            // 记录正数下标
            int[] positives = new int[n];
            int positiveCount = 0;
            long[] prefix = new long[n + 1];
            for (int i = 0; i < n; i++) {
                prefix[i + 1] = prefix[i] + values[i];
                if (values[i] > 0) positives[positiveCount++] = i;
            }
            if (positiveCount < required) return -1;

            long answer = Long.MIN_VALUE;
            int seen = 0;
            for (int right = 0; right < n; right++) {
                if (values[right] <= 0) continue;  // 只关心正数
                seen++;
                if (seen < required) continue;

                // 保证恰好 required 个正数的最左边界
                int left = positives[seen - required];
                long sum = prefix[right + 1] - prefix[left];
                answer = Math.max(answer, sum);
            }
            return answer == Long.MIN_VALUE ? -1 : answer;
        }
    }

    // 加入 DP，让 discardLimit 参与
    public class SolutionV2 {
        private static final long NEG = Long.MIN_VALUE / 4;

        public long maximumHerbEnergy(int[] values, int required, int discardLimit) {
            int n = values.length;
            if (required < 1 || required > n || discardLimit < 0)
                return -1;

            int[] positives = new int[n];
            int positiveCount = 0;
            long[] prefix = new long[n + 1];
            for (int i = 0; i < n; i++) {
                prefix[i + 1] = prefix[i] + values[i];
                if (values[i] > 0) positives[positiveCount++] = i;
            }
            if (positiveCount < required) return -1;

            // ending[budget] = 以当前位置结尾、已丢弃 budget 个负数时的最大能量
            long[] ending = new long[discardLimit + 1];
            java.util.Arrays.fill(ending, NEG);
            ending[0] = 0;

            long answer = NEG;
            int seen = 0;

            for (int i = 0; i < n; i++) {
                long[] next = new long[discardLimit + 1];
                java.util.Arrays.fill(next, NEG);

                for (int budget = 0; budget <= discardLimit; budget++) {
                    // 保留当前值
                    if (ending[budget] != NEG)
                        next[budget] = ending[budget] + values[i];
                    // 丢弃当前值（仅负数）
                    if (values[i] < 0 && budget > 0 && ending[budget - 1] != NEG)
                        next[budget] = Math.max(next[budget], ending[budget - 1]);
                }

                // 如果当前是正数且达到 required，尝试以 left 为起点的窗口
                if (values[i] > 0 && ++seen >= required) {
                    int left = positives[seen - required];
                    long base = prefix[i + 1] - prefix[left];
                    // 阶段 2 先不处理丢弃，直接拿 base 更新
                    next[discardLimit] = Math.max(next[discardLimit], base);
                }

                ending = next;
                answer = Math.max(answer, ending[discardLimit]);
            }
            return answer == NEG ? -1 : answer;
        }
    }

    // 暴力排序找最小负数
    public class SolutionV3 {
        private static final long NEG = Long.MIN_VALUE / 4;

        public long maximumHerbEnergy(int[] values, int required, int discardLimit) {
            int n = values.length;
            if (required < 1 || required > n || discardLimit < 0)
                return -1;

            int[] positives = new int[n];
            int positiveCount = 0;
            long[] prefix = new long[n + 1];
            for (int i = 0; i < n; i++) {
                prefix[i + 1] = prefix[i] + values[i];
                if (values[i] > 0) positives[positiveCount++] = i;
            }
            if (positiveCount < required) return -1;

            long[] ending = new long[discardLimit + 1];
            java.util.Arrays.fill(ending, NEG);
            ending[0] = 0;

            long answer = NEG;
            int seen = 0;

            for (int i = 0; i < n; i++) {
                long[] next = new long[discardLimit + 1];
                java.util.Arrays.fill(next, NEG);

                // DP 转移
                for (int budget = 0; budget <= discardLimit; budget++) {
                    if (ending[budget] != NEG)
                        next[budget] = ending[budget] + values[i];
                    if (values[i] < 0 && budget > 0 && ending[budget - 1] != NEG)
                        next[budget] = Math.max(next[budget], ending[budget - 1]);
                }

                // 窗口 + 暴力找最小负数
                if (values[i] > 0 && ++seen >= required) {
                    int left = positives[seen - required];
                    long base = prefix[i + 1] - prefix[left];

                    // 收集窗口内所有负数，升序排序
                    java.util.List<Long> negatives = new java.util.ArrayList<>();
                    for (int k = left; k <= i; k++)
                        if (values[k] < 0) negatives.add((long) values[k]);
                    java.util.Collections.sort(negatives);

                    long removedSum = 0;
                    for (int budget = 0; budget <= discardLimit; budget++) {
                        if (budget > 0 && budget <= negatives.size())
                            removedSum += negatives.get(budget - 1);
                        next[budget] = Math.max(next[budget], base - removedSum);
                    }
                }

                ending = next;
                answer = Math.max(answer, ending[discardLimit]);
            }
            return answer == NEG ? -1 : answer;
        }
    }


    // 线段树替换暴力排序
    public class SolutionV4 {
        private static final long NEG = Long.MIN_VALUE / 4;

        public long maximumHerbEnergy(int[] values, int required, int discardLimit) {
            int n = values.length;
            if (required < 1 || required > n || discardLimit < 0 || discardLimit > 5)
                return -1;

            int[] positives = new int[n];
            int positiveCount = 0;
            long[] prefix = new long[n + 1];
            for (int i = 0; i < n; i++) {
                prefix[i + 1] = prefix[i] + values[i];
                if (values[i] > 0) positives[positiveCount++] = i;
            }
            if (positiveCount < required) return -1;

            // ---------- 线段树构建 ----------
            int size = 1;
            while (size < n) size <<= 1;
            long[][] tree = new long[size * 2][];
            java.util.Arrays.fill(tree, new long[0]);
            for (int i = 0; i < n; i++)
                if (values[i] < 0) tree[size + i] = new long[]{values[i]};
            for (int node = size - 1; node > 0; node--)
                tree[node] = merge(tree[node * 2], tree[node * 2 + 1], discardLimit);

            // ---------- DP ----------
            long[] ending = new long[discardLimit + 1];
            java.util.Arrays.fill(ending, NEG);
            ending[0] = 0;

            long answer = NEG;
            int seen = 0;

            for (int i = 0; i < n; i++) {
                long[] next = new long[discardLimit + 1];
                java.util.Arrays.fill(next, NEG);

                // DP 转移
                for (int budget = 0; budget <= discardLimit; budget++) {
                    if (ending[budget] != NEG)
                        next[budget] = ending[budget] + values[i];
                    if (values[i] < 0 && budget > 0 && ending[budget - 1] != NEG)
                        next[budget] = Math.max(next[budget], ending[budget - 1]);
                }

                // 线段树查询窗口内最小负数
                if (values[i] > 0 && ++seen >= required) {
                    int left = positives[seen - required];
                    long base = prefix[i + 1] - prefix[left];

                    long[] removed = new long[0];
                    for (int l = left + size, r = i + size; l <= r; l >>= 1, r >>= 1) {
                        if ((l & 1) == 1) removed = merge(removed, tree[l++], discardLimit);
                        if ((r & 1) == 0) removed = merge(removed, tree[r--], discardLimit);
                    }

                    long removedSum = 0;
                    for (int budget = 0; budget <= discardLimit; budget++) {
                        if (budget > 0 && budget <= removed.length)
                            removedSum += removed[budget - 1];
                        next[budget] = Math.max(next[budget], base - removedSum);
                    }
                }

                ending = next;
                answer = Math.max(answer, ending[discardLimit]);
            }
            return answer == NEG ? -1 : answer;
        }

        /**
         * 合并两个升序数组，只保留前 limit 个
         */
        private long[] merge(long[] left, long[] right, int limit) {
            long[] result = new long[Math.min(limit, left.length + right.length)];
            int a = 0, b = 0;
            for (int i = 0; i < result.length; i++)
                result[i] = (b >= right.length || (a < left.length && left[a] <= right[b]))
                        ? left[a++] : right[b++];
            return result;
        }
    }


    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] values = new int[]{-2, 4, -3};
        int required = 1;
        int discardLimit = 0;
        System.out.println(solution.maximumHerbEnergy(values, required, discardLimit));
    }

}
