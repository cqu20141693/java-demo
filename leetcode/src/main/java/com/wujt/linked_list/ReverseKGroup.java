package com.wujt.linked_list;

/**
 * @author wujt
 */
public class ReverseKGroup {


    public static void main(String[] args) {

        ListNode listNode = getListNode(2);
        listNode = getListNode(3);

        System.out.println("success");
    }

    private static ListNode getListNode(int k) {
        ListNode listNode = new ListNode(1);
        ListNode next = new ListNode(2);
        listNode.next = next;
        ListNode next1 = new ListNode(3);
        next.next = next1;
        ListNode next2 = new ListNode(4);
        next1.next = next2;
        next2.next = new ListNode(5);
        ListNode reverseKGroup = reverseKGroup(listNode, k);
        return reverseKGroup;
    }

    /**
     * 1 2 3 4
     *
     * @param head
     * @param k
     * @return
     */
    public static ListNode reverseKGroup(ListNode head, int k) {

        if (k == 1) {
            return head;
        }
        // 首先想到的是分组，不清楚head的长度，只能是找到第K个位置，存在则进行链表倒置
        // k个节点倒置，需要记录first节点,first节点next需要单独处理，
        //然后需要记录pre节点和next节点进行倒置，并循环到kNode
        ListNode kNode = head;
        for (int i = 0; i < k - 1; i++) {
            if (kNode != null) {
                kNode = kNode.next;
            }
        }
        if (kNode == null) {
            return head;
        }
        ListNode nextHead = kNode.next;
        ListNode pre = head, next = pre.next;
        while (next != kNode) {
            // 记录下一次的next
            ListNode temp = next.next;
            next.next = pre;
            // 将本次next 设置为pre
            pre = next;
            next = temp;
        }
        kNode.next = pre;
        head.next = reverseKGroup(nextHead, k);
        return kNode;
    }
}
