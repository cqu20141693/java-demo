package com.wujt.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author wujt
 */
public class NIOReactorServer {
    public static void main(String[] args) throws IOException {
        // 利用一个Selector 管理服务端监听socket
        Selector serverSelector = Selector.open();
        // 利用另外一个Selector管理客户端socket
        // netty 是每一个EventLoop绑定一个Selector管理多个注册的SocketChannel
        Selector clientSelector = Selector.open();

        new Thread(() -> {
            doAcceptor(serverSelector, clientSelector);
        }).start();

        new Thread(() -> {
            doSubReactor(clientSelector);
        }).start();

    }

    private static void doSubReactor(Selector clientSelector) {
        try {
            while (true) {
                // (2) 批量轮询是否有哪些连接有数据可读，这里的1指的是阻塞的时间为1ms
                if (clientSelector.select(1) > 0) {
                    Set<SelectionKey> selectionKeys = clientSelector.selectedKeys();
                    Iterator<SelectionKey> keys = selectionKeys.iterator();
                    while (keys.hasNext()) {
                        SelectionKey key = keys.next();
                        if (key.isReadable()) {
                            try {
                                SocketChannel clientChannel = (SocketChannel) key.channel();
                                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                // (3) 读取数据以块为单位批量读取
                                clientChannel.read(byteBuffer);
                                // 写模式切换为读模式
                                byteBuffer.flip();
                                System.out.println(Charset.defaultCharset().newDecoder().decode(byteBuffer).toString());
                            } finally {
                                // 删除已处理的SelectionKey
                                keys.remove();
                                key.interestOps(SelectionKey.OP_READ);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void doAcceptor(Selector serverSelector, Selector clientSelector) {
        try {
            // 对应Netty服务端启动的4个步骤
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(serverSelector, SelectionKey.OP_ACCEPT);
            serverSocketChannel.socket().bind(new InetSocketAddress(8000));

            while (true) {
                // 监测是否有新的连接，这里的1指的是阻塞的时间为1ms
                if (serverSelector.select(1) > 0) {
                    Set<SelectionKey> selectionKeys = serverSelector.selectedKeys();
                    Iterator<SelectionKey> keys = selectionKeys.iterator();
                    while (keys.hasNext()) {
                        SelectionKey key = keys.next();
                        if (key.isAcceptable()) {
                            try {
                                // (1) 每来一个新连接，不需要创建一个线程，而是直接注册到clientSelector
                                SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
                                clientChannel.configureBlocking(false);
                                clientChannel.register(clientSelector, SelectionKey.OP_READ);
                            } finally {
                                // 删除已处理的SelectionKey
                                keys.remove();
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
