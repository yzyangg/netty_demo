package netty.hello;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/17 14:50
 */
public class HelloServer {
    public static void main(String[] args) {
        // 创建一个 ServerBootstrap 实例来引导和绑定服务器
        ServerBootstrap server = new ServerBootstrap()
                // 创建一个 NioEventLoopGroup 对象来处理事件的处理，如接受新的连接和读/写数据
                .group(new NioEventLoopGroup())
                // 指定使用 NioServerSocketChannel 类来实例化一个新的 Channel 对象来接受进来的连接
                .channel(NioServerSocketChannel.class)
                // 设置子处理器来处理接收的连接的 I/O 事件及数据
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        // 添加 StringDecoder 解码器，用于将字节数据解码为字符串
                        channel.pipeline().addLast(new StringDecoder());
                        // 添加 ChannelInboundHandlerAdapter 处理器，用于处理通道中传入的消息
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                // 处理通道中接收到的消息
                                System.out.println(msg);
                            }
                        });
                    }
                });// 绑定服务器到指定的端口号
        server.bind(8080);

    }
}