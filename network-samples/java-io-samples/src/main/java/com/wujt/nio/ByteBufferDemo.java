package com.wujt.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * ByteBuffer NIO提供的缓冲容器：包括HeapByteBuffer，DirectByteBuffer
 * public static ByteBuffer allocate(int capacity) 分配一个固定容量的缓冲容器： 默认HeapByteBuffer
 * public final ByteBuffer put(byte[] src) 像缓冲容器中写入数据
 * public ByteBuffer get(byte[] dst, int offset, int length) 读取缓冲区中的数据到dst
 * public final Buffer flip()  切换读写指针
 * public final Buffer clear() // 重置缓存器
 *
 * @author wujt
 */
public class ByteBufferDemo {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        byteBuffer.put("hello".getBytes());
        byte b = byteBuffer.get();
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            boolean connect = socketChannel.connect(new InetSocketAddress("localhost", 8686));
            int write = socketChannel.write(byteBuffer);
            int read = socketChannel.read(byteBuffer);
            byte[] bytes = new byte[1024];
            byteBuffer.flip();
            byteBuffer.get(bytes, 0, byteBuffer.limit());
            System.out.println(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(1024);
        // 复原 ByteBuffer
        allocateDirect.clear();
        allocateDirect.put("hello".getBytes());

    }
}
