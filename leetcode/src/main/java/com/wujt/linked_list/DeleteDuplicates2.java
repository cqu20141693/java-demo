package com.wujt.linked_list;

/**
 * @author wujt
 */
public class DeleteDuplicates2 {
    /**
     * 官方处理
     * 直接比较移动，保留一个重复节点时，我们可以拿第一个节点当做preNode，这样就不需要进行记录和循环比较，而是每次比较后进行移动
     *
     * @param head
     * @return
     */
    public ListNode deleteDuplicates2(ListNode head) {
        ListNode current = head;
        while (current != null && current.next != null) {
            if (current.next.val == current.val) {
                current.next = current.next.next;
            } else {
                current = current.next;
            }
        }
        return head;
    }

    public ListNode deleteDuplicates(ListNode head) {
        // 思路: 遍历链表，记录需要比较的值，将当前节点的值和需要比较的值进行对比
        //相同则过滤，不相同则保留节点
        if (head == null) {
            return null;
        }
        ListNode preHead = head, curr = head.next;
        int currValue = preHead.val;
        while (curr != null) {
            if (currValue == curr.val) {
                ListNode tmp = curr.next;
                // 继续向下比较
                while (tmp != null && tmp.val == currValue) {
                    tmp = tmp.next;
                }
                if (tmp == null) {
                    preHead.next = null;
                    return head;
                }
                preHead.next = tmp;
                currValue = tmp.val;
                preHead = tmp;
                curr = tmp.next;
            } else {
                preHead.next = curr;
                currValue = curr.val;
                preHead = curr;
                curr = curr.next;
            }
        }
        return head;
    }
}
