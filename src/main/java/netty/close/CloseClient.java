package netty.close;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/18 15:05
 */
public class CloseClient {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channelFuture = new Bootstrap().group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new StringDecoder());
                    }
                }).connect("localhost", 8080);
        channelFuture.sync();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner sc = new Scanner(System.in);
                while (true) {
                    String s = sc.nextLine();
                    if (s.equals("exit")) {
                        channelFuture.channel().close();
                        break;
                    } else {
                        channelFuture.channel().writeAndFlush(s);
                    }
                }
            }
        }).start();
    }
}
