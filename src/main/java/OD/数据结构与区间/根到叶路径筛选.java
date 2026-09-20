package OD.数据结构与区间;


/**
 * 考点：栈
 *
 * @author faming.yang@hand-china.com 2026-09-12 22:31
 */
public class 根到叶路径筛选 {


    public class Solution {

        /**
         * 分析"蜘蛛路径"。
         * <p>
         * 规则（据代码推断）：
         * 1. 从根节点出发，沿树向下走到叶子，形成一条"根到叶"的路径。
         * 2. 路径上【连续】的负值节点数不能达到 2：
         * - 遇到负值节点，连续负数计数 +1
         * - 遇到非负节点，连续负数计数清零
         * - 若连续负数计数 >= 2，该路径作废（剪枝）
         * 3. 统计所有"合法路径"（满足规则 2 且走到叶子）的：
         * - maximum     : 合法路径中节点值之和的最大值
         * - legalCount  : 合法路径的条数
         * - 是否达标    : maximum >= threshold 且存在合法路径 → 1，否则 0
         *
         * @param root      二叉树根节点
         * @param threshold 阈值
         * @return 长度为 3 的数组 {maximum, 是否达标, legalCount}
         */
        public int[] analyzeSpiderPaths(TreeNode root, int threshold) {

            // ---------- 空树 ----------
            if (root == null)
                return new int[]{Integer.MIN_VALUE, 0, 0};

            int maximum = Integer.MIN_VALUE;   // 合法路径的最大和
            int legalCount = 0;                // 合法路径条数

            // 用栈模拟 DFS（迭代式，避免递归栈溢出）
            // State 保存：当前节点、到当前节点为止的路径和、连续负数个数
            java.util.ArrayDeque<State> stack = new java.util.ArrayDeque<>();
            stack.push(new State(root, 0, 0));

            while (!stack.isEmpty()) {
                State state = stack.pop();

                // ---------- 1. 更新"连续负数"计数 ----------
                //   当前节点是负值 → 连续负数 +1
                //   否则           → 连续负数清零
                int negatives = state.node.val < 0 ? state.negatives + 1 : 0;

                // ---------- 2. 剪枝：连续负数 >= 2，路径作废 ----------
                if (negatives >= 2)
                    continue;

                // ---------- 3. 累加路径和 ----------
                int sum = state.sum + state.node.val;

                // ---------- 4. 到达叶子 → 结算 ----------
                if (state.node.left == null && state.node.right == null) {
                    maximum = Math.max(maximum, sum);
                    legalCount++;
                } else {
                    // ---------- 5. 非叶子 → 继续向下扩展 ----------
                    // 先压右孩子，再压左孩子
                    // 由于栈是后进先出，这样会让"左孩子先被处理"（先序遍历顺序）
                    if (state.node.right != null)
                        stack.push(new State(state.node.right, sum, negatives));
                    if (state.node.left != null)
                        stack.push(new State(state.node.left, sum, negatives));
                }
            }

            // ---------- 6. 组装结果 ----------
            return new int[]{
                    maximum,                                              // 最大路径和
                    legalCount > 0 && maximum >= threshold ? 1 : 0,       // 是否达标
                    legalCount                                            // 合法路径数
            };
        }

        /**
         * DFS 栈中的状态：记录"当前节点 + 到该节点为止的路径和 + 连续负数个数"。
         */
        private class State {
            final TreeNode node;       // 当前节点
            final int sum;             // 从根到当前节点（含）的路径和
            final int negatives;       // 到当前节点为止的【连续】负数个数

            State(TreeNode node, int sum, int negatives) {
                this.node = node;
                this.sum = sum;
                this.negatives = negatives;
            }
        }

        class TreeNode {
            int val;
            TreeNode left;
            TreeNode right;

            TreeNode() {
            }

            TreeNode(int val) {
                this.val = val;
            }

            TreeNode(int val, TreeNode left, TreeNode right) {
                this.val = val;
                this.left = left;
                this.right = right;
            }
        }
    }

}
