package com.cc.ocpp;

import com.cc.netwok.handler.ProtocolIdleStateHandler;
import com.cc.netwok.handler.UserEventHandler;
import com.cc.ocpp.network.codec.OCPPHandler;
import com.cc.ocpp.network.codec.ProtocolDecoder;
import com.cc.ocpp.network.codec.ProtocolEncoder;
import com.cc.ocpp.network.codec.ZDFrameDecoder;
import com.cc.ocpp.network.cp.config.CPServerProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

/**
 * 充电桩服务端服务
 * wcc 2022/5/14
 */
@SpringBootApplication
@Slf4j
public class OCPPServer implements CommandLineRunner {

    @Autowired
    private CPServerProperties properties;

    public static void main(String[] args) {
        SpringApplication.run(OCPPServer.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //  服务端启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup work = new NioEventLoopGroup();

        // 配置服务端
        serverBootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .option(NioChannelOption.SO_BACKLOG, 1024)
                // 设置连接超时
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .handler(new LoggingHandler(LogLevel.INFO))
                // child config
                .childHandler(new OcppInitializer())
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(1024 * 8, 1024 * 16))
                .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                // mqtt 不使用tcp keep live
                .childOption(ChannelOption.SO_KEEPALIVE, false)
                .childOption(ChannelOption.TCP_NODELAY, true);
        try {
            ChannelFuture f = serverBootstrap.bind(properties.getIp(), properties.getPort());
            f.sync();
        } catch (InterruptedException ex) {
            log.error("bind server ", ex);
        }
    }

    class OcppInitializer extends ChannelInitializer<NioSocketChannel> {

        @Override
        protected void initChannel(NioSocketChannel ch) {
            LoggingHandler loggingHandler = new LoggingHandler(LogLevel.INFO);
            ChannelPipeline pipeline = ch.pipeline();

            pipeline.addLast("delimiterFrame", new ZDFrameDecoder());
            pipeline.addLast("decoder", new ProtocolDecoder());
            pipeline.addLast("idleStateHandler", new ProtocolIdleStateHandler(0, 0, 120, TimeUnit.SECONDS));
            pipeline.addLast("userEventHandler", new UserEventHandler());
            pipeline.addLast("encoder", new ProtocolEncoder());
//                    pipeline.addLast(loggingHandler);
            pipeline.addLast("handler", new OCPPHandler(true,properties));
        }
    }
}
