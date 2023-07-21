package netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/19 17:55
 */
@Slf4j
public class EchoClient {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new EchoClientHandler());

            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("localhost", 8080)).sync();
            Channel channel = channelFuture.channel();
            System.out.printf("客户端启动成功\n 请输入要发送的数据");
            ByteBuffer buffer = ByteBuffer.allocate(32);
            try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
                String userInput;
                while ((userInput = stdIn.readLine()) != null) {
                    ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(10);
                    byteBuf.writeBytes(userInput.getBytes());
                    channel.writeAndFlush(byteBuf);
                }
            }
        } catch (UnknownHostException e) {
            log.error("UnknownHostException: " + e.getMessage());
        } catch (IOException e) {
            log.error("IOException: " + e.getMessage());
        } catch (InterruptedException e) {
            log.error("InterruptedException: " + e.getMessage());
        } finally {
            group.shutdownGracefully();
        }
    }
}
