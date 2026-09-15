package OD.贪心和动态规划;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 线性 DP + 单调队列优化
 *
 * @author faming.yang@hand-china.com 2026-09-15 18:11
 */
public class 跳格得分 {

    long maximumJumpScore(long[] scores, int maxStep) {
        // write code here
        // dp[i] 跳到索引i最高得分
        long[] dp = new long[scores.length + 1];


        for (int i = 1; i <= scores.length; i++) {

            for (int j = 1; j <= maxStep; j++) {

                int target = i + j;

                dp[target] = Math.max(dp[target], scores[i] + 1);
            }


        }

        return 0;
    }


    class Solution {
        /**
         * 在一维数组 scores 上跳跃，每次最多向前跳 maxStep 步，
         * 求从下标 0 跳到最后一个下标能获得的最大得分（得分 = 沿途经过位置的分值之和）。
         * <p>
         * 状态转移：
         * best[i] = scores[i] + max{ best[j] | i - maxStep <= j < i }
         * <p>
         * 用【单调队列】把内层"求滑动窗口最大值"从 O(maxStep) 优化到 O(1)，
         * 整体时间复杂度 O(n)。
         *
         * @param scores  每个位置的得分
         * @param maxStep 单次最多跳多少步
         * @return 从下标 0 跳到末尾的最大总得分
         */
        long maximumJumpScore(long[] scores, int maxStep) {
            // 边界：没有位置，得分 0
            if (scores.length == 0)
                return 0;

            // 边界：maxStep <= 0 时无法正常跳跃，按题意直接返回最后一个位置的分数
            if (maxStep <= 0)
                return scores[scores.length - 1];

            int n = scores.length;

            // best[i]：从下标 0 跳到下标 i（且必须落在 i 上）能获得的最大得分
            long[] best = new long[n];

            // 单调队列：存储"候选前驱下标"，队首是窗口内 best 值最大的下标
            // 队列中 best[queue[...]] 从队首到队尾单调递减
            int[] queue = new int[n];
            int head = 0, tail = 1;   // [head, tail) 为有效区间

            // 起点：跳到下标 0 的得分就是 scores[0]
            best[0] = scores[0];
            queue[0] = 0;             // 起点入队

            // 从下标 1 开始逐个计算
            for (int index = 1; index < n; index++) {
                // ① 维护窗口左边界：可跳到 index 的前驱 j 必须满足 j >= index - maxStep
                //    把滑出窗口的下标从队首移除
                while (head < tail && queue[head] < index - maxStep)
                    head++;

                // ② 取窗口内 best 最大的前驱（队首），转移得到 best[index]
                best[index] = best[queue[head]] + scores[index];

                // ③ 维护单调性：把队尾所有 best 值 <= best[index] 的下标弹出，
                //    因为它们不可能再成为未来窗口的最大值（index 更靠右且更优）
                while (tail > head && best[queue[tail - 1]] <= best[index])
                    tail--;

                // ④ 当前下标入队
                queue[tail++] = index;
            }

            // 终点（最后一个下标）的最大得分
            return best[n - 1];
        }
    }

    class Solution3 {
        long maximumJumpScore(long[] scores, int maxStep) {
            if (scores.length == 0)
                return 0;
            if (maxStep <= 0)
                return scores[scores.length - 1];

            int n = scores.length;
            long[] best = new long[n];

            // 单调队列：存下标，对应 best 值从队首到队尾单调递减
            Deque<Integer> deque = new ArrayDeque<>();

            best[0] = scores[0];
            deque.offerLast(0);   // 起点入队

            for (int index = 1; index < n; index++) {
                // ① 队首出：滑出窗口（下标 < index - maxStep）的移除
                while (!deque.isEmpty() && deque.peekFirst() < index - maxStep)
                    deque.pollFirst();

                // ② 取队首：窗口内 best 最大的前驱
                best[index] = best[deque.peekFirst()] + scores[index];

                // ③ 队尾出：把 best 值 <= 当前 best[index] 的弹出（它们再也不会成为最大值）
                while (!deque.isEmpty() && best[deque.peekLast()] <= best[index])
                    deque.pollLast();

                // ④ 当前下标入队（队尾进）
                deque.offerLast(index);
            }

            return best[n - 1];
        }
    }

    // 朴素 DP（最容易懂，但可能超时）
    class Solution31 {
        long maximumJumpScore(long[] scores, int maxStep) {
            int n = scores.length;
            if (n == 0) return 0;
            if (maxStep <= 0) return scores[n - 1];

            long[] best = new long[n];   // best[i]：跳到 i 能获得的最大得分
            best[0] = scores[0];

            for (int i = 1; i < n; i++) {
                long maxPrev = Long.MIN_VALUE;
                // 枚举所有能跳到 i 的前驱 j（最多 maxStep 步远）
                for (int j = Math.max(0, i - maxStep); j < i; j++) {
                    maxPrev = Math.max(maxPrev, best[j]);
                }
                best[i] = maxPrev + scores[i];
            }
            return best[n - 1];
        }
    }

    class Solution32 {
        long maximumJumpScore(long[] scores, int maxStep) {
            int n = scores.length;
            if (n == 0) return 0;
            if (maxStep <= 0) return scores[n - 1];

            long[] best = new long[n];
            best[0] = scores[0];

            // 用一个"单调队列"来快速求窗口最大值
            // 队列里存下标，对应的 best 值从队首到队尾递减
            java.util.ArrayDeque<Integer> window = new java.util.ArrayDeque<>();
            window.addLast(0);   // 起点入队

            for (int i = 1; i < n; i++) {
                // -------- 第 1 步：把已经滑出窗口的（太老的）从队首删掉 --------
                // 能跳到 i 的前驱 j 必须满足 j >= i - maxStep
                while (window.peekFirst() < i - maxStep) {
                    window.pollFirst();
                }

                // -------- 第 2 步：队首就是当前窗口的最大值 --------
                int bestPrev = window.peekFirst();
                best[i] = best[bestPrev] + scores[i];

                // -------- 第 3 步：把队尾那些"不如我"的删掉 --------
                // 我的下标更靠右（活得更久）、分数还不比它们低，它们没用了
                while (!window.isEmpty() && best[window.peekLast()] <= best[i]) {
                    window.pollLast();
                }

                // -------- 第 4 步：自己入队 --------
                window.addLast(i);
            }

            return best[n - 1];
        }
    }

}



