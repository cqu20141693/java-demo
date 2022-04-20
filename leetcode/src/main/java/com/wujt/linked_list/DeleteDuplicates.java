package com.wujt.linked_list;

/**
 * @author wujt
 */
public class DeleteDuplicates {
    public ListNode deleteDuplicates(ListNode head) {
        // 思路： 遍历链表，将curr curr.next 值进行比较
        // 不相同则可以将curr加入结果链表，继续下一轮循环
        // 相同则继续进行下一个值比较，直到值不相同，
        ListNode newHead = new ListNode(-1);
        newHead.next = head;
        ListNode preNode = newHead, curr = head;

        while (curr != null) {
            if (curr.next != null) {
                //preNode 不移动
                if (curr.val == curr.next.val) {
                    ListNode tmp = curr.next.next;

                    while (tmp != null && tmp.val == curr.val) {
                        tmp = tmp.next;
                    }
                    preNode.next = tmp;
                    curr = tmp;
                } else {
                    preNode.next = curr;
                    preNode = curr;
                    curr = curr.next;

                }

            } else {
                preNode.next = curr;
                curr = curr.next;
            }
        }
        return newHead.next;
    }
}
