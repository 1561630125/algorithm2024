package 力扣.二叉树.A117;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-26 17:57
 */
public class 填充每个节点的下一个右侧指针节点2 {

    public static void main(String[] args) {
        Node node = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        node.left = node2;
        node.right = node3;

        Solution3 solution = new Solution3();
        solution.connect(node);
    }
}

//层次遍历，较好理解
class Solution {
    public Node connect(Node root) {
        if (root == null) {
            return null;
        }
        Queue<Node> queue = new ArrayDeque<Node>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int n = queue.size();
            Node pre = null;
            for (int i = 1; i <= n; ++i) {
                Node cur = queue.poll();
                if (cur.left != null) {
                    queue.offer(cur.left);
                }
                if (cur.right != null) {
                    queue.offer(cur.right);
                }
                if (i != 1) {
                    pre.next = cur;
                }
                pre = cur;
            }
        }
        return root;
    }
}

//使用已建立的 next 指针
// 如果第 i 层节点之间已经建立 next 指针，就可以通过 next 指针访问该层的所有节点，同时对于每个第 i 层的节点，
// 我们又可以通过它的 left 和 right 指针知道其第 i+1 层的孩子节点是什么，所以遍历过程中就能够按顺序为第 i+1 层节点建立 next 指针

class Solution2 {
    Node last = null, nextStart = null;

    public Node connect(Node root) {
        if (root == null) {
            return null;
        }
        Node start = root;
        while (start != null) {
            last = null;
            nextStart = null;
            for (Node p = start; p != null; p = p.next) {
                if (p.left != null) {
                    handle(p.left);
                }
                if (p.right != null) {
                    handle(p.right);
                }
            }
            start = nextStart;
        }
        return root;
    }

    public void handle(Node p) {
        if (last != null) {
            last.next = p;
        }
        if (nextStart == null) {
            nextStart = p;
        }
        last = p;
    }
}
//chatgpt 给出的答案比官解Solution2好理解多了
class Solution3 {
    public Node connect(Node root) {
        if (root == null) {
            return null;
        }

        Node head = root; // 当前层的头节点

        // 循环遍历每一层，从上至下
        while (head != null) {
            Node dummy = new Node(0); //下一层的虚拟头节点
            Node temp = dummy; //当前处理的节点

            // 遍历当前层，连接下一层的节点
            for (Node cur = head; cur != null; cur = cur.next) {
                if (cur.left != null) {
                    temp.next = cur.left;
                    temp = temp.next; //移动temp
                }
                if (cur.right != null) {
                    temp.next = cur.right;
                    temp = temp.next; //移动temp
                }
            }
            // 移动到下一层的实际头节点处
            head = dummy.next;
        }

        return root;
    }
}



class Node {
    public int val;
    public Node left;
    public Node right;
    public Node next;

    public Node() {}

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, Node _left, Node _right, Node _next) {
        val = _val;
        left = _left;
        right = _right;
        next = _next;
    }
};