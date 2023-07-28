package netty.reactor.basic;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.logging.Handler;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/22 11:55
 */
public class handler implements Runnable {
    public final SelectionKey selectionKey;
    public final SocketChannel socketChannel;

    private ByteBuffer readBuff = ByteBuffer.allocate(1024);
    private ByteBuffer sendBuff = ByteBuffer.allocate(2048);

    private final static int READ = 0; // 读取就绪
    private final static int SEND = 1; // 响应就绪

    private int status = READ;

    public handler(SocketChannel socketChannel, Selector selector) throws IOException {
        this.socketChannel = socketChannel; // 接受客户端连接
        this.socketChannel.configureBlocking(false); // 设置为非阻塞模式
        selectionKey = socketChannel.register(selector, 0); // 将通道注册到多路复用器上，并设置为读取就绪状态
        selectionKey.attach(this); // 附加处理对象，当前是Handler对象
        selectionKey.interestOps(SelectionKey.OP_READ); // 注册读操作

        selector.wakeup();


    }

    @Override
    public void run() {
        try {
            switch (status) {
                case READ:
                    read();
                    break;
                case SEND:
                    send();
                    break;
                default:
            }
        } catch (IOException e) {
            try {
                System.out.printf("客户端%s已断开连接", socketChannel.getRemoteAddress());
                selectionKey.cancel();
                socketChannel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }
    }

    public void read() throws IOException {
        if (selectionKey.isValid()) {
            System.out.println("服务端读取数据前");
            readBuff.clear();

            int count = socketChannel.read(readBuff);
            if (count > 0) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(String.format("recived %s from %s",
                        new String(readBuff.array()), socketChannel.getRemoteAddress()));
                status = SEND;
                selectionKey.interestOps(SelectionKey.OP_WRITE);

            } else {
                selectionKey.cancel();
                socketChannel.close();
                System.out.printf("客户端%s已断开连接", socketChannel.getRemoteAddress());
            }

        }
    }
    void send() throws IOException {
        if (selectionKey.isValid()) {
            sendBuff.clear();
            sendBuff.put(String
                    .format("recived %s from %s", new String(readBuff.array()), socketChannel.getRemoteAddress())
                    .getBytes());
            sendBuff.flip();

            // write方法结束，意味着本次写就绪变为写完毕，标记着一次事件的结束
            int count = socketChannel.write(sendBuff);

            if (count < 0) {
                // 同上，write场景下，取到-1，也意味着客户端断开连接
                selectionKey.cancel();
                socketChannel.close();
                System.out.println("send close");
            }

            // 没断开连接，则再次切换到读
            status = READ;
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }
}
