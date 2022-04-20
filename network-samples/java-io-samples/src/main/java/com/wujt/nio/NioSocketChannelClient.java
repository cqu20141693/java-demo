package com.wujt.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static com.gow.domain.NioConfig.PORT;

/**
 * @author wujt
 */
public class NioSocketChannelClient {
    private Selector selector;          //创建一个选择器
    private final static int BUF_SIZE = 10240;
    private static ByteBuffer byteBuffer = ByteBuffer.allocate(BUF_SIZE);

    public void initClient() throws IOException {
        this.selector = Selector.open();
        // 创建一个client SocketChannel
        SocketChannel clientChannel = SocketChannel.open();
        //将通道设置为非阻塞
        clientChannel.configureBlocking(false);
        // 连接server socketChannel
        clientChannel.connect(new InetSocketAddress(PORT));
        //将上述的通道管理器和通道绑定，并为该通道注册OP_CONNECT事件
        //注册事件后，当该事件到达时，selector.select()会返回（一个key），如果该事件没到达selector.select()会一直阻塞
        // 并且会将channel和SelectionKey 绑定，当事件发生时，可以直接取出Channel处理
        SelectionKey selectionKey = clientChannel.register(selector, SelectionKey.OP_CONNECT);

        while (true) {
            //这是一个阻塞方法，一直等待直到有数据可读，返回值是key的数量（可以有多个）
            int select = selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isConnectable()) {
                    doConnect(key);
                } else if (key.isReadable()) {
                    doRead(key);
                }
            }
        }
    }

    public void doConnect(SelectionKey key) throws IOException {
        // 获取选择器中的SocketChannel
        SocketChannel clientChannel = (SocketChannel) key.channel();
        // 如果正在进行连接炒作
        if (clientChannel.isConnectionPending()) {
            clientChannel.finishConnect();
        }
        // 再次设置Channel 非阻塞
        clientChannel.configureBlocking(false);
        String info = "服务端你好!!";
        byteBuffer.clear();
        byteBuffer.put(info.getBytes("UTF-8"));
        byteBuffer.flip();
        clientChannel.write(byteBuffer);
        clientChannel.register(key.selector(),SelectionKey.OP_READ);
        //clientChannel.close();
    }

    public void doRead(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        clientChannel.read(byteBuffer);
        byte[] data = byteBuffer.array();
        String msg = new String(data).trim();
        System.out.println("服务端发送消息：" + msg);
        clientChannel.close();
        key.selector().close();
    }

}
