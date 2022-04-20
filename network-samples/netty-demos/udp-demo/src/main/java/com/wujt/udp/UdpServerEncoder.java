package com.wujt.udp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author wujt
 */
public class UdpServerEncoder extends MessageToMessageEncoder<MessageBean> {

    /**
     * 需要传输的远方地址
     */
    private final InetSocketAddress remoteAddress;

    public UdpServerEncoder() {
        // 广播地址
        this.remoteAddress = new InetSocketAddress("255.255.255.255", UdpClient.PORT);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageBean msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = Unpooled.copiedBuffer(msg.getTime(), StandardCharsets.UTF_8);
        System.out.println("[Server] encode to " + remoteAddress.toString());
        out.add(new DatagramPacket(byteBuf, remoteAddress));
    }

}