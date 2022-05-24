/*
 * Copyright (c) 2012-2015 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * The Apache License v2.0 is available at
 * http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package com.cc.netwok.handler;

import com.cc.netwok.domain.ChannelAliveEvent;
import com.cc.netwok.utils.NettyUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Sharable
@Slf4j
public class UserEventHandler extends ChannelDuplexHandler {

    public UserEventHandler() {
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {

        if (evt instanceof IdleStateEvent) {
            IdleState e = ((IdleStateEvent) evt).state();
            if (e == IdleState.ALL_IDLE) {
                //fire a channelInactive to trigger publish of Will
                String clientId = NettyUtils.strAttr(ctx.channel(), NettyUtils.ATTR_CLIENT_ID);
                if (clientId != null) {
                    log.info("sendError(ctx.channel(), heartbeat is lost!");
                }
                log.info("link idle clientId = {}, fireChannelInactive", clientId);
                ctx.fireChannelInactive();
                ctx.close();
            }
        } else if (evt instanceof ChannelAliveEvent) {
            reportLinkAlive(ctx.channel(), ((ChannelAliveEvent) evt).getChannelAliveCheckTime());
        } else {
            try {
                super.userEventTriggered(ctx, evt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void reportLinkAlive(Channel channel, Integer channelAliveCheckTime) {
        log.info("idle check alive channel={},alive time={}",
                NettyUtils.strAttr(channel, NettyUtils.ATTR_CLIENT_ID), channelAliveCheckTime);
    }
}
