package netty.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/18 11:38
 */
@Slf4j
public class MyClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            ChannelFuture channelFuture = bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new MyClientHandler());
                        }
                    }).connect("localhost", 8080);
            System.out.println("客户端 is ready!");
            // 对关闭通道进行监听

//            // 阻塞住当前线程，等待链接（nio线程）建立好
//            channelFuture.sync();
//            Channel channel = channelFuture.channel();
//            channel.writeAndFlush("hello world");

            // 异步监听，不阻塞当前线程，等待链接（nio线程）建立好，然后执行回调函数
            channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
                Channel channel = channelFuture1.channel();
                log.debug("{}", channel);
                // 发送消息
                channel.writeAndFlush("hello world");
            });

        } finally {
            eventExecutors.shutdownGracefully();
        }

    }
}
