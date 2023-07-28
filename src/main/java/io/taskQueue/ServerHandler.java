package io.taskQueue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/24 15:59
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("服务端收到客户端连接\n");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String name = Thread.currentThread().getName();
        System.out.printf("当前线程..%s\n", name);
        Channel channel = ctx.channel();
        System.out.printf("当前channel..%s\n", channel);
//        System.out.printf("服务端收到客户端消息，异步执行任务...\n");
//        ctx.channel().eventLoop().execute(() -> {
//            System.out.printf("执行耗时任务ing...\n");
//            try {
//                Thread.sleep(1000 * 3);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            ByteBuf byteBuf = (ByteBuf) msg;
//            String s = byteBuf.toString(Charset.defaultCharset());
//            System.out.printf("服务端收到客户端消息: %s\n", s);
//        });
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("服务端收到客户端消息读取完毕\n");
    }

}
