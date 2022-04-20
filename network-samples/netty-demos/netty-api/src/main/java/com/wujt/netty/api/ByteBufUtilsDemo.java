package com.wujt.netty.api;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * @author wujt
 */
public class ByteBufUtilsDemo {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello".getBytes());
        String hexDump = ByteBufUtil.hexDump(byteBuf);
        System.out.println(hexDump);
    }
}
