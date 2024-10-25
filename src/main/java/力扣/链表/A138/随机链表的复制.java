package 力扣.链表.A138;

import java.util.HashMap;
import java.util.Map;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-25 9:09
 */
public class 随机链表的复制 {

}

//回溯+哈希表
class Solution {
    //因为随机指针的存在，当我们拷贝节点时，「当前节点的随机指针指向的节点」可能还没创建
    Map<Node, Node> cachedNode = new HashMap<Node, Node>();

    public Node copyRandomList(Node head) {
        if (head == null) {
            return null;
        }
        if (!cachedNode.containsKey(head)) {//防止重复拷贝
            Node headNew = new Node(head.val);
            cachedNode.put(head, headNew);
            headNew.next = copyRandomList(head.next);
            headNew.random = copyRandomList(head.random);
        }
        return cachedNode.get(head);
    }
}

//迭代+节点拆分
class Solution2 {
    public Node copyRandomList(Node head) {
        if (head == null) {
            return null;
        }
        for (Node node = head; node != null; node = node.next.next) {
            Node nodeNew = new Node(node.val);
            nodeNew.next = node.next;
            node.next = nodeNew;
        }
        //每一个拷贝节点 S′的随机指针应当指向的节点，即为其原节点 S 的随机指针指向的节点T的后继节点 T′
        for (Node node = head; node != null; node = node.next.next) {
            Node nodeNew = node.next;
            nodeNew.random = (node.random != null) ? node.random.next : null;
        }
        Node headNew = head.next;
        for (Node node = head; node != null; node = node.next) {
            Node nodeNew = node.next;
            node.next = node.next.next;
            nodeNew.next = (nodeNew.next != null) ? nodeNew.next.next : null;
        }
        return headNew;
    }
}



class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}