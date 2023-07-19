package netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.SocketAddress;
import java.nio.charset.Charset;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/18 11:38
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        System.out.printf("客户端[%s]发送的消息是：%s\n", socketAddress, byteBuf.toString(Charset.defaultCharset()));

        ctx.channel().eventLoop().execute(() -> {
            try {
                System.out.println("长时间执行业务逻辑");
                Thread.sleep(10 * 1000);
                System.out.println("业务逻辑执行完毕");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 写入缓冲并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client", Charset.defaultCharset()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
