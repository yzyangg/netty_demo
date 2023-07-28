package netty.reactor.basic;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/21 17:48
 */
public class AsyncHandler implements Runnable {
    /**
     * 多路复用器
     */
    private final Selector selector;
    /**
     * 客户端通道
     */
    private final SocketChannel socketChannel;
    /**
     * 选择键，用于表示注册到多路复用器中的通道的就绪状态
     */
    private final SelectionKey selectionKey;


    private ByteBuffer readBuff = ByteBuffer.allocate(1024);
    private ByteBuffer sendBuff = ByteBuffer.allocate(2048);

    private final static int READ = 0; // 读取就绪
    private final static int SEND = 1; // 响应就绪
    public static final int PROCESSING = 2; // 处理中

    private int status = READ;

    // 开启线程数为4的异步处理线程池
    private static final ExecutorService workers = Executors.newFixedThreadPool(5);

    public AsyncHandler(Selector selector, SocketChannel socketChannel) throws IOException {
        this.selector = selector;
        this.socketChannel = socketChannel;
        this.socketChannel.configureBlocking(false); // 设置为非阻塞模式
        selectionKey = socketChannel.register(selector, 0); // 将通道注册到多路复用器上，并设置为读取就绪状态
        selectionKey.attach(this); // 附加处理对象，当前是Handler对象
        selectionKey.interestOps(SelectionKey.OP_READ); // 注册读操作
        this.selector.wakeup();// 唤醒阻塞在select方法上的线程，使得selector能够响应注册的读事件

    }

    /**
     * 异步读取客户端请求消息
     */
    @Override
    public void run() {
        switch (status) {
            case READ:
                read();
                break;
            case SEND:
                send();
                break;
            default:

        }
    }

    /**
     * read方法结束，意味着本次"读就绪"变为"读完毕"，标记着一次就绪事件的结束
     */
    private void read() {
        // 检查selector的状态是否有效
        if (selectionKey.isValid()) {
            try {
                readBuff.clear();

                // 从socketChannel中读取数据
                int count = socketChannel.read(readBuff);

                if (count > 0) {
                    status = PROCESSING; // 读取到数据后，将状态置为处理中
                    workers.execute(this::readWorker); // 交由线程池处理读取到的数据
                } else {
                    selectionKey.cancel();
                    socketChannel.close();
                    System.out.println("读取结束");
                }
            } catch (IOException e) {
                System.out.printf("读取数据失败，原因：%s%n", e.getMessage());
                selectionKey.cancel();
                try {
                    socketChannel.close();
                } catch (IOException ioException) {
                    System.out.printf("关闭连接失败，原因：%s%n", ioException.getMessage());
                }
            }
        }
    }


    /**
     * send方法结束，意味着本次"写就绪"变为"写完毕"，标记着一次就绪事件的结束
     */
    private void send() {
        if (selectionKey.isValid()) {
            status = PROCESSING; // 写入数据后，将状态置为处理中
            workers.execute(this::sendWorker); // 交由线程池处理发送的数据
            selectionKey.interestOps(SelectionKey.OP_READ); // 重新
        }
    }

    /**
     * 读取数据的业务逻辑
     */
    private void readWorker() {
        try {
            Thread.sleep(3 * 1000);
            System.out.printf("读取到来自%s的数据：%s%n", socketChannel.getRemoteAddress(), new String(readBuff.array()));
            status = SEND;
            selectionKey.interestOps(SelectionKey.OP_WRITE);
            this.selector.wakeup();
        } catch (InterruptedException e) {
            System.out.printf("读取数据失败，原因：%s%n", e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送数据的业务逻辑
     */
    private void sendWorker() {
        try {
            sendBuff.clear();
            sendBuff.put(String.format("来自%s的回应", socketChannel.getLocalAddress()).getBytes());
            sendBuff.flip();

            int count = socketChannel.write(sendBuff);
            if (count < 0) {
                selectionKey.cancel();
                socketChannel.close();
                System.out.println("写入结束");
            }

            status = READ;
        } catch (Exception e) {
            System.err.println("异步处理send业务时发生异常！异常信息：" + e.getMessage());
            selectionKey.cancel();
            try {
                socketChannel.close();
            } catch (IOException e1) {
                System.err.println("异步处理send业务关闭通道时发生异常！异常信息：" + e.getMessage());
            }
        }
    }

}
