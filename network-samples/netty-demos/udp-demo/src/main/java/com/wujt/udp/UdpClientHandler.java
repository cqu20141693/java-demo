package com.wujt.udp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author wujt
 */
public class UdpClientHandler extends SimpleChannelInboundHandler<MessageBean> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageBean msg) throws Exception {
        System.out.println("[Client] received from server: " + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}