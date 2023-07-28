package io.nioserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/23 15:00
 */
public class Client {
    public static void main(String[] args) throws IOException {
        new Client().startClient();
    }

    public void startClient() throws IOException {
        // 创建SocketChannel，并连接到服务器
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost", 8080));

        // 等待连接完成
        while (!socketChannel.finishConnect()) {
            // 可以做一些其他操作
            System.out.printf("等待连接中...");
        }

        System.out.println("Connected to Server: localhost:8080");

        // 创建输入流，读取用户输入
        Scanner scanner = new Scanner(System.in);

        // 创建ByteBuffer来发送和接收数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 循环读取用户输入并发送到服务器
        while (true) {
            System.out.print("Enter message (or 'exit' to quit): ");
            String message = scanner.nextLine();

            // 用户输入exit时退出循环
            if ("exit".equalsIgnoreCase(message)) {
                break;
            }

            // 发送消息到服务器
            buffer.clear();
            buffer.put(message.getBytes());
            buffer.flip();
            socketChannel.write(buffer);

            // 接收服务器的回复
            buffer.clear();
            int bytesRead = socketChannel.read(buffer);
            if (bytesRead == -1) {
                // 服务器断开连接
                System.out.println("Connection closed by server.");
                break;
            }

            buffer.flip();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            System.out.println("Received from server: " + new String(bytes));
        }

        // 关闭SocketChannel和Scanner
        socketChannel.close();
        scanner.close();
    }
}
