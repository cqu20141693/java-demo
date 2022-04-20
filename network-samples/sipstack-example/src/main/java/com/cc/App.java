package com.cc;

import com.cc.link.handler.SipConnections;
import com.cc.link.handler.SipHandler;
import com.cc.link.handler.SipTXHandler;
import com.cc.link.model.SipConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.sipstack.netty.codec.sip.SipMessageDatagramDecoder;
import io.sipstack.netty.codec.sip.SipMessageEncoder;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author gow
 * @date 2022/2/15
 */
@SpringBootApplication
public class App implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }

    @Autowired
    private SipHandler sipHandler;
    @Autowired
    private SipConnections sipConnections;

    @Override
    public void run(String... args) throws Exception {
        SipConfig sipConfig = new SipConfig();
        sipConfig.setPort(5060);
        EventLoopGroup udpGroup = new NioEventLoopGroup(1);
        final Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(udpGroup)
                    .channel(NioDatagramChannel.class)
                    .handler(new ChannelInitializer<DatagramChannel>() {
                        @Override
                        protected void initChannel(final DatagramChannel ch) throws Exception {
                            final ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("decoder", new SipMessageDatagramDecoder());
                            pipeline.addLast("encoder", new SipMessageEncoder());
                            pipeline.addLast("txHandler", new SipTXHandler());
                            pipeline.addLast("handler", sipHandler);
                        }
                    });
            final InetSocketAddress socketAddress = new InetSocketAddress(sipConfig.getHost(), sipConfig.getPort());
            Channel channel = bootstrap.bind(socketAddress).sync().channel();
            sipConnections.setChannel(channel);
            channel.closeFuture().await();
        } finally {
            //优雅的关闭释放内存
            udpGroup.shutdownGracefully();
        }
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        // 支持中文编码
        restTemplate.getMessageConverters().set(1,
                new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;

    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);//单位为ms
        factory.setConnectTimeout(5000);//单位为ms
        return factory;
    }
}
