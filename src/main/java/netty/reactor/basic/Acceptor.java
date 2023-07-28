package netty.reactor.basic;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/21 17:42
 */
public class Acceptor implements Runnable {
    private final Selector selector;

    private final ServerSocketChannel serverSocketChannel;

    public Acceptor(Selector selector, ServerSocketChannel serverSocketChannel) {
        this.selector = selector;
        this.serverSocketChannel = serverSocketChannel;
    }


    @Override
    public void run() {
        SocketChannel socketChannel;
        try {
            // 接收客户端连接请求
            socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                System.out.printf("收到来自%s的连接", socketChannel.getRemoteAddress());
                // 客户端通道传递给Handler处理，Handler负责接下来的事件处理
                new AsyncHandler(selector, socketChannel);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
