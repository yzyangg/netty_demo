package pro.rpc_demo.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import lombok.extern.slf4j.Slf4j;
import pro.rpc_demo.model.RpcRequest;
import pro.rpc_demo.model.RpcResponse;
import pro.rpc_demo.coder.RpcDecoder;
import pro.rpc_demo.coder.RpcEncoder;
import pro.rpc_demo.handler.ClientHandler;
import pro.rpc_demo.serial.JSONSerializer;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/27 14:17
 */
@Slf4j

public class Client {
    private EventLoopGroup eventLoopGroup;
    private Channel channel;
    private ClientHandler clientHandler;
    private String host;
    private Integer port;
    private static final int MAX_RETRY = 5;

    public Client(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        clientHandler = new ClientHandler();
        eventLoopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new RpcEncoder(RpcRequest.class, new JSONSerializer()));
                        socketChannel.pipeline().addLast(new RpcDecoder(RpcResponse.class, new JSONSerializer()));
                        socketChannel.pipeline().addLast(clientHandler);
                    }
                });

        connect(bootstrap, host, port, MAX_RETRY);
    }

    /**
     * 连接服务端
     *
     * @param bootstrap
     * @param host
     * @param port
     * @param retry
     */
    public void connect(Bootstrap bootstrap, String host, int port, int retry) {
        ChannelFuture channelFuture;
        try {
            channelFuture = bootstrap.connect(host, port).addListener(future -> {
                if (future.isSuccess()) {
                    log.info("连接服务端成功");
                } else if (retry == 0) {
                    log.error("重试次数已用完，放弃连接");
                } else {
                    int order = (MAX_RETRY - retry) + 1;
                    // 本次重连的间隔
                    int delay = 1 << order;
                    log.error("{}: 连接失败，第{}次重连……", Thread.currentThread().getName(), order);
                    bootstrap.config().group().schedule(() -> {

                        connect(bootstrap, host, port, retry - 1);

                    }, delay, TimeUnit.SECONDS);
                }
            }).sync();
            channel = channelFuture.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public RpcResponse send(final RpcRequest rpcRequest) {
        try {
            // 向服务端发送数据，直到发送成功
            channel.writeAndFlush(rpcRequest).await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return clientHandler.getResponse(rpcRequest.getRequestId());
    }

    @PreDestroy
    public void close() {
        eventLoopGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
    }
}
