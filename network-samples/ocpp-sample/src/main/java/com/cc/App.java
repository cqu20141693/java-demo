package com.cc;

import com.cc.client.KeepaliveHandler;
import com.cc.client.ProtocolDecoder;
import com.cc.client.ProtocolEncoder;
import com.cc.client.Session;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.cc.client.Utils.getDefaultLoginMessage;

@SpringBootApplication
public class App implements CommandLineRunner {
    public Optional<Channel> getChannel() {
        return channels.keySet().stream().findFirst();
    }

    private Map<Channel, Session> channels = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
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

                    pipeline.addLast(new IdleStateHandler(0, 0, 10, TimeUnit.MINUTES));
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 11, 2, 2, 0));
                    pipeline.addLast(new ProtocolDecoder());
                    pipeline.addLast(new ProtocolEncoder());
                    pipeline.addLast(loggingHandler);
                    pipeline.addLast(new KeepaliveHandler());
                }
            });

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9000);

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
