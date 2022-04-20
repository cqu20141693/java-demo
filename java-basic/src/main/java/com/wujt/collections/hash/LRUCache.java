package com.wujt.collections.hash;

import java.util.HashMap;

/**
 * @author wujt
 */
public class LRUCache {

    /**
     *
     * Node类用于抽象链表的节点
     * key、value存储键、值，
     * before、after分别指向当前节点的前后Node节点；
     *
     */
    class Node {
        int key;
        int value;
        Node before;
        Node after;
    }
    /**
     * 使用HashMap缓存Node节点
     */
    private HashMap<Integer, Node> cache = new HashMap<>();
    /**
     * 最大容量，超过capacity时继续插入会触发删除最老未被使用的节点
     */
    private int capacity;
    /**
     * 头节点、尾节点（注意这两个节点不存储实际的数据）
     */
    private Node head, tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;

        head = new Node();
        head.before = null;

        tail = new Node();
        tail.after = null;

        head.after = tail;
        tail.before = head;
    }

    /**
     * 将节点插入队列头部
     *
     * @param node
     */
    private void addToHead(Node node) {

        node.before = head;
        node.after = head.after;
        head.after.before = node;
        head.after = node;

    }

    /**
     * 删除队列中的一个节点
     *
     * @param node
     */
    private void removeNode(Node node) {
        Node before = node.before;
        Node after = node.after;
        before.after = after;
        after.before = before;
    }
    /**
     * 将节点移动到有效数据头部
     *
     * @param node
     */
    private void moveToHead(Node node) {
        removeNode(node);
        addToHead(node);
    }
    /**
     * 删除有效数据尾节点
     *
     * @return 尾节点
     */
    private Node popTail() {
        Node res = tail.before;
        this.removeNode(res);
        return res;
    }

    public int get(int key) {
        // 首先查看map中的数据,没得返回null;
        // 存在，先将节点移动到头结点再返回数据
        Node node = cache.get(key);
        if (node == null) {
            return -1; // should raise exception here.
        }
        // 如果获取到数据，则将获取到的节点移动到队列头部;
        moveToHead(node);
        return node.value;
    }

    public void put(int key, int value) {
        // 首先看map是否存在数据，存在则直接返回old数据，并替换value,并将节点移动到head
        // 如果不存在，则创建一个节点，并将节点put进map,将节点放到头结点，
        // 后续再判断是否超过容量，如果容量超限，需要移除尾部节点，并在map中清楚tail节点
        Node node = cache.get(key);
        if (node == null) {
            Node newNode = new Node();
            newNode.key = key;
            newNode.value = value;
            cache.put(key, newNode);
            addToHead(newNode);
            if (cache.size() > capacity) {
                // 删除队尾有效数据节点
                Node tail = this.popTail();
                this.cache.remove(tail.key);
            }
        } else {
            node.value = value;
            // 在使用get方法获取值之后，需要将当前获取的节点移动到队列头部
            moveToHead(node);
        }
    }
}
