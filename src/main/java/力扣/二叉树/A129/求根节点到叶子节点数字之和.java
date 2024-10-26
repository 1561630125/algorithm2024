package 力扣.二叉树.A129;

import java.util.LinkedList;
import java.util.Queue;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-26 18:57
 */
public class 求根节点到叶子节点数字之和 {

}

//DFS
class Solution {
    public int sumNumbers(TreeNode root) {
        return dfs(root, 0);
    }

    public int dfs(TreeNode root, int prevSum) {
        if (root == null) {
            return 0;
        }
        int sum = prevSum * 10 + root.val;
        if (root.left == null && root.right == null) {
            return sum;
        } else {
            return dfs(root.left, sum) + dfs(root.right, sum);
        }
    }
}
//BFS
class Solution2 {
    public int sumNumbers(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int sum = 0;
        Queue<TreeNode> nodeQueue = new LinkedList<TreeNode>();
        Queue<Integer> numQueue = new LinkedList<Integer>();
        nodeQueue.offer(root);
        numQueue.offer(root.val);
        while (!nodeQueue.isEmpty()) {
            TreeNode node = nodeQueue.poll();
            int num = numQueue.poll();
            TreeNode left = node.left, right = node.right;
            if (left == null && right == null) {
                sum += num;
            } else {
                if (left != null) {
                    nodeQueue.offer(left);
                    numQueue.offer(num * 10 + left.val);
                }
                if (right != null) {
                    nodeQueue.offer(right);
                    numQueue.offer(num * 10 + right.val);
                }
            }
        }
        return sum;
    }
}


class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}