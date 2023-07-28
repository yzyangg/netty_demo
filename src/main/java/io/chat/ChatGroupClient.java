package io.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/25 17:49
 */
public class ChatGroupClient {
    private int port;
    private String host;

    public ChatGroupClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ChannelFuture channelFuture = new Bootstrap().channel(NioSocketChannel.class)
                    .group(group)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("解码器", new StringDecoder());
                            socketChannel.pipeline().addLast("编码器", new StringEncoder());
                            socketChannel.pipeline().addLast(new ChatGroupClientHandler());
                        }
                    }).connect(new InetSocketAddress(host, port))
                    .sync();
            Scanner sc = new Scanner(System.in);
            while (sc.hasNextLine()) {
                String msg = sc.nextLine();
                channelFuture.channel().writeAndFlush(msg);
            }


            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new ChatGroupClient(8080, "localhost").run();
    }
}
