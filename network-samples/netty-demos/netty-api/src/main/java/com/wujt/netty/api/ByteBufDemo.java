package com.wujt.netty.api;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * @author wujt
 * <p>
 * https://www.cnblogs.com/leesf456/p/6898069.html
 */

public class ByteBufDemo {
    public static void main(String[] args) {
        // get,set 不会影响读写指针，进行随机访问
        ByteBuf buffer = Unpooled.copiedBuffer("hello world".getBytes());
        System.out.println(buffer.hasArray());
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.print((char) buffer.getByte(i));
        }


        testCompositeByteBuf();

        testSlice();
    }

    private static void testCompositeByteBuf() {
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
    }

    public static void testSlice() {
        test1();

        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        // 切片出一部分内存共享到一个buffer
        ByteBuf sliced = buf.slice(0, 14);
        System.out.println(sliced.toString(utf8));
        buf.setByte(0, (byte) 'J');
        assert buf.getByte(0) == sliced.getByte(0);

        // copy
        ByteBuf copy = buf.copy(0, 14);
        System.out.println(copy.toString(utf8));
        buf.setByte(0, (byte) 'N');
        assert buf.getByte(0) != copy.getByte(0);
    }

    private static void test1() {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);
        System.out.println("allocate ByteBuf(9, 100)   " + buffer);

        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        System.out.println("writeBytes(1,2,3,4)  " + buffer);

        ByteBuf buffer1 = buffer.slice();
        System.out.println("buffer slice   " + buffer1);
        // 共享内存数据
        assert buffer1 == buffer;
    }
}
