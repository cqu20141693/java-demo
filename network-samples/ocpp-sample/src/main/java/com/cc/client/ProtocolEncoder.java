package com.cc.client;

import com.cc.network.cp.CPMessage;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import static com.gow.codec.bytes.DataType.OBJECT;

public class ProtocolEncoder extends MessageToMessageEncoder<CPMessage> {


    @Override
    protected void encode(ChannelHandlerContext ctx, CPMessage message, List<Object> out) throws Exception {

        byte[] bytes = OBJECT.serialize(message);

        out.add(Unpooled.wrappedBuffer(bytes));
    }
}
