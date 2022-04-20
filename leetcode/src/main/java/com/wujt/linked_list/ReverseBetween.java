package com.wujt.linked_list;

/**
 * @author wujt
 */
public class ReverseBetween {
    public ListNode reverseBetween(ListNode head, int left, int right) {
        if (left == right) {
            return head;
        }
        // 思路； 首先是找到第m个位置，记住m-1，然后将链表进行遍历并反转到n的位置，并记住n+1
        ListNode preHead = new ListNode(-1), curr = preHead;
        preHead.next = head;
        // 移动到m-1
        for (int i = 0; i < left - 1; i++) {
            curr = curr.next;
        }
        ListNode preNNode = curr, nNode = curr.next, pre = nNode, next = pre.next;
        for (int i = left; i < right; i++) {
            // 记录下一次的next
            ListNode temp = next.next;
            next.next = pre;
            // 将本次next 设置为pre
            pre = next;
            next = temp;
        }
        preNNode.next = pre;
        nNode.next = next;
        return preHead.next;
    }
}
