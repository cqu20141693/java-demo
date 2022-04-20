package com.wujt.linked_list;

import java.util.PriorityQueue;

/**
 * @author wujt
 */
public class MergeKLists {

    public ListNode mergeKLists(ListNode[] lists) {
        // 首先能想到的方式是两个先merge,再将merge和next merge,
        // 看了官方题解，其中优先队列的方式和其中一个思路相近，就是记录下每个链表的最小head,
        // 个人逐个再比较，但是比较复杂，但是利用优先队列确实只需要插入的时间复杂度想法是自己
        ListNode merge = null;

        for (ListNode list : lists) {
            merge = mergeList(merge, list);
        }
        return merge;
    }

    private ListNode mergeList(ListNode merge, ListNode list) {

        ListNode head = new ListNode(-1), pre = head;
        while (merge != null && list != null) {
            if (merge.val > list.val) {
                pre.next = new ListNode(list.val);
                list = list.next;
            } else {
                pre.next = new ListNode(merge.val);
                merge = merge.next;
            }
            pre = pre.next;
        }
        pre.next = merge == null ? list : merge;
        return head.next;

    }

    public ListNode mergeKLists2(ListNode[] lists) {
        PriorityQueue<ListNode> q = new PriorityQueue<>((x, y)->x.val-y.val);
        for(ListNode node : lists){
            if(node!=null){
                q.add(node);
            }
        }
        ListNode head = new ListNode(0);
        ListNode tail = head;
        while(!q.isEmpty()){
            tail.next = q.poll();
            tail = tail.next;
            if (tail.next != null){
                q.add(tail.next);
            }
        }
        return head.next;
    }
}
