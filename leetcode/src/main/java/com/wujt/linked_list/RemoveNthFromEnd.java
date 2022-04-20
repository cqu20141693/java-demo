package com.wujt.linked_list;

/**
 * @author wujt
 */
public class RemoveNthFromEnd {

    class Solution {
        /**
         * 链表中结点的数目为 sz
         * 1 <= sz <= 30
         * 0 <= Node.val <= 100
         * 1 <= n <= sz
         *
         * @param head
         * @param n
         * @return
         */
        public ListNode removeNthFromEnd(ListNode head, int n) {
            //第一直觉是遍历一遍head得到size,然后再遍历到倒数n+1的位置，剔除第n个，返回head
            // 第二种想法是一个指针先走n步，然后第二个和第一个指针再一起走，这样当第一个指针到尾部的时候，这时第二个指针再第一个指针的后n位

            //  官方思路三： 使用栈，先遍历入栈，出栈时到第n位后直接剔除，然后返回头结点
            // 先走一步
            ListNode first = head, second = null;
            //再走n-1步
            for (int i = 0; i < n - 1; i++) {
                first = first.next;
            }
            // 第二个指针到达倒数第n+1的位置
            while (first.next != null) {
                first = first.next;
                if (second == null) {
                    second = head;
                } else {
                    second = second.next;
                }
            }
            // 开始清理倒数n位节点
            if (second == null) {
                return head.next;
            }
            ListNode next = second.next;
            second.next = next.next;

            return head;

        }
    }
}
