package 力扣.链表.A92;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-25 15:15
 */
public class 反转链表 {

}

class Solution {
    //迭代
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }


}
//递归
class Solution2 {
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode newHead = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }
}

//自己再写一遍
class Solution3 {
    public ListNode reverseList(ListNode head) {
/*        //迭代
        ListNode pre = null;
        ListNode cur = head;
        while (cur.next !=null){
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;*/


        //递归
        if (head == null || head.next == null){
            return head;
        }

        ListNode pre = reverseList(head.next);
        head.next.next = pre;
        head.next = null;
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