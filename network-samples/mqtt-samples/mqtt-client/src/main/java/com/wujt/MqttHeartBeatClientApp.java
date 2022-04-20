package com.wujt;

import com.alibaba.fastjson.JSONObject;
import com.wujt.config.DevConfig;
import com.wujt.config.MqttConfig;
import com.wujt.handler.MqttHeartBeatClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

/**
 * @Author jiangtaoW
 * @Date 2019/10/10
 * @Version 1.0
 * @Description : mqtt 心跳客户端
 */
@SpringBootApplication
@Slf4j
public class MqttHeartBeatClientApp implements CommandLineRunner {
    private static final String HOST = System.getProperty("host", "127.0.0.1");
    private static final int PORT = Integer.parseInt(System.getProperty("port", "1883"));
    private static final String CLIENT_ID = System.getProperty("clientId", "guestClient");
    private static final String USER_NAME = System.getProperty("userName", "guest");
    private static final String PASSWORD = System.getProperty("password", "guest");

    @Autowired
    private MqttConfig mqttConfig;

    @Autowired
    private DevConfig devConfig;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MqttHeartBeatClientApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
      //  log.info("mqtt={},dev={}", JSONObject.toJSONString(mqttConfig), JSONObject.toJSONString(devConfig));
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast("encoder", MqttEncoder.INSTANCE);
                    ch.pipeline().addLast("decoder", new MqttDecoder());
                    ch.pipeline().addLast("heartBeatHandler", new IdleStateHandler(0, 20, 0, TimeUnit.SECONDS));
                    ch.pipeline().addLast("handler", new MqttHeartBeatClientHandler(mqttConfig.getClientId(), mqttConfig.getUserName(), mqttConfig.getPassword()));
                }
            });

            ChannelFuture f = b.connect(mqttConfig.getHost(), mqttConfig.getPort()).sync();
            System.out.println("Client connected");
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
