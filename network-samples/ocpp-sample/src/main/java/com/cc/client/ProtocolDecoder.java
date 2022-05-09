package com.cc.client;

import com.alibaba.fastjson.JSONObject;
import com.cc.network.cp.CPMessage;
import com.cc.network.cp.domian.enums.MessageType;
import com.gow.codec.bytes.deserializable.DecodeContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.gow.codec.bytes.DataType.OBJECT;

/**
 * 编解码处理器
 * wcc 2022/5/6
 */
@Data
@Slf4j
public class ProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelCtx, ByteBuf byteBuf, List<Object> list) throws Exception {
        int capacity = byteBuf.capacity();
        byte[] bytes = new byte[capacity];
        byteBuf.readBytes(bytes);

        DecodeContext context = OBJECT.deserialize(bytes, 0, 0, new CPMessage());
        CPMessage obj = (CPMessage) context.getObj();
        log.info("receive msg={}", JSONObject.toJSONString(context.getObj()));
        MessageType type = obj.getBody().getType();
        if (type == MessageType.LOGIN_REPLY) {

        } else if (type == MessageType.CHARGING) {
            log.info("handle charge message...");
            CPMessage reply = Utils.getDefaultChargingReply();
            channelCtx.channel().writeAndFlush(reply);
        }
        list.add(obj);
    }
}
