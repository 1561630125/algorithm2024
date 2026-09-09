package OD.数据结构与区间;

import java.util.HashMap;
import java.util.Map;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-09 13:18
 */
public class 可整除片段 {

    /**
     * 判断数组中是否存在连续子数组，其和能被 target 整除
     *
     * 题目背景：
     * 给定一个整数数组 cards 和一个目标数 target
     * 判断是否存在一个连续子数组，使得子数组的和能被 target 整除
     *
     * 核心原理：
     * 利用前缀和 + 同余定理（抽屉原理）
     *
     * 数学依据：
     * 如果两个前缀和除以 divisor 的余数相同，
     * 那么它们之间的子数组和一定能被 divisor 整除
     *
     * 例如：
     * cards = [2, 4, 3, 1], target = 3
     * 前缀和: [0, 2, 6, 9, 10]
     * 余数:   [0, 2, 0, 0, 1]
     * 余数0出现多次 → 存在子数组和能被3整除
     * 例如：索引1到2: [4, 3] = 7? 不对
     * 索引0到2: [2, 4, 3] = 9 ✅ 能被3整除
     *
     * @param target 目标除数
     * @param cards 输入的整数数组
     * @return 如果存在这样的连续子数组，返回 true；否则返回 false
     */
    boolean hasDivisibleSubarray(long target, long[] cards) {
        // ========== 处理 target = 0 的特殊情况 ==========
        // 如果 target 为 0，任何数除以 0 都是无意义的
        // 但这里取绝对值，让 divisor = 0
        // 后续会特殊处理 divisor == 0 的情况
        long divisor = Math.abs(target);

        // ========== 初始化 ==========
        long prefix = 0L;  // 前缀和
        // seen 存储所有出现过的余数
        java.util.Set<Long> seen = new java.util.HashSet<>();
        seen.add(0L);  // 前缀和为 0 的余数是 0

        // ========== 遍历数组，计算前缀和 ==========
        for (long card : cards) {
            prefix += card;  // 累加前缀和

            // ========== 计算当前前缀和除以 divisor 的余数 ==========
            // 处理两种特殊情况：
            // 1. divisor == 0：直接使用前缀和作为"余数"（因为不能取模）
            // 2. divisor != 0：使用 ((prefix % divisor) + divisor) % divisor
            //    确保余数为非负数（Java 的 % 可能是负数）
            long remainder = divisor == 0L ?
                    prefix :
                    ((prefix % divisor) + divisor) % divisor;

            // ========== 检查余数是否出现过 ==========
            // 如果余数已经存在，说明找到了一个连续子数组
            // 其和能被 divisor 整除
            if (!seen.add(remainder)) {
                return true;  // 找到了！
            }
            // 否则继续遍历
        }

        // ========== 遍历结束，没有找到 ==========
        return false;
    }


    /**
     * 判断是否存在长度至少为 2 的连续子数组，其和能被 target 整除
     *
     * 例如：LeetCode 523. Continuous Subarray Sum
     *
     * 关键区别：长度至少为 2
     * 需要延迟添加余数，避免刚添加的余数被立即匹配
     */
    boolean hasDivisibleSubarrayAtLeastK(long target, long[] cards, int minLength) {
        long divisor = Math.abs(target), prefix = 0L;
        Map<Long, Integer> seen = new HashMap<>();  // 存储余数 → 最近索引
        seen.put(0L, -1);  // 前缀和0在索引-1

        for (int i = 0; i < cards.length; i++) {
            prefix += cards[i];
            long remainder = divisor == 0L ? prefix :
                    ((prefix % divisor) + divisor) % divisor;

            // 检查余数是否出现过，且距离至少为 minLength
            if (seen.containsKey(remainder)) {
                int prevIndex = seen.get(remainder);
                if (i - prevIndex >= minLength) {  // ✅ 长度要求！
                    return true;
                }
            } else {
                // 只有余数不存在时才记录
                // 注意：保留第一次出现的位置（最小的索引）
                seen.put(remainder, i);
            }
        }
        return false;
    }

    /**
     * 判断是否存在长度恰好为 k 的连续子数组，其和能被 target 整除
     *
     * 思路：使用固定大小的滑动窗口
     */
    boolean hasDivisibleSubarrayExactlyK(long target, long[] cards, int k) {
        if (k <= 0 || k > cards.length) return false;

        long divisor = Math.abs(target);
        long windowSum = 0;

        // ====== 方法1：滑动窗口（推荐） ======
        // 1. 计算第一个窗口
        for (int i = 0; i < k; i++) {
            windowSum += cards[i];
        }
        if (windowSum % divisor == 0) return true;

        // 2. 滑动窗口
        for (int i = k; i < cards.length; i++) {
            windowSum = windowSum - cards[i - k] + cards[i];
            if (windowSum % divisor == 0) return true;
        }
        return false;

        // ====== 方法2：前缀和 + 同余（更通用） ======
        // 需要长度为 k，即 prefix[i] - prefix[i-k] 能被整除
        // 等价于 prefix[i] ≡ prefix[i-k] (mod divisor)
    }


    /**
     * 判断是否存在长度在 [minLen, maxLen] 之间的连续子数组
     * 其和能被 target 整除
     */
    boolean hasDivisibleSubarrayInRange(long target, long[] cards, int minLen, int maxLen) {
        if (minLen <= 0 || maxLen > cards.length) return false;

        long divisor = Math.abs(target);
        long[] prefix = new long[cards.length + 1];

        // ====== 1. 计算前缀和 ======
        for (int i = 0; i < cards.length; i++) {
            prefix[i + 1] = prefix[i] + cards[i];
        }

        // ====== 2. 检查所有长度在 [minLen, maxLen] 的子数组 ======
        Map<Long, Integer> seen = new HashMap<>();

        for (int i = 0; i <= cards.length; i++) {
            long remainder = divisor == 0L ? prefix[i] :
                    ((prefix[i] % divisor) + divisor) % divisor;

            // 检查 i - j 是否在 [minLen, maxLen] 范围内
            // 需要找到之前出现的相同的余数
            // 且索引差在范围内

            // 方法：使用滑动窗口 + 延迟删除
            // 或者使用 TreeMap 维护范围内的索引
        }

        // 简化实现：直接枚举长度
        for (int length = minLen; length <= maxLen; length++) {
            long windowSum = 0;
            for (int i = 0; i < length; i++) {
                windowSum += cards[i];
            }
            if (windowSum % divisor == 0) return true;

            for (int i = length; i < cards.length; i++) {
                windowSum = windowSum - cards[i - length] + cards[i];
                if (windowSum % divisor == 0) return true;
            }
        }
        return false;
    }




    public static void main(String[] args) {

    }

}
