package pro.dubbo_rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/27 23:14
 */
public class Server {
    public static void startServer(String host, int port) {
        startServer0(host, port);
    }

    private static void startServer0(String host, int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            ChannelFuture channelFuture = server.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("编码器", new StringDecoder());
                            socketChannel.pipeline().addLast("解码器", new StringDecoder());
                            socketChannel.pipeline().addLast(new DubboServerHandler());
                        }
                    })
                    .bind(new InetSocketAddress(host, port));
            channelFuture.sync();
            System.out.println("服务器启动成功");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
