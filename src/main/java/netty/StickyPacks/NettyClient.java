package netty.StickyPacks;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/20 15:30
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new NettyClientHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    private static class NettyClientHandler extends ChannelInboundHandlerAdapter {
        private static final String MESSAGE = "Hello, Netty!\n";

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            for (int i = 0; i < 5; i++) {
                ctx.writeAndFlush(Unpooled.copiedBuffer(MESSAGE.getBytes()));
            }
        }
    }
}