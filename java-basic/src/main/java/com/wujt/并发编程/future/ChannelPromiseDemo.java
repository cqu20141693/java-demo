package com.wujt.并发编程.future;

import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.ReflectiveChannelFactory;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 实现原理： 通过 自定义一个线程组合一个轮询执行器；但一个任务提交到线程组；通过轮询获取取余的方式获取线程执行任务；
 * 在执行任务完成中需要
 *
 *
 * @author wujt
 */
public class ChannelPromiseDemo {
    public static void main(String[] args) {
        // ThreadPerTaskExecutor : 一个用于创建 FastThreadLocalThread 线程的执行器。
        ReflectiveChannelFactory<NioServerSocketChannel> channelFactory = new ReflectiveChannelFactory<>(NioServerSocketChannel.class);
        //NioEventLoopGroup -> MultithreadEventExecutorGroup
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        //NioEventLoop -> SingleThreadEventLoop -> SingleThreadEventExecutor -> AbstractScheduledEventExecutor
        EventLoop next = eventLoopGroup.next();
        NioServerSocketChannel serverSocketChannel = channelFactory.newChannel();
        DefaultChannelPromise defaultChannelPromise = new DefaultChannelPromise(serverSocketChannel, next);
        // channel 和 EventLoop 进行绑定
        next.register(defaultChannelPromise);


    }
}
