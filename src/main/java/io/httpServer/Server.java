package io.httpServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import javax.swing.text.html.Option;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/24 22:24
 */
public class Server {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true) // 保持连接
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast("HttpServerCodec", new HttpServerCodec());
                    socketChannel.pipeline().addLast("MyHandler", new SimpleChannelInboundHandler<HttpObject>() {
                        @Override
                        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                            System.out.printf("handlerAdded 被调用" + ctx.channel().remoteAddress() + "被调用");
                        }

                        @Override
                        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
                            System.out.printf("handlerRemoved 被调用" + ctx.channel().remoteAddress() + "被调用");
                        }

                        @Override
                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
                            if (httpObject instanceof HttpObject) {
//                                HttpRequest httpRequest = (DefaultHttpRequest) httpObject;
//                                String uri = httpRequest.uri();

                                ChannelPipeline pipeline = channelHandlerContext.pipeline();
                                System.out.println("pipeline hashcode = " + pipeline.hashCode());
                                Channel channel = pipeline.channel();
                                System.out.printf("channel hashcode = " + channel.hashCode());

                                System.out.println("msg 类型 = " + httpObject.getClass());
                                System.out.println("客户端地址 = " + channelHandlerContext.channel().remoteAddress());
                                ByteBuf byteBuf = Unpooled.copiedBuffer("hello Server", CharsetUtil.UTF_8);

                                //构造返回对象
                                DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);

                                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

                                channelHandlerContext.writeAndFlush(response);

                            }

                        }
                    });
                }
            });

            ChannelFuture channelFuture = server.bind(new InetSocketAddress("localhost", 8080)).sync();

            System.out.println("服务器启动成功");


            // 阻塞当前线程，直到服务器关闭
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
