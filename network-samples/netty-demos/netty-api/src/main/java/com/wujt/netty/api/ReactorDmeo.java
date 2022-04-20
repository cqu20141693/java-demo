package com.wujt.netty.api;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 常见的Reactor线程模型有三种，分别如下：
 * Reactor单线程模型；
 * Reactor多线程模型；
 * 主从Reactor多线程模型；
 * 参考
 * https://blog.csdn.net/u010623927/article/details/87948212
 *
 *
 * 源码解读
 * @author wujt
 */
public class ReactorDmeo {

    public static void main(String[] args) {


    }

    /**
     * Netty单线程模型服务端代码示例
     *
     * @param port
     */
    public void bind(int port) {

        EventLoopGroup reactorGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            // Acceptor 和IO 使用同一个线程
            b.group(reactorGroup, reactorGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("http-codec", new HttpServerCodec());
                            ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                            //添加业务处理器
                        }
                    });
            // 进入AbstractBootstrap.bind()
            Channel ch = b.bind(port).sync().channel();
            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reactorGroup.shutdownGracefully();
        }
    }

    /**
     * Netty多线程模型代码
     *
     * @param port
     */
    public void bind2(int port) {
        EventLoopGroup acceptorGroup = new NioEventLoopGroup(1);
        // 默认cpu核心数
        NioEventLoopGroup ioGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            // acceptor 使用单线程进行处理新连接请求，IO处理使用线程池处理
            b.group(acceptorGroup, ioGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("http-codec", new HttpServerCodec());
                            ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                            //后面代码省略
                        }
                    });

            Channel ch = b.bind(port).sync().channel();
            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            acceptorGroup.shutdownGracefully();
            ioGroup.shutdownGracefully();
        }
    }

    /**
     * Netty主从线程模型代码
     *
     * @param port
     */
    public void bind3(int port) {
        // acceptor 和io 都使用线程池
        EventLoopGroup acceptorGroup = new NioEventLoopGroup();
        NioEventLoopGroup ioGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(acceptorGroup, ioGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("http-codec", new HttpServerCodec());
                            ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                            //后面代码省略
                        }
                    });

            Channel ch = b.bind(port).sync().channel();
            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            acceptorGroup.shutdownGracefully();
            ioGroup.shutdownGracefully();
        }
    }

}
