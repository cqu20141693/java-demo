package com.wujt.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @author wujt
 */
public class UdpClient {
    /**
     * 客户端端口号
     */
    public static final int PORT = 8080;

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        bootstrap.group(eventExecutors)
                .channel(NioDatagramChannel.class)
                // 指定允许广播
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new UdpClientDecoder())
                                .addLast(new UdpClientHandler());
                    }
                })
        ;

        // 监听固定的端口
        ChannelFuture channelFuture = bootstrap.bind(PORT).syncUninterruptibly();
        channelFuture.channel().closeFuture().syncUninterruptibly();

        eventExecutors.shutdownGracefully();
    }

}
