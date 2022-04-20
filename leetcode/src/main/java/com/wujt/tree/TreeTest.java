package com.wujt.tree;

/**
 * @author wujt
 */
public class TreeTest {
    public static void main(String[] args) {
        PreAndInOrder preAndInOrder = new PreAndInOrder();
        int[] pre = {3, 9, 8, 5, 4, 10, 20, 15, 7};
        int[] inOrder = {4, 5, 8, 10, 9, 3, 15, 20, 7};
        TreeNode treeNode = preAndInOrder.buildTree(pre, inOrder);
        TreeNode treeNode2 = preAndInOrder.buildTree2(pre, inOrder);
        System.out.println(treeNode);
    }
}
