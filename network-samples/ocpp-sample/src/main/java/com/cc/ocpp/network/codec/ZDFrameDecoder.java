package com.cc.ocpp.network.codec;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

/**
 * zhida 充电桩帧解析器
 * wcc 2022/5/10
 */
public class ZDFrameDecoder extends DelimiterBasedFrameDecoder {

    public ZDFrameDecoder() {
        super(Integer.MAX_VALUE, false, true, Unpooled.wrappedBuffer(new byte[]{0x7e}));
    }
}
