package com.cc.network.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * zhida 充电桩帧解析器
 * wcc 2022/5/10
 */
public class ZDFrameDecoder extends LengthFieldBasedFrameDecoder {

    public ZDFrameDecoder() {
        super(Integer.MAX_VALUE, 11, 2, 2, 0);
    }
}
