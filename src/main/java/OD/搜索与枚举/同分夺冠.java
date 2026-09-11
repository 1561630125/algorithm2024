package OD.搜索与枚举;

import java.util.Arrays;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-10 23:00
 */
public class 同分夺冠 {

    /**
     values = [4, 3, 2, 1]，groups = 2，total = 10，target = 5
         index=0, 放 4:
         桶0: [4, 0]
         index=1, 放 3:
         桶0: [7, 0] 超过 5，跳过
         桶1: [4, 3]
         index=2, 放 2:
         桶0: [6, 3] 超过 5，跳过
         桶1: [4, 5]
         index=3, 放 1:
         桶0: [5, 5] → index=4 检查 → 两个桶都等于 5 → 成功！
     * @return
     */


    /**
     * 回溯：把 values[index..] 分配到各个桶里
     *
     * @param values  降序排序后的点数
     * @param index   当前要分配的数
     * @param buckets 各组的当前和
     * @param target  每组的目标和
     * @param seen    已经失败过的「桶状态」，用于剪枝
     * @return 能否把所有数分完，且每组都恰好等于 target
     */
    private boolean mvpVisit(long[] values, int index, long[] buckets,
                             long target, java.util.Set<String> seen) {

        // 所有数都分配完了，检查是否每组都等于 target
        if (index == values.length) {
            for (long bucket : buckets)
                if (bucket != target) return false;
            return true;
        }

        // 把当前桶状态序列化成字符串
        // 如果这个状态之前已经失败过，直接返回 false，避免重复搜索
        String state = java.util.Arrays.toString(buckets);
        if (!seen.add(state)) return false;

        // 尝试把 values[index] 放进第 bucket 组
        for (int bucket = 0; bucket < buckets.length; bucket++) {

            // 剪枝：如果和前一个桶的和相同，放哪个都一样，跳过避免重复
            if (bucket > 0 && buckets[bucket] == buckets[bucket - 1]) continue;

            // 剪枝：放进去超过 target，跳过
            if (buckets[bucket] + values[index] > target) continue;

            // 放入
            buckets[bucket] += values[index];

            // 递归处理下一个数，成功就直接返回
            if (mvpVisit(values, index + 1, buckets, target, seen)) return true;

            // 回溯：拿出来
            buckets[bucket] -= values[index];
        }

        // 所有放法都失败
        return false;
    }

    /**
         values = [4, 3, 2, 1]，groups = 2，total = 10，target = 5
         index=0, 放 4:
         桶0: [4, 0]
         index=1, 放 3:
         桶0: [7, 0] 超过 5，跳过
         桶1: [4, 3]
         index=2, 放 2:
         桶0: [6, 3] 超过 5，跳过
         桶1: [4, 5]
         index=3, 放 1:
         桶0: [5, 5] → index=4 检查 → 两个桶都等于 5 → 成功！
         * @return
     */
    private boolean mvpVisit2(long[] values, int index, long[] buckets,
                             long target, java.util.Set<String> seen) {
        if (index == values.length) {
            for(int i = 0; i < buckets.length; i++) {
                if (buckets[i] != target) {
                    return false;
                }
            }
            return true;
        }

        String state = Arrays.toString(buckets);
        if (!seen.add(state)) {
            return false;
        }

        for(int i = 0; i < buckets.length; i++) {

            if (buckets[i] + values[index] > target) continue;

            if (i > 0 && buckets[i] == buckets[i - 1]) continue;

            buckets[i] += values[index];
            if (mvpVisit2(values,index+1,buckets,target - values[index],seen)) return true;
            buckets[i] -= values[index];
        }


        return false;

    }

    /**
     * 判断能否把 values 分成 groups 组，每组和相等
     *
     * @param values 降序排序后的点数
     * @param groups 组数
     * @param total  所有点数之和
     * @return 能否等分
     */
    private boolean mvpCanPartition(long[] values, int groups, long total) {

        // 总和不能被 groups 整除，无法等分
        if (total % groups != 0L) return false;

        long target = total / groups;  // 每组的目标和

        // 最大的数已经超过 target，不可能分进去
        if (values[0] > target) return false;

        // 如果有些数恰好等于 target，它们各自单独成组
        int begin = 0;
        while (begin < values.length && values[begin] == target) {
            groups--;   // 消耗一个组
            begin++;    // 跳过这个数
        }

        // 所有组都被单独的数填满，直接成功
        if (groups == 0) return true;

        // 对剩下的数回溯分配
        return mvpVisit(values, begin, new long[groups], target, new java.util.HashSet<>());
    }

    /**
     * 求最小的「每组得分」，使得所有点能分成若干组且每组和相等
     * 组数越多，每组和越小，所以从最大组数往下找
     */
    long minimumMvpScore(long[] points) {
        if (points.length == 0) return 0L;

        // 1. 降序排序
        Long[] boxed = new Long[points.length];
        long total = 0L;
        for (int index = 0; index < points.length; index++) {
            boxed[index] = points[index];
            total += points[index];
        }
        java.util.Arrays.sort(boxed, java.util.Collections.reverseOrder());

        long[] values = new long[points.length];
        for (int index = 0; index < values.length; index++)
            values[index] = boxed[index];

        // 2. 从最大组数（每个数一组）开始，逐渐减少组数
        //    组数越多，每组和 total/groups 越小
        //    找到第一个能等分的组数，返回 total/groups
        int groups = values.length;
        while (groups != 1 && !mvpCanPartition(values, groups, total))
            groups--;

        return total / groups;
    }
}
