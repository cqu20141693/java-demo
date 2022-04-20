package com.wujt.nio;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Channel共有四种通道：
 * FileChannel：作用于IO文件流
 * DatagramChannel：作用于UDP协议
 * SocketChannel：作用于TCP协议
 * ServerSocketChannel：作用于TCP协议服务端
 *
 * @author wujt
 */
public class ChannelDemo {
    public static void main(String[] args) {
        try {
            SocketChannelAPITest();

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            while (true) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                //通道设置为非阻塞
                socketChannel.configureBlocking(false);
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private static void SocketChannelAPITest() throws IOException {
        NioSocketChannelClient myNioClient = new NioSocketChannelClient();
        myNioClient.initClient();
    }
}
