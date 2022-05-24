package com.cc.ocpp;

import com.cc.ocpp.client.KeepaliveHandler;
import com.cc.ocpp.client.Session;
import com.cc.ocpp.network.codec.OCPPHandler;
import com.cc.ocpp.network.codec.ProtocolDecoder;
import com.cc.ocpp.network.codec.ProtocolEncoder;
import com.cc.ocpp.network.codec.ZDFrameDecoder;
import com.cc.ocpp.network.cp.config.CPServerProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.cc.ocpp.client.Utils.getDefaultLoginMessage;

@SpringBootApplication
public class OCPPClient implements CommandLineRunner {
    public Optional<Channel> getChannel() {
        return channels.keySet().stream().findFirst();
    }

    private Map<Channel, Session> channels = new ConcurrentHashMap<>();

    @Autowired
    private CPServerProperties properties;

    public static void main(String[] args) {
        SpringApplication.run(OCPPClient.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);

        bootstrap.option(NioChannelOption.CONNECT_TIMEOUT_MILLIS, 10 * 1000);

        NioEventLoopGroup group = new NioEventLoopGroup();

        try {

            bootstrap.group(group);

            LoggingHandler loggingHandler = new LoggingHandler(LogLevel.INFO);

            SslContextBuilder sslContextBuilder = SslContextBuilder.forClient();

            //下面这行，先直接信任自签证书，以避免没有看到ssl那节课程的同学运行不了；
            //学完ssl那节后，可以去掉下面这行代码，安装证书，安装方法参考课程，执行命令参考resources/ssl.txt里面
            sslContextBuilder.trustManager(InsecureTrustManagerFactory.INSTANCE);

            SslContext sslContext = sslContextBuilder.build();

            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();

                    pipeline.addLast(new IdleStateHandler(0, 0, 20, TimeUnit.SECONDS));
//                    pipeline.addLast(new ZDFrameDecoder());
//                    pipeline.addLast(new ProtocolDecoder());
                    pipeline.addLast(new ZDFrameDecoder());
                    pipeline.addLast(new ProtocolDecoder());
                    pipeline.addLast(new ProtocolEncoder());
//                    pipeline.addLast(loggingHandler);
                    pipeline.addLast(new KeepaliveHandler());
                    pipeline.addLast(new OCPPHandler(false,properties));
                }
            });

//            String host = "127.0.0.1";
//            String host = "118.24.224.99";
            ChannelFuture channelFuture = bootstrap.connect(properties.getPublicIp(), properties.getPublicPort());

            channelFuture.sync();

            //  发送登录消息
            Channel channel = channelFuture.channel();
            channels.put(channel, new Session());
            channel.writeAndFlush(getDefaultLoginMessage());

            channelFuture.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }
    }

}
