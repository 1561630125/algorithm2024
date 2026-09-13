package OD.树与图;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-13 18:22
 */
public class 前序遍历 {
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
    
    
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        preorder(root, result);
        return result;
    }

    void preorder(TreeNode root, java.util.List<Integer> result) {
        if (root == null) return;
        result.add(root.val);          // 根
        preorder(root.left, result);   // 左
        preorder(root.right, result);  // 右
    }


    public List<Integer> preorderTraversal2(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.add(node.val);           // 访问根

            // 关键：先压右，再压左！
            // 因为栈是后进先出，压左后弹左，保证"左先于右"
            if (node.right != null) stack.push(node.right);
            if (node.left != null)  stack.push(node.left);
        }
        return result;
    }

}
