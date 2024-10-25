package 力扣.链表.A25;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-25 16:07
 */
public class K个一组翻转链表 {

    public static void main(String[] args) {
        ListNode listNode1 = new ListNode(1);
        ListNode listNode2 = new ListNode(2);
        ListNode listNode3 = new ListNode(3);
        ListNode listNode4 = new ListNode(4);
        ListNode listNode5 = new ListNode(5);
        listNode1.next = listNode2;
        listNode2.next = listNode3;
        listNode3.next = listNode4;
        listNode4.next = listNode5;
        listNode5.next = null;


        Solution2 solution = new Solution2();
        ListNode listNode = solution.reverseKGroup(listNode1, 2);
        System.out.println(listNode);
    }
}
class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode hair = new ListNode(0);
        hair.next = head;
        ListNode pre = hair;

        while (head != null) {
            ListNode tail = pre;
            // 查看剩余部分长度是否大于等于 k
            for (int i = 0; i < k; ++i) {
                tail = tail.next;
                if (tail == null) {
                    return hair.next;
                }
            }
            ListNode nex = tail.next;
            ListNode[] reverse = myReverse(head, tail);
            head = reverse[0];
            tail = reverse[1];
            // 把子链表重新接回原链表
            pre.next = head;
            tail.next = nex;
            pre = tail;
            head = tail.next;
        }

        return hair.next;
    }
    //TODO：下次继续再看一遍
    //head:1->2->3->4->5
    //tail:2->3->4->5
    public ListNode[] myReverse(ListNode head, ListNode tail) {
        ListNode pre = tail.next;//pre:3->4->5
        ListNode cur = head;//p:1->2->3->4->5
        while (pre != tail) {
            ListNode next = cur.next;//nex:2->3->4->5
            cur.next = pre;//p.next:3->4->5,当前节点指向尾节点的下一个节点
            pre = cur;//
            cur = next;
        }
        return new ListNode[]{tail, head};
    }

    //head:1->2->3->4->5、tail:2->3->4->5
    //第一轮，pre:1->3->4->5,cur：2->3->4->5
    //第二轮，pre:2->1->3->4->5,cur：>3->4->5




//    public ListNode[] myReverse2(ListNode head, ListNode tail) {
//        ListNode prev = tail.next;
//        ListNode p = head;
//        while (prev != tail) {
//            ListNode nex = p.next;
//            p.next = prev;
//            prev = p;
//            p = nex;
//        }
//        return new ListNode[]{tail, head};
//    }

}

//递归版本，比较好理解
class Solution2 {
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode currentNode = head;
        int nodeCount = 0;
        // 计算链表长度
        while (currentNode != null) {
            nodeCount++;
            currentNode = currentNode.next;
        }
        // 剩余节点不足k个 直接返回头节点
        if (nodeCount < k) {
            return head;
        }

        ListNode pre = head;
        ListNode cur = head.next;
        for (int i = 0;i < k - 1;i++) {
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }

        head.next = reverseKGroup(cur, k);
        return pre;
    }
}

class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}