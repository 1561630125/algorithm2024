package OD.数据结构与区间;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-12 13:44
 */
public class 直角三角配组 {

    class Solution {
        // 当前组所有合法的三角形，每个用位掩码表示（哪几条边参与）
        java.util.List<Integer> batch64Triples;
        // 当前组的边数
        int batch64SideCount;
        // 记忆化数组：batch64Memo[mask] = 从已用边集合 mask 出发，还能选出的最大三角形数
        int[] batch64Memo;

        /**
         * 状压 DFS
         * @param mask 已经被占用的边的集合（二进制位为 1 表示该边已被使用）
         * @return     从当前 mask 出发，还能选出的最大三角形数量
         */
        int batch64TriangleSearch(int mask) {
            // 已经算过，直接返回
            if (batch64Memo[mask] >= 0)
                return batch64Memo[mask];

            // 找到第一个还没被占用的边（最小的未使用下标）
            int first = 0;
            while (first < batch64SideCount && (mask & (1 << first)) != 0)
                first++;

            // 所有边都用完了，无法再选三角形
            if (first == batch64SideCount)
                return batch64Memo[mask] = 0;

            // 选择 1：不选任何包含 first 的三角形，直接跳过这条边
            int best = batch64TriangleSearch(mask | (1 << first));

            // 选择 2：枚举所有包含 first 且与当前 mask 不冲突的三角形，选一个
            for (int triple : batch64Triples)
                if ((triple & (1 << first)) != 0 && (triple & mask) == 0)
                    best = Math.max(best, 1 + batch64TriangleSearch(mask | triple));

            return batch64Memo[mask] = best;
        }

        /**
         * 主函数：对每组边长，求最多能选出多少个互不共边的直角三角形
         * @param groups 每组是一个 long 数组，表示该组的边长
         * @return       每组对应的最大三角形数量
         */
        long[] maximumRightTriangles(long[][] groups) {
            long[] answers = new long[groups.length];

            for (int group = 0; group < groups.length; group++) {
                // 1. 复制边长并平方
                long[] sides = groups[group].clone();
                for (int i = 0; i < sides.length; i++)
                    sides[i] *= sides[i];

                // 2. 排序，方便后面剪枝
                java.util.Arrays.sort(sides);

                // 3. 预处理所有满足 a² + b² = c² 的三元组，用位掩码表示
                batch64Triples = new java.util.ArrayList<>();
                for (int i = 0; i < sides.length; i++)
                    for (int j = i + 1; j < sides.length; j++)
                        for (int k = j + 1; k < sides.length; k++) {
                            if (sides[i] + sides[j] == sides[k])
                                // 找到直角三角形，记录 i/j/k 三条边
                                batch64Triples.add((1 << i) | (1 << j) | (1 << k));
                            else if (sides[i] + sides[j] < sides[k])
                                // 因为已排序，sides[k] 太大，后面的 k 只会更大，直接跳出
                                break;
                        }

                // 4. 初始化状压 DP
                batch64SideCount = sides.length;
                batch64Memo = new int[1 << sides.length];
                java.util.Arrays.fill(batch64Memo, -1);

                // 5. 从空集开始搜索
                answers[group] = batch64TriangleSearch(0);
            }
            return answers;
        }
    }

}
