package com.wujt.linked_list;

/**
 * @author wujt
 */
public class SwapPairs {

    /**
     * 1 2 3 4
     *
     * @param head
     * @return
     */
    public ListNode swapPairs(ListNode head) {
        // 首先想到的是建立一个临时节点，记录当前pair的第二个节点，通过判断其是否为空结束转换
        // 看官方题解后，该问题确实可以使用递归的方式解决
        ListNode pre = new ListNode(-1);
        pre.next = head;
        ListNode curr = pre, first = curr.next;
        while (first != null) {
            ListNode second = first.next;
            if (second == null) {
                break;
            } else {
                // 交换
                curr.next = second;
                first.next = second.next;
                second.next = first;

                // 初始next
                curr = first;
                first = first.next;
            }
        }

        return pre.next;
    }



}
