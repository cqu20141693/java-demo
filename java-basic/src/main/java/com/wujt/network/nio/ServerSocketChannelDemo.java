package com.wujt.network.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * jdk1.4
 * ServerSocketChannel: 可选择性的面向流的服务端套接字
 * <p>
 * fields:
 * boolean blocking = true;
 * ServerSocket socket ： 具体实现serverSocketChannel的实现类ServerSocketChannelImpl
 * public final SelectionKey register(Selector sel, int ops) : 将通道注册到管理者Selector,并表明关注的事件
 *
 * <p>
 * API:
 *
 * @author wujt
 */
public class ServerSocketChannelDemo {
    public static void main(String[] args) throws IOException {
        // 获得一个ServerSocket通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设置通道为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 将该通道对应的ServerSocket绑定到port端口
        serverSocketChannel.socket().bind(new InetSocketAddress(8888));


    }
}
