package com.cc.ocpp.network.codec;

import com.cc.ocpp.client.KeepaliveHandler;
import com.cc.ocpp.client.Utils;
import com.cc.ocpp.network.cp.CPMessage;
import com.cc.ocpp.network.cp.Header;
import com.cc.ocpp.network.cp.config.CPServerProperties;
import com.cc.ocpp.network.cp.domian.Body;
import com.cc.ocpp.network.cp.domian.FunctionReply;
import com.cc.ocpp.network.cp.domian.TokenGenerator;
import com.cc.ocpp.network.cp.domian.Version;
import com.cc.ocpp.network.cp.domian.control.ChargingMessage;
import com.cc.ocpp.network.cp.domian.enums.MessageType;
import com.cc.ocpp.network.cp.domian.login.LoginReplyMessage;
import com.gow.codec.bytes.deserializable.DecodeContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 充电桩协议处理器
 * wcc 2022/5/15
 */
@Slf4j
public class OCPPHandler extends ChannelInboundHandlerAdapter {

    private final static AtomicInteger counter = new AtomicInteger(0);
    private final Boolean server;

    private final CPServerProperties properties;

    public OCPPHandler(boolean server, CPServerProperties properties) {
        this.server = server;
        this.properties = properties;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof DecodeContext) {
            DecodeContext context = (DecodeContext) msg;
            if (!context.success()) {
                log.info("OCPP msg decode failed");
                return;
            }
            CPMessage obj = (CPMessage) context.getObj();
            log.info("receive message type={}", obj.getBody().getType());
            if (server) {
                serverHandler(ctx, obj);
            } else {
                clientHandler(ctx, obj);
            }
        } else {
            log.info("receive msg not DecodeContext");
        }
    }

    private void serverHandler(ChannelHandlerContext ctx, CPMessage obj) {
        MessageType type = obj.getBody().getType();
        if (type == MessageType.LOGIN) {
            log.info("receive login message,publish login event.");
            log.info("handle login event,login reply.");
            LoginReplyMessage reply = new LoginReplyMessage();
            reply.setSequence(obj.getHeader().getSequence());
            reply.setKeepalive(properties.getKeepalive());
            reply.setReportInterval(properties.getReportInterval());
            reply.setSuccess((byte) 1);
            reply.setToken(TokenGenerator.generate());
            reply.setTimeSeconds((int) (System.currentTimeMillis() / 1000));
            CPMessage cpMessage = cpMessage(MessageType.LOGIN_REPLY);
            cpMessage.setBody(reply);
        }
    }

    public static CPMessage cpMessage(MessageType messageType) {
        CPMessage cpMessage = new CPMessage();
        cpMessage.setFlag((byte) 0x7e);
        cpMessage.setEndFlag((byte) 0x7e);
        Header header = new Header(messageType.getMessageId(), getSequence(), (byte) 0, TokenGenerator.generate(), Version.default_version);
        cpMessage.setHeader(header);
        return cpMessage;
    }

    private static Short getSequence() {
        counter.compareAndSet(0xffff, 0);
        return (short) counter.getAndIncrement();
    }

    private void clientHandler(ChannelHandlerContext ctx, CPMessage obj) {
        MessageType type = obj.getBody().getType();
        if (type == MessageType.LOGIN_REPLY) {

        } else if (type == MessageType.CHARGING) {
            ChargingMessage objBody = (ChargingMessage) obj.getBody();
            log.info("handle charge message...");
            CPMessage reply = Utils.getDefaultChargingReply();

            // 真是处理情况下，应该把序列号进行上下文传输，用于reply
            // 设置序列号
            Body body = reply.getBody();
            if (body instanceof FunctionReply) {
                ((FunctionReply) body).setSequence(obj.getHeader().getSequence());
            }
            if (objBody.getStatus() == 1) {
                KeepaliveHandler.status = 1;
            } else {
                KeepaliveHandler.status = -1;
            }
            ctx.channel().writeAndFlush(reply);
        }
    }
}
