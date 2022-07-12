package com.cc.netwok.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import javax.annotation.Nonnull;

/**
 * 空消息
 * wcc 2022/6/29
 */
public final class EmptyMessage implements EncodedMessage {

    public static final EmptyMessage INSTANCE = new EmptyMessage();

    private EmptyMessage() {
    }

    @Nonnull
    @Override
    public ByteBuf getPayload() {
        return Unpooled.wrappedBuffer(new byte[0]);
    }

    @Override
    public String toString() {
        return "empty message";
    }
}
