package com.wujt.netty.api;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author wujt
 * buffer(); // 基于堆的ByteBuff
 * buffer(..); // 同上
 * <p>
 * directBuffer(); // 同上
 * <p>
 * wrappedBuffer(); // 包装给定内容
 * copiedBuffer(); // 拷贝给定内容
 */
public class UnpooledDemo {
    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer();
        buffer = Unpooled.buffer(10);
        ByteBuf directBuffer = Unpooled.directBuffer();

        ByteBuf byteBuf = Unpooled.wrappedBuffer(directBuffer);

        ByteBuf copiedBuffer = Unpooled.copiedBuffer(new byte[]{1, 0});


    }
}
