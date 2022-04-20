package com.wujt.linked_list;

/**
 * @author wujt
 */
public class ListTest {
    public static void main(String[] args) {

        testDeleteDuplicates();

        testDeleteDuplicates2();
        int[] ints = { 1,4,3,2,5,2};
        ListNode preHead = new ListNode(-1), curr = preHead;
        for (int i : ints) {
            curr.next = new ListNode(i);
            curr = curr.next;
        }
        PartitionList partitionList = new PartitionList();
        ListNode partition = partitionList.partition(preHead.next, 3);
    }

    private static void testDeleteDuplicates2() {
        int[] ints = { 1,1,2,3,3};
        ListNode preHead = new ListNode(-1), curr = preHead;
        for (int i : ints) {
            curr.next = new ListNode(i);
            curr = curr.next;
        }

        DeleteDuplicates2 deleteDuplicates = new DeleteDuplicates2();
        ListNode listNode = deleteDuplicates.deleteDuplicates(preHead.next);
    }

    private static void testDeleteDuplicates() {
        int[] ints = {1, 2, 3, 3, 4, 4, 5};
        ListNode preHead = new ListNode(-1), curr = preHead;
        for (int i : ints) {
            curr.next = new ListNode(i);
            curr = curr.next;
        }

        DeleteDuplicates deleteDuplicates = new DeleteDuplicates();
        deleteDuplicates.deleteDuplicates(preHead.next);
    }
}
