package 力扣.分治.A147;

/**
 * description
 *
 * @author faming.yang@hand-china.com 2024-10-28 17:21
 */

public class 对链表进行插入排序 {

}


/**
 * <img width="640" height="320" src="https://gitee.com/yfmzzzzzz/myimg/raw/master/myimg/image-20241028173847413-2024-10-2817:38:48.png" alt="">
 */
class Solution {
    public ListNode insertionSortList(ListNode head) {
        if (head == null) {
            return head;
        }
        ListNode dummyHead = new ListNode(0);
        dummyHead.next = head;
        ListNode lastSorted = head, curr = head.next;//维护 lastSorted 为链表的已排序部分的最后一个节点，初始时 lastSorted = head
        while (curr != null) {
            if (lastSorted.val <= curr.val) {
                lastSorted = lastSorted.next;
            } else {
                ListNode prev = dummyHead;
                while (prev.next.val <= curr.val) {
                    prev = prev.next;
                }
                lastSorted.next = curr.next;
                curr.next = prev.next;
                prev.next = curr;
            }
            curr = lastSorted.next;
        }
        return dummyHead.next;
    }
}

class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}