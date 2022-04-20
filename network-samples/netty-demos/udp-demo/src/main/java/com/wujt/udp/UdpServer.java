package com.wujt.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author wujt
 */
public class UdpServer {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap serverBootstrap = new Bootstrap();
        EventLoopGroup eventExecutors = new NioEventLoopGroup();

        serverBootstrap.group(eventExecutors)
                .channel(NioDatagramChannel.class)
                // 支持广播
                .option(ChannelOption.SO_BROADCAST, true)
                // 添加编码器
                .handler(new UdpServerEncoder());


        // 循环广播内容-5S钟推送一次服务器时间
        // 这个端口绑定的是0
        Channel channel = serverBootstrap.bind(0).syncUninterruptibly().channel();

        for(int i = 0; i < 10000; i++) {
            MessageBean messageBean = new MessageBean();
            messageBean.setTime(LocalDateTime.now().toString());
            channel.writeAndFlush(messageBean);
            System.out.println("[Server] broadcast: " + messageBean);
            TimeUnit.SECONDS.sleep(5);
        }

        //close
        channel.closeFuture().syncUninterruptibly();
        eventExecutors.shutdownGracefully();
    }
}
