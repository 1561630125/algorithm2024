package OD.树与图;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * 考点：单调栈
 *
 * @author faming.yang@hand-china.com 2026-09-13 18:04
 */
public class 前序验树 {

    static class Solution {

        /**
         * 判断 preorder 是否为合法 BST 的前序遍历，并返回相关结果。
         *
         * @param preorder 待验证的前序序列
         * @return {1, 最左叶子值, 最右叶子值} 合法；
         * {0, 0, 0} 非法
         */
        int[] umbrellaEffect(int[] preorder) {

            // ---------- 边界：空输入 ----------
            if (preorder == null || preorder.length == 0)
                return new int[]{0, 0, 0};

            /*long lower = Long.MIN_VALUE;   // 当前允许的最小值下界
            int[] stack = new int[preorder.length];
            int size = 0;
            java.util.Set<Integer> seen = new java.util.HashSet<>();

            // ---------- 1. 用单调栈验证 BST 前序合法性 ----------
            for (int value : preorder) {

                // 重复元素 或 小于等于下界 → 不是合法 BST
                if (!seen.add(value) || value <= lower)
                    return new int[]{0, 0, 0};

                // 单调递减栈：遇到更大的值，弹出所有比它小的，更新下界
                while (size > 0 && value > stack[size - 1])
                    lower = stack[--size];

                stack[size++] = value;
            }*/

            Set<Integer> seen = new HashSet<>();
            Stack<Integer> stack = new Stack<>();   // 单调递减栈
            long lower = Long.MIN_VALUE;

            for (int value : preorder) {
                // 重复 或 小于等于下界 → 非法
                if (!seen.add(value) || value <= lower)
                    return new int[]{0, 0, 0};

                // 弹出所有比 value 小的栈顶，更新下界
                while (!stack.isEmpty() && value > stack.peek())
                    lower = stack.pop();

                stack.push(value);
            }


            // ---------- 2. 根据前序序列重建树结构（左右孩子指针） ----------
            int[] left = new int[preorder.length];  // left[i] 和 right[i] 是两个数组，表示节点 i 的左/右孩子是谁：
            int[] right = new int[preorder.length];
            java.util.Arrays.fill(left, -1);
            java.util.Arrays.fill(right, -1);

            for (int index = 1; index < preorder.length; index++) {
                int current = 0;   // 从根开始
                while (true) {
                    // 比当前节点小 → 走左；否则走右
                    int[] branch = preorder[index] < preorder[current] ? left : right;
                    if (branch[current] < 0) {   // 空位，挂上
                        branch[current] = index;
                        break;
                    }
                    current = branch[current];   // 继续往下找
                }
            }

            // ---------- 3. 求最左、最右叶子值 ----------
            return new int[]{
                    1,
                    pendant(preorder, left, right, true),    // 最左叶子
                    pendant(preorder, left, right, false)    // 最右叶子
            };
        }

        /**
         * 求从根出发、按指定偏好一路走到叶子的值。
         *
         * @param preferLeft true 优先走左（求最左叶子）；
         *                   false 优先走右（求最右叶子）
         */
        int pendant(int[] values, int[] left, int[] right, boolean preferLeft) {
            int current = preferLeft ? left[0] : right[0];
            if (current < 0)
                return 0;   // 根没有对应孩子

            // 一路向下，直到叶子（没有孩子）
            while (left[current] >= 0 || right[current] >= 0)
                current = preferLeft
                        ? (left[current] >= 0 ? left[current] : right[current])
                        : (right[current] >= 0 ? right[current] : left[current]);

            return values[current];
        }
    }


    static class Solution2 {
        private int index;

        int[] umbrellaEffect(int[] preorder) {
            if (preorder == null || preorder.length == 0)
                return new int[]{0, 0, 0};

            index = 0;
            Node root = build(preorder, Long.MIN_VALUE, Long.MAX_VALUE);

            // 非法检测：有元素没被消费，或根为 null（空输入已提前返回）
            if (index != preorder.length)
                return new int[]{0, 0, 0};

            return new int[]{1, leftmost(root), rightmost(root)};
        }

        private Node build(int[] preorder, long lower, long upper) {
            if (index >= preorder.length)
                return null;

            int value = preorder[index];
            // 当前值不在 (lower, upper) 内 → 不消费，返回 null
            if (value <= lower || value >= upper)
                return null;

            Node node = new Node(value);
            index++;

            // 只有下一个值能当左孩子才递归
            if (index < preorder.length && preorder[index] < value)
                node.left = build(preorder, lower, value);

            // 只有下一个值能当右孩子才递归
            if (index < preorder.length && preorder[index] > value)
                node.right = build(preorder, value, upper);

            return node;
        }

        private int leftmost(Node n) {
            while (n.left != null || n.right != null)
                n = (n.left != null) ? n.left : n.right;
            return n.value;
        }

        private int rightmost(Node n) {
            while (n.left != null || n.right != null)
                n = (n.right != null) ? n.right : n.left;
            return n.value;
        }

        static class Node {
            int value;
            Node left, right;

            Node(int v) {
                value = v;
            }
        }
    }


    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] preorder = new int[]{50, 25, 10, 30, 75, 60, 90};

        System.out.println(Arrays.toString(solution.umbrellaEffect(preorder)));

        Solution2 solution2 = new Solution2();
        System.out.println(Arrays.toString(solution2.umbrellaEffect(preorder)));
    }


}
