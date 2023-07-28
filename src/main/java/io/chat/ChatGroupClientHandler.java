package io.chat;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/25 17:57
 */
public class ChatGroupClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        System.out.printf("什么时候触发？？\n");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("我是[客户端],%s\n", ctx.channel().localAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.printf("收到服务端消息: %s\n", s.trim());
    }
}
