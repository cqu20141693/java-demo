package com.wujt.network.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * SocketChannel: 可选择性的面向流的套接字通道
 * <p>
 * fields:
 * <p>
 * API:
 * bind(SocketAddress local): 绑定一个本地监听端口；如果不指定；系统随机分配
 * connect(SocketAddress remote): 连接的远程主机地址
 * finishConnect()： 只有调用改方法后才能进行数据的读取和写入
 * int read(ByteBuffer dst): 当两个线程调用方法时； 第二个线程阻塞到第一个线程读取完成。没有数据读取数据为0
 * int write(ByteBuffer src) : 当两个线程调用方法时； 第二个线程阻塞到第一个线程写入完成。缓冲区无法写入时，写入返回为零。
 *
 * @author wujt
 */
public class SocketChannelDemo {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        InetSocketAddress socketAddress = new InetSocketAddress(8888);
        socketChannel.bind(socketAddress);
        socketChannel.connect(socketAddress);
        socketChannel.finishConnect();
        ByteBuffer allocate = ByteBuffer.allocate(20);
        socketChannel.read(allocate);
        allocate.clear();
        allocate = ByteBuffer.wrap("hello".getBytes());
        socketChannel.write(allocate);
    }
}
