package com.wujt.tree;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author wujt
 */
public class PreAndInOrder {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder == null || preorder.length == 0) {
            return null;
        }
        TreeNode root = new TreeNode(preorder[0]);
        Stack<TreeNode> stack = new Stack<>();
        // 将根节点压入栈
        stack.push(root);
        int inorderIndex = 0;
        for (int i = 1; i < preorder.length; i++) {
            // 获取前序遍历的下一个数据，即是左节点
            int preorderVal = preorder[i];
            // 获取获取栈顶数据
            TreeNode node = stack.peek();
            // 首先建立左子树
            // 前序遍历一直到和中序遍历第一个节点相同，表示最左边树建立完成
            if (node.getValue() != inorder[inorderIndex]) {
                // 不相同则设置当前节点的左节点
                node.setLeft(new TreeNode(preorderVal));
                // 并将左节点压入栈
                stack.push(node.getLeft());
            } else {
                // 相同左边树建立完成
                // 开始出栈建立每个节点的右节点
                // 在中序遍历中，如果当前栈顶的值和中序遍历的值相同，直接出栈
                while (!stack.isEmpty() && stack.peek().getValue() == inorder[inorderIndex]) {
                    node = stack.pop();
                    inorderIndex++;
                }
                node.setRight(new TreeNode(preorderVal));
                stack.push(node.getRight());
            }
        }
        return root;
    }

    private Map<Integer, Integer> indexMap;


    public TreeNode doBuildTree(int[] preorder, int[] inorder, int preorder_left, int preorder_right, int inorder_left, int inorder_right) {
        if (preorder_left > preorder_right) {
            return null;
        }

        // 入栈根节点
        // 获取左子树的前序遍历数组和中序遍历数组
        // 获取右子树的前序遍历和中序遍历数组
        // 通过前序遍历获取第一个根节点，可以在中序遍历获取到根节点的位置，
        // 中序遍历中当前位置的左边全是左子树，右边是右子树，可以获取左子树的size
        // 前序遍历中通过size[preorder_left+1,preorder_left+size]为左子树，右子树为[preorder_left + size_left_subtree + 1, preorder_right]
        // 递归建立左右子树

        // 前序遍历中的第一个节点就是根节点
        int preorder_root = preorder_left;
        // 在中序遍历中定位根节点
        int inorder_root = indexMap.get(preorder[preorder_root]);

        // 先把根节点建立出来
        TreeNode root = new TreeNode(preorder[preorder_root]);
        // 得到左子树中的节点数目
        int size_left_subtree = inorder_root - inorder_left;
        // 递归地构造左子树，并连接到根节点
        // 先序遍历中「从 左边界+1 开始的 size_left_subtree」个元素就对应了中序遍历中「从 左边界 开始到 根节点定位-1」的元素
        root.setLeft(doBuildTree(preorder, inorder, preorder_left + 1,
                preorder_left + size_left_subtree, inorder_left, inorder_root - 1));

        // 递归地构造右子树，并连接到根节点
        // 先序遍历中「从 左边界+1+左子树节点数目 开始到 右边界」的元素就对应了中序遍历中「从 根节点定位+1 到 右边界」的元素
        root.setRight(doBuildTree(preorder, inorder,
                preorder_left + size_left_subtree + 1, preorder_right, inorder_root + 1, inorder_right));
        return root;
    }


    public TreeNode buildTree2(int[] preorder, int[] inorder) {
        int n = preorder.length;
        // 构造哈希映射，帮助我们快速定位根节点
        indexMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < n; i++) {
            indexMap.put(inorder[i], i);
        }
        return doBuildTree(preorder, inorder, 0, n - 1, 0, n - 1);
    }
}
