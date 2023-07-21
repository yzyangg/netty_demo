package netty.ParseProtocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import netty.echo.EchoServerHandler;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/20 16:11
 */
@Slf4j
public class TestHttp {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            ChannelFuture channelFuture = server.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            socketChannel.pipeline().addLast(new HttpServerCodec());
                            socketChannel.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
                                    log.debug(msg.uri());
                                    // 响应对象
                                    DefaultFullHttpResponse response =
                                            new DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK);
                                    String s = "<h1>hello world</h1>";
                                    response.content().writeBytes(s.getBytes(Charset.defaultCharset()));
                                    response.headers().set("Content-Type", "text/html;charset=utf-8");
                                    response.headers().set("Content-Length", s.getBytes().length);
                                    ctx.writeAndFlush(response);

                                }
                            });
                        }
                    })
                    .bind(new InetSocketAddress("localhost", 8080));
            channelFuture.sync();

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
