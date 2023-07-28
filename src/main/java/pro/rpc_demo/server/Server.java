package pro.rpc_demo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.SuppressForbidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import pro.rpc_demo.coder.RpcDecoder;
import pro.rpc_demo.coder.RpcEncoder;
import pro.rpc_demo.handler.ServerHandler;
import pro.rpc_demo.model.RpcRequest;
import pro.rpc_demo.model.RpcResponse;
import pro.rpc_demo.serial.JSONSerializer;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.imageio.spi.ServiceRegistry;
import java.net.InetSocketAddress;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/27 14:17
 */
@Slf4j
public class Server implements InitializingBean {

    private EventLoopGroup boss = null;
    private EventLoopGroup worker = null;

    @Resource
    private ServerHandler serverHandler;


    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    public void start() {
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new RpcEncoder(RpcResponse.class, new JSONSerializer()));
                        socketChannel.pipeline().addLast(new RpcDecoder(RpcRequest.class, new JSONSerializer()));
                        socketChannel.pipeline().addLast(serverHandler);
                    }
                });

    }

    public void bind(final ServerBootstrap serverBootstrap, int port) {

        serverBootstrap.bind(new InetSocketAddress(port)).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
                bind(serverBootstrap, port + 1);
            }
        });
    }

    @PreDestroy
    public void destroy() {
        boss.shutdownGracefully();
        worker.shutdownGracefully();
        log.info("关闭Netty");
    }
}
