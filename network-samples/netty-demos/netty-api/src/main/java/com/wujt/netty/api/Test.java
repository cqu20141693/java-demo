package com.wujt.netty.api;

import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;

/**
 * @author gow
 * @date 2021/9/14
 */
public class Test {
    public static void main(String[] args) {
        int def = NettyRuntime.availableProcessors() * 2;
        int max = Math.max(1, SystemPropertyUtil
                .getInt("io.netty.eventLoopThreads", def));
        System.out.println();
    }
}
