package io.websocket;

import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutorGroup;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/26 9:56
 */
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        System.out.printf("收到客户端消息: %s\n", textWebSocketFrame.text());

        channelHandlerContext.channel()
                .writeAndFlush(new TextWebSocketFrame(simpleDateFormat.format(new Date()) + " " + textWebSocketFrame.text()));

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("handlerAdded: %s\n", ctx.channel().id().asLongText());
        System.out.printf("handlerAdded: %s\n", ctx.channel().id().asShortText());
    }
}
