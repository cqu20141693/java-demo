package com.wujt.linked_list;

/**
 * @author wujt
 */
public class PartitionList {
    public ListNode partition(ListNode head, int x) {
        // 首先想到的思路是变量链表，然后用两个链表将分区的节点进行连接

        ListNode lessNode = new ListNode(-1), gteNode = new ListNode(-1), curr = head, tail = lessNode, getTail = gteNode;

        while (curr != null) {
            if (curr.val < x) {
                tail.next = new ListNode(curr.val);
                tail = tail.next;

            } else {
                getTail.next = new ListNode(curr.val);
                getTail = getTail.next;
            }
            curr = curr.next;
        }
        if (tail == lessNode) {
            return head;
        } else {
            tail.next = gteNode.next;
            return lessNode.next;
        }
    }
}
