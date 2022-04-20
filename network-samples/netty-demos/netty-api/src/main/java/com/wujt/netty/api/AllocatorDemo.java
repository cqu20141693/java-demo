package com.wujt.netty.api;

import io.netty.buffer.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author wujt
 * ByteBufAllocator作为内存分配器的抽象
 * //1.分配ByteBuf，是否是堆外内存依赖于具体的实现
 * ByteBuf buffer(int initialCapacity,int maxCapacity);
 * // 2.分配适合于IO的ByteBuf，期待是堆外内存
 * ByteBuf ioBuffer(int initialCapacity,int maxCapacity);
 * // 3.分配堆内存ByteBuf
 * ByteBuf heapBuffer(int initialCapacity,int maxCapacity);
 * // 4.分配堆外内存DirectByteBuffer
 * ByteBuf directBuffer(int initialCapacity,int maxCapacity);
 *
 * AbstractByteBufAllocator实际上是ByteBufAllocator的骨架实现，
 * 暴露了newHeapBuffer()、newDirectBuffer()两个抽象方法
 * 具体实现子类UnpooledByteBufAllocator、PooledByteBufAllocator，用于这两个子类实现具体的分配动作
 *
 *
 *
 * PooledByteBufAllocator内存分配
 * 利用PoolThreadLocalCache进行实现，PoolThreadLocalCache为FastThreadLocal实现，
 * 因此调用threadCache.get()获取PoolThreadCache时，不同NIO线程拿到的是各自的PoolThreadCache，这样使得多线程内存分配减少了竞争。
 *然后调用PoolThreadCache.directArena;获取堆外内存相关的PoolArena，再调用PoolArena.allocate()方法进行内存分配。
 *
 * <p>
 * buffer();
 * buffer(initCapacity);
 * buffer(initCapacity, maxCapacity);
 * <p>
 * heapBuffer();
 * heapBuffer(initCapacity);
 * heapBuffer(initCapacity, maxCapacity);
 * <p>
 * directBuffer();
 * directBuffer(..);
 * directBuffer(.., ..);
 * <p>
 * compositeBuffer();
 * compositeBuffer(..);
 * compositeDirectBuffer();
 * compositeDirectBuffer(..);
 * compositeHeapBuffer();
 * compositeHeapBuffer(..);
 * <p>
 * ioBuffer(); // for i/o in socket
 */
public class AllocatorDemo {
    public static void main(String[] args) {
        testAllocator();
    }

    public static void testAllocator() {
        // 默认使用的是PooledByteBufAllocator
        NioServerSocketChannel channel = new NioServerSocketChannel();
        ByteBufAllocator alloc = channel.alloc();
        ByteBuf byteBuf = alloc.heapBuffer();
        System.out.println(byteBuf.hasArray());

        //  ChannelHandlerContext ctx ;
        // ByteBufAllocator allocator = ctx.alloc();


        // true表示调用buffer()时，使用直接内存，false表示堆内存
        ByteBufAllocator allocator = new UnpooledByteBufAllocator(true);
        ByteBufAllocator allocator2 = new PooledByteBufAllocator(true);
        ByteBuf buffer = allocator.buffer();
        buffer = allocator.buffer(10);
        CompositeByteBuf compositeByteBuf = allocator.compositeBuffer();
    }

}
