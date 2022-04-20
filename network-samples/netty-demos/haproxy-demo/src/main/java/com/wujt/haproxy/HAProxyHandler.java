package com.wujt.haproxy;

import io.netty.channel.*;
import io.netty.handler.codec.haproxy.HAProxyMessage;
import io.netty.handler.codec.haproxy.HAProxyMessageEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wujt
 */
@Slf4j
public class HAProxyHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerAdded is trigger");
        ctx.pipeline().addBefore(ctx.name(), null, HAProxyMessageEncoder.INSTANCE);
        super.handlerAdded(ctx);
    }

    @Override
    public void write(final ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ChannelFuture future = ctx.write(msg, promise);
        if (msg instanceof HAProxyMessage) {
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        log.info("remove is trigger");
                        ctx.pipeline().remove(HAProxyMessageEncoder.INSTANCE);
                        ctx.pipeline().remove(HAProxyHandler.this);
                    } else {
                        ctx.close();
                    }
                }
            });
        }
    }
}