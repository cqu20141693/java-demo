package com.wujt.server.netty;

import com.cc.netwok.handler.ProtocolIdleStateHandler;
import com.cc.netwok.handler.UserEventHandler;
import com.wujt.config.MqttProtocolConfig;
import com.wujt.server.netty.handler.MqttProtocolHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wujt
 */
@Component
@Slf4j
public class MqttBrokerAcceptor {
    /**
     * 监听的事件循环
     */
    private EventLoopGroup bossGroup;
    /**
     * 工作的事件循环
     */
    private EventLoopGroup workerGroup;

    private final MqttProtocolConfig config;
    private final MqttProtocolHandler handler;

    @Autowired
    public MqttBrokerAcceptor(MqttProtocolConfig configuration
            , MqttProtocolHandler mqttProtocolHandler) {
        this.config = configuration;
        this.handler = mqttProtocolHandler;
    }

    public void initialize() {
        bossGroup = new NioEventLoopGroup(config.getBossGroupSize());
        workerGroup = new NioEventLoopGroup(config.getWorkerGroupSize());
        initializePlainTCPTransport();
        if (config.isEnableWebsocket()) {
            initializeWebSocketTransport();
        }
    }

    private void initializeWebSocketTransport() {
        String host = config.getHost();
        int port = config.getPort() + 1000;
        initFactory(host, port, new AbstractPipelineInitializer() {
            @Override
            void init(ChannelPipeline pipeline) throws Exception {
                pipeline.addLast(new HttpServerCodec());
                pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                pipeline.addLast("webSocketHandler",
                        new WebSocketServerProtocolHandler("/mqtt", "mqtt,mqtt.5"));
                pipeline.addLast("ws2bytebufDecoder", new WebSocketFrameToByteBufDecoder());
                pipeline.addLast("bytebuf2wsEncoder", new ByteBufToWebSocketFrameEncoder());
                pipelineInit(pipeline);
            }
        });
        log.info("Started websocket on host: {}, port {}", host, port);
    }

    private void pipelineInit(ChannelPipeline pipeline) {
        pipeline.addLast("decoder", new MqttDecoder());
        pipeline.addFirst("idleStateHandler", new ProtocolIdleStateHandler(0, 0, 120, TimeUnit.SECONDS));
        pipeline.addAfter("idleStateHandler", "idleEventHandler", new UserEventHandler());
        pipeline.addLast("encoder", MqttEncoder.INSTANCE);
        pipeline.addLast("handler", handler);
    }


    private void initializePlainTCPTransport() {
        String host = config.getHost();
        int port = config.getPort();
        initFactory(host, port, new AbstractPipelineInitializer() {
            @Override
            void init(ChannelPipeline pipeline) {
                pipelineInit(pipeline);
            }
        });
        log.info("Started TCP on host: {}, port {}", host, port);
    }

    private void initFactory(String host, int port, final AbstractPipelineInitializer pipeliner) {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        try {
                            pipeliner.init(pipeline);
                        } catch (Throwable th) {
                            log.error("Severe error during pipeline creation", th);
                            throw th;
                        }
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)
                // 设置写Buffer的水位
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(1024 * 8, 1024 * 16))
                .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                // mqtt 不使用tcp keep live
                .childOption(ChannelOption.SO_KEEPALIVE, false)
                .childOption(ChannelOption.TCP_NODELAY, true);
        try {
            ChannelFuture f = b.bind(host, port);
            f.sync();
        } catch (InterruptedException ex) {
            log.error("bind server ", ex);
        }
    }

    static class WebSocketFrameToByteBufDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {

        @Override
        protected void decode(ChannelHandlerContext chc, BinaryWebSocketFrame frame, List<Object> out)
                throws Exception {
            // convert the frame to a ByteBuf
            ByteBuf bb = frame.content();
            // System.out.println("WebSocketFrameToByteBufDecoder decode - " +
            // ByteBufUtil.hexDump(bb));
            bb.retain();
            out.add(bb);
        }
    }

    static class ByteBufToWebSocketFrameEncoder extends MessageToMessageEncoder<ByteBuf> {

        @Override
        protected void encode(ChannelHandlerContext chc, ByteBuf bb, List<Object> out) throws Exception {
            // convert the ByteBuf to a WebSocketFrame
            BinaryWebSocketFrame result = new BinaryWebSocketFrame();
            // System.out.println("ByteBufToWebSocketFrameEncoder encode - " +
            // ByteBufUtil.hexDump(bb));
            result.content().writeBytes(bb);
            out.add(result);
        }
    }

    abstract class AbstractPipelineInitializer {
        /**
         * netty pipeline 初始化
         *
         * @param pipeline 输入的pipeline对象
         * @throws Exception
         */
        abstract void init(ChannelPipeline pipeline) throws Exception;
    }

    public void close() {
        if (workerGroup == null) {
            throw new IllegalStateException("Invoked close on an Acceptor that wasn't initialized");
        }
        if (bossGroup == null) {
            throw new IllegalStateException("Invoked close on an Acceptor that wasn't initialized");
        }
        Future workerWaiter = workerGroup.shutdownGracefully();
        Future bossWaiter = bossGroup.shutdownGracefully();

        try {
            workerWaiter.await(1000);
        } catch (InterruptedException iex) {
            throw new IllegalStateException(iex);
        }

        try {
            bossWaiter.await(1000);
        } catch (InterruptedException iex) {
            throw new IllegalStateException(iex);
        }
    }
}
