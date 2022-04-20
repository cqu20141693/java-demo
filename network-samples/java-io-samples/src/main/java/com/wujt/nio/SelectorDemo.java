package com.wujt.nio;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Selector选择器是NIO的核心，它是channel的管理者
 * 通过执行select()阻塞方法，监听是否有channel准备好
 * 一旦有数据可读，此方法的返回值是SelectionKey的数量
 * <p>
 * 所以服务端通常会死循环执行select()方法，直到有channl准备就绪，然后开始工作
 * 每个channel都会和Selector绑定一个事件，然后生成一个SelectionKey的对象
 * 需要注意的是：
 * channel和Selector绑定时，channel必须是非阻塞模式
 * 而FileChannel不能切换到非阻塞模式，因为它不是套接字通道，所以FileChannel不能和Selector绑定事件
 * <p>
 * 在NIO中一共有四种事件：
 * 1.SelectionKey.OP_CONNECT：连接事件
 * 2.SelectionKey.OP_ACCEPT：接收事件
 * 3.SelectionKey.OP_READ：读事件
 * 4.SelectionKey.OP_WRITE：写事件
 *
 * API:
 *  public static Selector open()
 *
 *
 *
 * <p>
 * https://segmentfault.com/a/1190000012316621
 * <p>
 * <p>
 * SelectionKey： Channel 注册到Selector时生成的Key
 * 对象包含了一些比较有价值的属性：
 * The interest set
 * The ready set
 * The Channel
 * The Selector
 * An attached object (optional)
 * <p>
 * public final Object attachment()
 * <p>
 * public final boolean isAcceptable()
 * public final boolean isConnectable()
 * public final boolean isReadable()
 * public final boolean isWritable()
 *
 * @author wujt
 */
public class SelectorDemo {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        Selector selector = Selector.open();
        SelectionKey key = socketChannel.register(selector, SelectionKey.OP_ACCEPT);

        for (; ; ) {
            selector.select(10);
            int select = selector.select();
            // 选择管理器中的注册的所有的SelectionKey
            Set<SelectionKey> keys = selector.keys();
            // 就绪的key集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                // 需要remove,当
                iterator.remove();
                SelectionKey selectionKey = iterator.next();
                Object attachment = selectionKey.attachment();
                SelectableChannel channel = selectionKey.channel();
                Selector selector1 = selectionKey.selector();

                int interestOps = selectionKey.interestOps();
                int interestedAndRead = interestOps | SelectionKey.OP_READ;
                selectionKey.interestOps(interestedAndRead);
                // 对
                int readyOps = selectionKey.readyOps();

                // 处理当前就绪Key的就绪事件
                if (selectionKey.isAcceptable()) {
                    // a connection was accepted by a ServerSocketChannel.

                } else if (selectionKey.isConnectable()) {
                    // a connection was established with a remote server.

                } else if (selectionKey.isReadable()) {
                    // a channel is ready for reading

                } else if (selectionKey.isWritable()) {
                    // a channel is ready for writing
                }
            }
        }
    }
}
