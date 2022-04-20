package com.wujt.network.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Nio 特点：
 * 双向通道（channel）进行数据传输；在通道上可以注册我们感兴趣的事件
 * 事件名	                    对应值
 * 服务端接收客户端连接事件	SelectionKey.OP_ACCEPT(16)
 * 客户端连接服务端事件	    SelectionKey.OP_CONNECT(8)
 * 读事件	                SelectionKey.OP_READ(1)
 * 写事件	                SelectionKey.OP_WRITE(4)
 * <p>
 * 管理通道的对象，我们称之为selector
 *
 * @author wujt
 */
public class NioServerDemo {
    public static void main(String[] args) throws IOException {
        // 获得一个ServerSocket通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        // 设置通道为非阻塞
        serverChannel.configureBlocking(false);
        // 将该通道对应的ServerSocket绑定到port端口
        int port = 8888;
        serverChannel.socket().bind(new InetSocketAddress(port));
        // 获得一个通道管理器
        Selector selector = Selector.open();
        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，
        //当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞。
        ByteBuffer buffer = ByteBuffer.allocate(100);
        SelectionKey selectionKey = serverChannel.register(selector, SelectionKey.OP_ACCEPT, buffer);

        listen(selector);

    }

    private static void listen(Selector selector) throws IOException {

        System.out.println("服务端Channel 绑定Selector 成功！");
        // 轮询访问selector
        while (true) {
            //当注册的事件到达时，方法返回；否则,该方法会一直阻塞
            selector.select();
            // 获得selector中选中的项的迭代器，选中的项为注册的事件
            Iterator<SelectionKey> ite = selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = ite.next();
                // 删除已选的key,以防重复处理
                ite.remove();
                // 客户端请求连接事件
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key
                            .channel();
                    // 获得和客户端连接的通道
                    SocketChannel channel = server.accept();
                    // 设置成非阻塞
                    channel.configureBlocking(false);

                    //在这里可以给客户端发送信息哦
                    channel.write(ByteBuffer.wrap(new String("向客户端发送了一条信息").getBytes()));
                    //在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。
                    channel.register(selector, SelectionKey.OP_READ,key.attachment());
                    System.out.println("与【" + channel.getRemoteAddress() + "】建立了连接！");
                    // 获得了可读的事件
                } else if (key.isReadable()) {
                    read(key, (ByteBuffer) key.attachment());
                }

            }
        }
    }

    private static void read(SelectionKey key, ByteBuffer buffer) throws IOException {


        // 读取到流末尾说明TCP连接已断开，
        // 因此需要关闭通道或者取消监听READ事件
        // 否则会无限循环
        if (((SocketChannel) key.channel()).read(buffer) == -1) {
            key.channel().close();
            return;
        }
        // 服务器可读取消息:得到事件发生的Socket通道
        SocketChannel channel = (SocketChannel) key.channel();
        // 创建读取的缓冲区

        channel.read(buffer);
        byte[] data = buffer.array();
        String msg = new String(data).trim();
        System.out.println("服务端收到信息：" + msg);
        //将缓冲器转换
        int write = channel.write( ByteBuffer.wrap(msg.getBytes()));// 将消息回送给客户端
        if (write == 0) {
            System.out.println("缓存区已满，不能发送出去");
            // 循环写；直到写完为止
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        }
    }
}
