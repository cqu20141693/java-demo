package com.cc.sip.uas;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.pkts.packet.sip.SipMessage;
import io.pkts.packet.sip.SipResponse;
import io.sipstack.netty.codec.sip.SipMessageEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * A super simple UAS implementation.
 *
 * @author gow
 * @date 2022/2/15
 */
@ChannelHandler.Sharable
@Slf4j
public final class UASHandler extends SimpleChannelInboundHandler<SipMessageEvent> {

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final SipMessageEvent event) throws Exception {
        final SipMessage msg = event.getMessage();

        // just consume the ACK
        if (msg.isAck()) {
            return;
        }

        // for all requests, just generate a 200 OK response.
        if (msg.isRequest()) {
            log.info("receive msg=  {}",msg.toRequest());
            final SipResponse response = msg.createResponse(200);
            event.getConnection().send(response);
        }
    }

}
