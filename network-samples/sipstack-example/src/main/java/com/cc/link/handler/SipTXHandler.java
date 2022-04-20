package com.cc.link.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.sipstack.netty.codec.sip.SipMessageEvent;

/**
 * @author gow
 * @date 2022/2/16
 */
public class SipTXHandler extends SimpleChannelInboundHandler<SipMessageEvent> {

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SipMessageEvent event) {
        // todo 事务层
        ctx.fireChannelRead(event);
    }
}
