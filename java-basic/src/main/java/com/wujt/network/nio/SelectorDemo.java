package com.wujt.network.nio;

import java.io.IOException;
import java.nio.channels.Selector;

/**
 * Selector: NIO 字节流双向通道管理容器，具体实现类SelectorImpl
 * <p>
 * fields:
 * protected Set<SelectionKey> selectedKeys = new HashSet() ： 表示关注事件中到达事件Key
 * protected HashSet<SelectionKey> keys = new HashSet() : 整个管理容器中管理的SelectorKey
 * API:
 * public abstract int selectNow()
 * public abstract int select(long timeout)
 * public abstract int select() : 阻塞等待
 *  public abstract Set<SelectionKey> selectedKeys() ： 获取准备好的SelectKey
 *  public static Selector open() : 创建一个Selector 实现
 * @author wujt
 */
public class SelectorDemo {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
    }
}
