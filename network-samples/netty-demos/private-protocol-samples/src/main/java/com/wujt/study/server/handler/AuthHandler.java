package com.wujt.study.server.handler;

import com.wujt.study.common.Operation;
import com.wujt.study.common.RequestMessage;
import com.wujt.study.common.auth.AuthOperation;
import com.wujt.study.common.auth.AuthOperationResult;
import com.wujt.study.util.NettyUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@ChannelHandler.Sharable
public class AuthHandler extends SimpleChannelInboundHandler<RequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage msg) throws Exception {
        try {
            String uuid = UUID.randomUUID().toString();
            NettyUtils.clientId(ctx.channel(), uuid);
            Operation operation = msg.getMessageBody();
            if (operation instanceof AuthOperation) {
                AuthOperation authOperation = (AuthOperation) operation;
                AuthOperationResult authOperationResult = authOperation.execute();
                if (authOperationResult.isPassAuth()) {
                    log.info("pass auth");
                } else {
                    log.error("fail to auth");
                    ctx.close();
                }
            } else {
                log.error("expect first msg is auth");
                ctx.close();
            }
        } catch (Exception e) {
            log.error("exception happen for: " + e.getMessage(), e);
            ctx.close();
        } finally {
            ctx.pipeline().remove(this);
        }

    }
}
