package com.wujt.file.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;

import java.io.RandomAccessFile;

/**
 * @author wujt
 */
@ChannelHandler.Sharable
public class FileServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        RandomAccessFile raf = null;
        long length = -1;
        try {
            raf = new RandomAccessFile(msg, "r");
            length = raf.length();
        } catch (Exception e) {
            ctx.writeAndFlush("ERR: " + e.getClass().getSimpleName() + ": " + e.getMessage() + '\n');
            return;
        } finally {
            if (length < 0 && raf != null) {
                raf.close();
            }
        }

        ctx.write("OK: " + raf.length() + '\n');
        if (ctx.pipeline().get(SslHandler.class) == null) {
            // 传输文件使用了 DefaultFileRegion 进行写入到 NioSocketChannel 中

            //减少两次 IO 的复制
            //第一次 IO：读取文件的时间从系统内存中拷贝到 jvm 内存中。
            //第二次 IO：从 jvm 内存中写入 Socket 时，再 Copy 到系统内存中。
            ctx.write(new DefaultFileRegion(raf.getChannel(), 0, length));
        } else {
            // SSL enabled - cannot use zero-copy file transfer.
            ctx.write(new ChunkedFile(raf));
        }
        ctx.writeAndFlush("\n");
    }
}
