package com.wujt.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @author wujt
 */
public class UdpClientDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        ByteBuf content = msg.content();
        final String result = content.toString(CharsetUtil.UTF_8);
        System.out.println("[Client] decode msg: " + result);
        MessageBean messageBean = new MessageBean();
        messageBean.setTime(result);

        out.add(messageBean);
    }

}
