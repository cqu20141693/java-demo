package com.wujt.server.netty.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wujt
 */
@ChannelHandler.Sharable
@Slf4j
public class MqttIdleEventHandler extends ChannelDuplexHandler {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState e = ((IdleStateEvent) evt).state();
            if (e == IdleState.ALL_IDLE) {
                // todo fire a channelInactive to trigger publish of Will
                log.debug("MqttIdleEventHandler::userEventTriggered() is called, chan = {}, fireChannelInactive", ctx.channel());

            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
