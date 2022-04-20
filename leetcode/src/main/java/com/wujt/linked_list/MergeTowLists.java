package com.wujt.linked_list;

/**
 * @author wujt
 */
public class MergeTowLists {
    /**
     * 合并两个有序链表
     *
     * @param l1
     * @param l2
     * @return
     */
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        // 遍历一个链表，进行相互比较，并记录当前已经比较了的位置，用于下次比较
        // 最简单的方式就是新建一个链表存储每个最小的值
        if (l1 == null) {
            return l2;
        } else if (l2 == null) {
            return l1;
        } else {
            ListNode head = new ListNode(), pre = head;
            while (l1 != null) {
                if (l2 == null || l1.val < l2.val) {
                    pre.next = new ListNode(l1.val);
                    l1 = l1.next;
                } else {
                    pre.next = new ListNode(l2.val);
                    l2 = l2.next;
                }
                pre = pre.next;
            }
            while (l2 != null) {
                pre.next = new ListNode(l2.val);
                l2 = l2.next;
                pre = pre.next;
            }
            return head.next;
        }
    }

    public ListNode mergeTwoLists2(ListNode l1, ListNode l2) {
        // 遍历一个链表，进行相互比较，并记录当前已经比较了的位置，用于下次比较
        // 最简单的方式就是新建一个链表存储每个最小的值
        if (l1 == null) {
            return l2;
        } else if (l2 == null) {
            return l1;
        } else {
            ListNode head = new ListNode(), pre = head;
            while (l1 != null) {
                if (l2 == null || l1.val < l2.val) {
                    pre.next = new ListNode(l1.val);
                    l1 = l1.next;
                } else {
                    pre.next = new ListNode(l2.val);
                    l2 = l2.next;
                }
                pre = pre.next;
            }
            pre.next = l2;
            return head.next;
        }
    }
}
