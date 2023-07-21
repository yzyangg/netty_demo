package netty.ParseProtocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/20 15:56
 */
public class Parse {
    public static void main(String[] args) {
        final String LINE = "\r\n";
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ChannelFuture channelFuture = new Bootstrap().channel(NioSocketChannel.class)
                    .group(group)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    ByteBuf buffer = ctx.alloc().buffer();
                                    buffer.writeBytes("*3".getBytes());
                                    buffer.writeBytes(LINE.getBytes());
                                    buffer.writeBytes("$3".getBytes());
                                    buffer.writeBytes(LINE.getBytes());
                                    buffer.writeBytes("SET".getBytes());
                                    buffer.writeBytes(LINE.getBytes());
                                    buffer.writeBytes("$4".getBytes());
                                    buffer.writeBytes(LINE.getBytes());
                                    buffer.writeBytes("name".getBytes());
                                    buffer.writeBytes(LINE.getBytes());
                                    buffer.writeBytes("$5".getBytes());
                                    buffer.writeBytes(LINE.getBytes());
                                    buffer.writeBytes("yzyzy".getBytes());
                                    buffer.writeBytes(LINE.getBytes());
                                    ctx.writeAndFlush(buffer);
                                }

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    ByteBuf buf = (ByteBuf) msg;
                                    String s = buf.toString(Charset.defaultCharset());
                                    System.out.println(s     );
                                }
                            });
                        }

                    }).connect(new InetSocketAddress("localhost", 6379))
                    .sync();


            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
