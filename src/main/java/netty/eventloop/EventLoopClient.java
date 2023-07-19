package netty.eventloop;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/17 15:09
 */
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        // 创建一个 Bootstrap 实例来引导和连接客户端
        Channel channel = new Bootstrap()
                // 创建一个 NioEventLoopGroup 对象来处理事件的处理，如接受新的连接和读/写数据
                .group(new NioEventLoopGroup())
                // 指定使用 NioSocketChannel 类来实例化一个新的 Channel 对象来建立连接
                .channel(NioSocketChannel.class)
                // 设置处理器来处理连接的 I/O 事件及数据
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) {
                        // 添加 StringDecoder 解码器，用于将字节数据解码为字符串
                        channel.pipeline().addLast(new StringDecoder());
                    }
                })
                // 连接到服务器的指定地址和端口号
                .connect(new InetSocketAddress("localhost", 8080))
                // 等待连接完成
                .sync()
                // 获取连接的 Channel
                .channel();

        // 向服务器发送消息
//        channel.writeAndFlush("hello world");

        System.out.println(channel);
        System.out.println();

    }
}
