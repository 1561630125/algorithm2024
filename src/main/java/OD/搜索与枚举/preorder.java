package OD.搜索与枚举;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2026-09-11 17:47
 */
public class preorder {

/*

    void preorder(TreeNode root) {
        if (root == null) return;
        System.out.print(root.val + " ");  // 根
        preorder(root.left);               // 左
        preorder(root.right);              // 右
    }

    void preorder(TreeNode root) {
        if (root == null) return;
        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            System.out.print(node.val + " ");   // 访问根

            // 先压右，再压左，保证左先出栈
            if (node.right != null) stack.push(node.right);
            if (node.left != null) stack.push(node.left);
        }
    }

    // 利用叶子节点的空右指针指向后继，不用栈，空间 O(1)。
    void preorder(TreeNode root) {
        TreeNode cur = root;
        while (cur != null) {
            if (cur.left == null) {
                System.out.print(cur.val + " ");  // 访问
                cur = cur.right;
            } else {
                // 找左子树的最右节点（中序前驱）
                TreeNode pred = cur.left;
                while (pred.right != null && pred.right != cur)
                    pred = pred.right;

                if (pred.right == null) {
                    System.out.print(cur.val + " ");  // 第一次到 cur，访问
                    pred.right = cur;                 // 建线索
                    cur = cur.left;
                } else {
                    pred.right = null;                // 拆线索
                    cur = cur.right;
                }
            }
        }
    }

    // 先序
    visit(root); preorder(root.left); preorder(root.right);

    // 中序
    inorder(root.left); visit(root); inorder(root.right);

    // 后序
    postorder(root.left); postorder(root.right); visit(root);
*/

}
