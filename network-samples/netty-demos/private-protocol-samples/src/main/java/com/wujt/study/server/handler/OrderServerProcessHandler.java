package com.wujt.study.server.handler;

import com.wujt.study.common.Operation;
import com.wujt.study.common.OperationResult;
import com.wujt.study.common.RequestMessage;
import com.wujt.study.common.ResponseMessage;
import com.wujt.study.util.NettyUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class OrderServerProcessHandler extends SimpleChannelInboundHandler<RequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage requestMessage) throws Exception {
        log.info("msg={}",requestMessage.toString());
        Operation operation = requestMessage.getMessageBody();
        OperationResult operationResult = operation.execute();

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessageHeader(requestMessage.getMessageHeader());
        responseMessage.setMessageBody(operationResult);

        if (ctx.channel().isActive() && ctx.channel().isWritable()) {
            ctx.writeAndFlush(responseMessage);
        } else {
            log.error("not writable now, message dropped");
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String clientId = NettyUtils.clientId(ctx.channel());

        log.info("fire inactive clientId={}", clientId);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String clientId = NettyUtils.clientId(ctx.channel());
        log.info("fire exception clientId={},cause={}", clientId, cause);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        String clientId = NettyUtils.clientId(ctx.channel());
        log.info("fire channelRegistered clientId={}", clientId);
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        String clientId = NettyUtils.clientId(ctx.channel());
        log.info("fire channelUnregistered clientId={}", clientId);
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String clientId = NettyUtils.clientId(ctx.channel());
        log.info("fire channelActive clientId={}", clientId);
        super.channelActive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        String clientId = NettyUtils.clientId(ctx.channel());
        log.info("fire channelReadComplete clientId={}", clientId);
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        String clientId = NettyUtils.clientId(ctx.channel());
        log.info("fire userEventTriggered clientId={},event={}", clientId, evt);
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        String clientId = NettyUtils.clientId(ctx.channel());
        log.info("fire channelWritabilityChanged clientId={}", clientId);
        super.channelWritabilityChanged(ctx);
    }
}
