package com.wujt.linked_list;

/**
 * @author wujt
 */
public class RotateRight {
    public ListNode rotateRight(ListNode head, int k) {

        // 思路： 需要向右移动k个节点，此时k-1位的next 为null，故需要找到该位置并设置，
        // 而后需要将截断的k位与前n-k位相结合，需要找到tail和head;
        // 需要考虑移动的位数后head位置并没有变化的情况

        if (head == null) {
            return null;
        }
        if (k == 0) {
            return head;
        }
        ListNode curr = head, kNode = head;
        // 移动k位,如果过程中已经移动到了链表尾部，可以知道size,从而计算下次循环的次数
        for (int i = 0; i < k; i++) {
            if (curr.next == null) {
                curr = head;
            } else {
                curr = curr.next;
            }
        }
        while (curr.next != null) {
            curr = curr.next;
            kNode = kNode.next;
        }
        // 移动的位数为size 的整数倍
        if (kNode.next == null) {
            return head;
        }
        ListNode newHead = kNode.next, tail = curr;
        tail.next = head;
        kNode.next = null;
        return newHead;
    }
}
