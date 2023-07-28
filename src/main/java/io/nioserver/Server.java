package io.nioserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/23 14:41
 */
public class Server {
    public static void main(String[] args) throws IOException {
        new Server().startServer();

    }

    public void startServer() throws IOException {
        Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.bind(new InetSocketAddress("localhost", 8080));

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.printf("服务器启动成功！");

        while (true) {
            int select = selector.select(1000);
            if (select == 0) {
                System.out.println("等待消息中...");
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                if (key.isAcceptable()) {
                    System.out.printf("有新的客户端连接！%s\n", key.channel().toString());
                    handleAccept(key, selector);
                }

                if (key.isReadable()) {
                    System.out.printf("有客户端发送消息！%s\n", key.channel().toString());
                    handleRead(key, selector);
                }
                iterator.remove();
            }


        }
    }

    public void handleAccept(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        SocketChannel clientChannel = serverSocketChannel.accept();

        clientChannel.configureBlocking(false);

        clientChannel.register(selector, SelectionKey.OP_READ);
        System.out.printf("客户端连接成功！，客户端地址：%s\n", clientChannel.getRemoteAddress().toString().substring(1));
    }

    public void handleRead(SelectionKey key, Selector selector) throws IOException {
        SocketChannel clientChannel = ((SocketChannel) key.channel());

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        int read = clientChannel.read(byteBuffer);

        if (read == -1) {
            clientChannel.close();
            key.cancel();
            System.out.printf("客户端断开连接！，客户端地址：%s\n", clientChannel.getRemoteAddress().toString().substring(1));
        }

        byteBuffer.flip();

        byte[] bytes = new byte[byteBuffer.remaining()];

        byteBuffer.get(bytes);

        System.out.printf("收到客户端消息：%s\n", new String(bytes));

        clientChannel.write(ByteBuffer.wrap("hello,client".getBytes()));

        byteBuffer.clear();
    }

}
