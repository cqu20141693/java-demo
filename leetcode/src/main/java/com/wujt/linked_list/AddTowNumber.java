package com.wujt.linked_list;

/**
 * @author wujt
 */
public class AddTowNumber {

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode listNode = new ListNode();

        int inc = 0;
        ListNode next = listNode;
        while (l1 != null) {
            if (l2 != null) {
                int val = l1.val + l2.val + inc;
                inc = 0;
                if (val > 9) {
                    inc = 1;
                    val = val - 10;
                }
                next.next = new ListNode(val);
                l2 = l2.next;
            } else {
                int val = l1.val + inc;
                inc = 0;
                if (val > 9) {
                    inc = 1;
                    val = val - 10;
                }
                next.next = new ListNode(val);

            }
            l1 = l1.next;
            next = next.next;
        }

        while (l2 != null) {
            int val = l2.val + inc;
            inc = 0;
            if (val > 9) {
                inc = 1;
                val = val - 10;
            }
            next.next = new ListNode(val);
            l2 = l2.next;
            next = next.next;
        }
        if (inc == 1) {
            next.next = new ListNode(1);
        }
        return listNode.next;
    }

    public static void main(String[] args) {
        ListNode l1 = new ListNode(2);
        l1.next = new ListNode(4);
        l1.next.next = new ListNode(3);
        ListNode l2 = new ListNode(5);
        l2.next = new ListNode(6);
        l2.next.next = new ListNode(4);
        addTwoNumbers(l1, l2);
    }
}
