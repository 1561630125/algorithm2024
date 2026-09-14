package OD.树与图;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-14 14:51
 */
public class 传话时长 {
    class Solution {
        /**
         * 计算「完成整棵二叉树所有任务」所需的最短时间（关键路径 / 最长路径）。
         *
         * tree 用数组表示的完全二叉树：
         *   - 下标 i 的节点值 tree[i] 表示该任务耗时；
         *   - tree[i] == -1 表示该位置没有节点；
         *   - 下标 0 为根；
         *   - 下标 i 的左孩子 = 2*i+1，右孩子 = 2*i+2。
         *
         * 返回：从根出发到某个叶子路径上耗时之和的最大值，
         *       即所有任务串行完成所需的最短总时间（关键路径长度）。
         */
        long whisperCompletionTime(long[] tree) {
            // 1. 空树或根不存在 → 耗时 0
            if (tree.length == 0 || tree[0] == -1L) return 0L;

            // elapsed[i]：从根到节点 i 的路径耗时累计值
            long[] elapsed = new long[tree.length];

            // reachable[i]：节点 i 是否可达（即是否真实存在且能从上往下走到）
            boolean[] reachable = new boolean[tree.length];

            // 2. 根节点初始化
            elapsed[0] = tree[0];
            reachable[0] = true;

            // result：记录所有可达节点中最大的累计耗时（即关键路径）
            long result = 0L;

            // 3. 自顶向下遍历（按下标顺序，天然父先于子）
            for (int index = 0; index < tree.length; index++) {
                if (!reachable[index]) continue;   // 不可达节点跳过

                // 更新全局最大累计耗时
                result = Math.max(result, elapsed[index]);

                // 4. 处理左右孩子
                for (int child : new int[] { index * 2 + 1, index * 2 + 2 }) {
                    if (child < tree.length && tree[child] != -1L) {
                        reachable[child] = true;                        // 标记可达
                        elapsed[child] = elapsed[index] + tree[child];  // 累加耗时
                    }
                }
            }

            return result;
        }
    }
}
