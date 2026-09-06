package OD.基础与模拟;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-06 15:38
 */
public class 连续牌和整除 {

    static boolean hasDivisibleSubarray(long target, long[] cards) {
        // divisor: 除数，取绝对值确保正数（处理target为负数的情况）
        // 注意：如果target=0，后面需要特殊处理，因为不能对0取模
        long divisor = Math.abs(target);

        // prefix: 前缀和，累加遍历过的所有元素
        long prefix = 0L;

        // seen: 记录所有出现过的"前缀和除以divisor的余数"
        // 如果某个余数重复出现，说明这两个位置之间的子数组和能被divisor整除
        java.util.Set<Long> seen = new java.util.HashSet<>();

        // 初始状态：空数组的前缀和为0，余数为0
        // 这个很重要！为了处理从数组开头就满足条件的子数组
        // 例如：cards=[3,1,2], target=3，前缀和为3时余数为0，看到重复的0就知道[3]满足条件
        seen.add(0L);

        for (long card : cards) {
            // 累加当前元素，更新前缀和
            prefix += card;

            // 计算当前前缀和除以divisor的余数
            // 使用 ((prefix % divisor) + divisor) % divisor 确保余数为非负数
            // Java中负数取模会得到负数，所以需要这个处理
            // 例如：-1 % 3 = -1，但我们需要的是 2
            long remainder = divisor == 0L ? prefix : ((prefix % divisor) + divisor) % divisor;

            // 尝试将当前余数加入集合
            // 如果余数已经存在（add返回false），说明之前某个位置的前缀和与当前前缀和余数相同
            // 根据同余定理：如果两个前缀和除以divisor的余数相同，则它们之间的子数组和能被divisor整除
            // 因此直接返回true
            if (!seen.add(remainder))
                return true;
        }

        // 遍历完所有元素都没有找到满足条件的子数组
        return false;
    }

    public static void main(String[] args) {

        System.out.println(hasDivisibleSubarray(7,new long[]{2,12,6,3,5,5}));

    }

}
