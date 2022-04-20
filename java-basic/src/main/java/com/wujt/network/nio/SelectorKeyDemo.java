package com.wujt.network.nio;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

/**
 * SelectionKey : channel 和selector 的一个关联对象， 具体实现类SelectionKeyImpl
 * <p>
 * fields:
 * final SelChImpl channel;
 * public final SelectorImpl selector;
 * private int index;
 * private volatile int interestOps;
 * private int readyOps;
 * private volatile Object attachment ： netty 就是通过这个将channel 和SelectKey 进行关联的
 * @author wujt
 */
public class SelectorKeyDemo {
    public static void main(String[] args) {

    }

    public void handle(Selector selector) {
        Set<SelectionKey> selectionKeys = selector.selectedKeys();


    }
}
