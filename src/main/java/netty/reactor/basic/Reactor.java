package netty.reactor.basic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/21 23:57
 */
public class Reactor implements Runnable {
    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;

    public Reactor(int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open(); // 打开通道
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false); // selector模式下，所有通道必须是非阻塞的

        // reactor是入口，负责接收客户端的连接
        SelectionKey sk = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 附加新连接处理器到新的SelectionKey上
        sk.attach(new Acceptor(selector, serverSocketChannel));
    }


    @Override
    public void run() {
        while (!Thread.interrupted()) {
            // 阻塞等待就绪的Channel，这是关键点之一
            try {
                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    // 分发事件
                    SelectionKey selectionKey = iterator.next();
                    dispatch(selectionKey);
                    iterator.remove();
                }
                selectionKeys.clear();


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void dispatch(SelectionKey k) {
        Runnable r = (Runnable) k.attachment();
        if (r != null) {
            long l = System.currentTimeMillis();
            r.run();
            System.out.println("耗时：" + (System.currentTimeMillis() - l));

        }
    }
}
