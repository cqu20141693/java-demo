package com.cc.link.handler;

import com.cc.link.model.SipInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.pkts.packet.sip.SipMessage;
import io.pkts.packet.sip.SipResponse;
import io.pkts.packet.sip.address.URI;
import io.pkts.packet.sip.address.impl.SipURIImpl;
import io.pkts.packet.sip.header.FromHeader;
import io.pkts.packet.sip.header.impl.FromHeaderImpl;
import io.pkts.packet.sip.impl.SipInitialLine;
import io.sipstack.netty.codec.sip.SipMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2022/2/16
 */
@Slf4j
@Component
public class SipHandler extends SimpleChannelInboundHandler<SipMessageEvent> {
    @Autowired
    private SipConnections sipConnections;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SipMessageEvent event) {
        final SipMessage msg = event.getMessage();
        log.info("receive msg \n{}", msg.toRequest());
        if (msg.isAck()) {
            // todo ack business
            return;
        }
        if (msg.isRequest()) {
            if (msg.isRegister()) {

                SipInitialLine initialLine = SipInitialLine.parse(msg.getInitialLine());
                SipURIImpl uri = (SipURIImpl) initialLine.toRequestLine().getRequestUri();
                SipInfo sipInfo = new SipInfo();
                sipInfo.setSUri(uri);
                sipInfo.setCUri((SipURIImpl) msg.getFromHeader().getAddress().getURI());
                sipConnections.addConnection(sipInfo);
            }
            final SipResponse response = msg.createResponse(200);
            event.getConnection().send(response);
        }
        if (msg.isResponse()) {
            //todo response
        }
    }
}
