package pro.chat.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import pro.chat.message.LoginRequestMessage;
import pro.chat.message.LoginResponseMessage;
import pro.chat.protocol.MessageCodecSharable;
import pro.chat.protocol.ProtocolFrameDecoder;
import pro.chat.server.service.UserServiceFactory;

import java.net.InetSocketAddress;

/**
 * 聊天--服务端
 */
@Slf4j
public class ChatServer {


    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGIN_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {

                @Override
                protected void initChannel(NioSocketChannel channel) throws Exception {
                    channel.pipeline().addLast(new ProtocolFrameDecoder());
                    channel.pipeline().addLast(LOGIN_HANDLER);
                    channel.pipeline().addLast(MESSAGE_CODEC);
                    channel.pipeline().addLast(new SimpleChannelInboundHandler<LoginRequestMessage>() {

                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
                            String username = msg.getUsername();
                            String password = msg.getPassword();
                            boolean login = UserServiceFactory.getUserService().login(username, password);
                            LoginResponseMessage responseMessage;
                            if (login) {
                                log.debug("登录成功");
                                responseMessage = new LoginResponseMessage(true, "登录成功");
                            } else {
                                log.debug("登录失败");
                                responseMessage = new LoginResponseMessage(false, "用户名或密码错误");
                            }
                            ctx.writeAndFlush(responseMessage);
                        }
                    });
                }
            });
            Channel channel = serverBootstrap.bind(new InetSocketAddress(8080)).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }


}
