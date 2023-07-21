package netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/19 18:11
 */
@Slf4j
public class EchoServer {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            ChannelFuture channelFuture = server.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new EchoServerHandler())
                    .bind(new InetSocketAddress("localhost", 8080));

            channelFuture.sync();
            System.out.println("服务器启动成功");


            // 阻塞当前线程，直到服务器关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.info("InterruptedException: " + e.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
