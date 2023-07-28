package io.chat;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/25 17:35
 */
public class ChatGroupServerHandler extends SimpleChannelInboundHandler<String> {
    /**
     * 全局事件处理器，是个单例
     */
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static Map<String, Channel> channelMap = new HashMap<>();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 当客户端连接服务器完成就会触发该方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户端加入聊天的信息推送给其他在线的客户端
        channelGroup.writeAndFlush(simpleDateFormat.format(new Date()) + "[客户端]" + channel.remoteAddress() + "加入聊天\n");
        channelGroup.add(channel);


        channelMap.put(channel.remoteAddress().toString(), channel);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.printf(simpleDateFormat.format(new Date()) + "[客户端]" + ctx.channel().remoteAddress() + "上线了\n");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//        System.out.println(msg);
//        String port = msg.substring(msg.indexOf(":") + 1, msg.indexOf(":") + 5);
//        System.out.printf("port: %s\n", port);
//        String uri = "/127.0.0.1:" + port;
//        channelMap.get(uri).writeAndFlush(ctx.channel().remoteAddress() + "说: " + msg + "\n");
        channelGroup.forEach(channel -> {
            if (channel != ctx.channel()) {
                channel.writeAndFlush("[客户端]" + channel.remoteAddress() + "收到了: " + msg + "\n");
            } else {
                channel.writeAndFlush("[自己]发送了消息: " + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.printf("[客户端]" + ctx.channel().remoteAddress() + "异常\n");
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.printf(simpleDateFormat.format(new Date()) + "[客户端]" + ctx.channel().remoteAddress() + "下线了\n");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush("[客户端]" + ctx.channel().remoteAddress() + "离开了\n");
        channelGroup.remove(ctx.channel());
        System.out.printf("当前在线人数: %d\n", channelGroup.size());
    }
}
