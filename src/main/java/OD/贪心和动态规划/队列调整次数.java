package OD.贪心和动态规划;

/**
 * 考点：贪心
 *
 * @author faming.yang@hand-china.com 2026-09-03 17:45
 */
public class 队列调整次数 {

    class Solution {
        /**
         * 计算需要"重新排序"的最小次数。
         * <p>
         * 题意理解：
         * - commands 是一串双端队列操作，分三类：
         * "tail..."  ：从尾部插入元素（不会破坏有序性）
         * "remove"   ：从头部删除元素（要求队列有序才能正确删除）
         * 其他       ：从头部插入元素（会破坏有序性）
         * - 队列内部要求始终"有序"，一旦被头部插入破坏，就需要一次"重排序"。
         * - 目标：求出最少需要重排序的次数。
         * <p>
         * 核心贪心思路：
         * - 只有当"队列非空且从头部插入"时才可能破坏有序性；
         * - 一旦破坏，用 ordered = false 打标记；
         * - 在真正执行 remove（需要有序）之前，如果标记为 false，
         * 就必须补一次重排序（result++），并恢复有序。
         */
        int minDequeReorders(String[] commands) {
            int size = 0;          // 当前队列中的元素个数
            int result = 0;        // 最少重排序次数
            boolean ordered = true; // 当前队列是否处于"有序"状态

            for (String raw : commands) {
                String command = raw.trim();   // 去掉首尾空白，便于比较

                if (command.equals("remove")) {
                    // ---- 删除操作（从头部移除），要求队列有序 ----
                    if (!ordered)
                        result++;              // 执行前若已失序，必须先重排序一次
                    ordered = true;            // 重排序后（或本来就有序）恢复为有序
                    size--;                    // 元素数量减一

                } else if (command.startsWith("tail")) {
                    // ---- 尾部插入：不会破坏有序性 ----
                    size++;                    // 元素数量加一

                } else {
                    // ---- 头部插入：只有在队列非空时才会破坏有序性 ----
                    if (size > 0)
                        ordered = false;       // 队列非空，头部插入导致失序
                    size++;                    // 元素数量加一
                }
            }

            return result;  // 返回最少重排序次数
        }
    }

}
