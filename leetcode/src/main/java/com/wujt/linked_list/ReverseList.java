package com.wujt.linked_list;

/**
 * @author wujt
 */
public class ReverseList {
    static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public static void main(String[] args) {
        ListNode node1 = new ListNode(1);

        ListNode node2 = new ListNode(2);
        node1.next = node2;
        ListNode node3 = new ListNode(3);
        node2.next = node3;
        ListNode node4 = new ListNode(4);
        node3.next = node4;
        ListNode listNode = reversList(node1);
        System.out.println("success");
    }

    public static ListNode reversList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode preHead = new ListNode();
        preHead.next = head;
        ListNode curr = head, next = head.next;
        head.next = null;
        while (next != null) {

            ListNode tmp = next.next;
            // 重新设置新头为next
            preHead.next = next;

            //将新头的next指向head
            next.next = curr;
            // 重置当前头
            curr = next;

            // 记录下一次next
            next = tmp;
        }
        return preHead.next;
    }
}
