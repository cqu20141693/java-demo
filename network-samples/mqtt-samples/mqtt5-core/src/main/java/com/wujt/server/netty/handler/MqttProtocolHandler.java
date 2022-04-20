package com.wujt.server.netty.handler;

import com.wujt.server.mqtt.stream.UpStreamHandler;
import com.wujt.server.netty.NettyUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wujt
 */
@ChannelHandler.Sharable
@Component
@Slf4j
public class MqttProtocolHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private UpStreamHandler upStreamHandler;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
        if (!(message instanceof MqttMessage)) {
            //非mqtt包，断开连接
            String msg = "not mqtt package,close channel.";
            log.info(msg);
            channelClose(ctx, msg);
            return;
        }
        MqttMessage msg = (MqttMessage) message;
        MqttMessageType messageType = msg.fixedHeader().messageType();
        if (messageType != MqttMessageType.CONNECT || messageType != MqttMessageType.DISCONNECT || messageType != MqttMessageType.AUTH) {
            if (!NettyUtils.connectSend(ctx.channel())) {
                log.error("the client send other message before MQTT Client.{}", ctx.channel());
                ctx.channel().close();
                return;
            }
        }
        switch (messageType) {
            case CONNECT:
                upStreamHandler.handleConnect(ctx.channel(), (MqttConnectMessage) msg);
                break;
            case PUBLISH:
                upStreamHandler.handlePublish(ctx.channel(), (MqttPublishMessage) msg);
                break;
            case PUBACK:
                upStreamHandler.handlePubAck(ctx.channel(), (MqttPubAckMessage) msg);
                break;
            case DISCONNECT:
                upStreamHandler.handleDisconnect(ctx.channel(),msg);
                break;
            case PINGREQ:
                upStreamHandler.handlePingReq(ctx.channel(), msg);
                break;
            case AUTH:
                upStreamHandler.handleAuth(ctx.channel(), msg);

            case SUBSCRIBE:
                upStreamHandler.handleSubscribe(ctx.channel(), (MqttSubscribeMessage) msg);
                break;
            case UNSUBSCRIBE:
                upStreamHandler.handleUnsubscribe(ctx.channel(), (MqttUnsubscribeMessage) msg);
                break;
            // 参考解码器实现
            case PUBREC:
                upStreamHandler.handlePubRec(ctx.channel(), msg);
                break;
            case PUBCOMP:
                upStreamHandler.handlePubComp(ctx.channel(), msg);
                break;
            case PUBREL:
                upStreamHandler.handlePubRel(ctx.channel(), msg);
                break;

            default:
                log.error("Unknown MessageType:{}", messageType);
                break;
        }
    }

    private void channelClose(ChannelHandlerContext ctx, String msg) {
        Channel channel = ctx.channel();
        // todo 如果是在线
        log.info("no related client and should close channel. channel = {}.", channel);
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isWritable()) {
            // todo 滞留消息处理或者通知上层
        }
        ctx.fireChannelWritabilityChanged();
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
