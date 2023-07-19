package netty.pipeline;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/18 16:35
 */
@Slf4j
public class PipeLineClient {
    public static void main(String[] args) {
        String host = "localhost";  // 服务器主机地址
        int port = 8080;  // 服务器端口号

        // 创建一个线程组，用于处理IO操作
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            // 创建Bootstrap对象，用于启动客户端
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("decoder", new StringDecoder());  // 字符串解码器
                            pipeline.addLast("encoder", new StringEncoder());  // 字符串编码器
                            pipeline.addLast("handler", new ClientHandler());  // 客户端消息处理器
                        }
                    });

            // 连接到服务器
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            Channel channel = channelFuture.channel();
            log.debug(("Connected to server: " + host + ":" + port));

            // 创建键盘输入对象
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();  // 读取键盘输入的消息
                if (message.equals("exit")) {
                    channel.close();  // 关闭通道
                    break;
                }
                channel.writeAndFlush(message);  // 发送消息到服务器
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭线程组
            group.shutdownGracefully();
        }
    }

    private static class ClientHandler extends SimpleChannelInboundHandler<String> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            log.debug("Received message from server: " + msg);
        }
    }
}
