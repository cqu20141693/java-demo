package com.wujt.network.nio;

import com.wujt.并发编程.util.ConcurrentUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author wujt
 */
public class NioClientDemo {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        Selector selector = Selector.open();
        InetSocketAddress socketAddress = new InetSocketAddress(8888);
        socketChannel.connect(socketAddress);
        socketChannel.configureBlocking(false);
        ByteBuffer byteBuffer = ByteBuffer.allocate(50);
        SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ, byteBuffer);
        while (!socketChannel.finishConnect()) {
            System.out.println("wait, or do something else...\n");
        }
        Thread write = new Thread(() -> {
            while (true) {
                ByteBuffer attachment = (ByteBuffer) selectionKey.attachment();
                try {

                    socketChannel.write( ByteBuffer.wrap("I am a client".getBytes()));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                ConcurrentUtils.sleep(2000);
            }
        }, "write");
        write.start();
        while (true) {
            selector.select(10);
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey nextKey = iterator.next();
                iterator.remove();
                if (nextKey.isReadable()) {
                    ByteBuffer attachment = (ByteBuffer) nextKey.attachment();
                    SocketChannel channel = (SocketChannel) nextKey.channel();
                    channel.read(attachment);
                    byte[] data = attachment.array();
                    String msg = new String(data).trim();
                    System.out.println("客户端收到信息：" + msg);
                    //将缓冲器转换
                    attachment.flip();
                }
            }

        }

    }
}
