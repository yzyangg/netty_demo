package netty.ByteBufTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/19 13:37
 */
public class ByteBufCompositeBufferDemo {
    public static void main(String[] args) {

        // 1.创建一个堆缓冲区
        ByteBuf heapBuffer = Unpooled.buffer();
        String s = "hello world";
        // 2.写入堆缓冲区
        heapBuffer.writeBytes(s.getBytes());

        // 3.创建一个直接缓冲区
        ByteBuf directBuffer = Unpooled.directBuffer();
        // 4.写入直接缓冲区
        directBuffer.writeBytes(s.getBytes());

        // 5.创建一个复合缓冲区
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        compositeByteBuf.addComponent(heapBuffer);
        compositeByteBuf.addComponent(directBuffer);
        if (!compositeByteBuf.hasArray()) {
            for (ByteBuf byteBuf : compositeByteBuf) {
                int offset = byteBuf.readerIndex();

                int length = byteBuf.readableBytes();

                byte[] bytes = new byte[length];
                ByteBuf byteBufBytes = byteBuf.getBytes(offset, bytes);
                String s1 = byteBufBytes.toString(Charset.defaultCharset());
                System.out.printf("offset:%d,length:%d,s:%s\n", offset, length, s1);
            }
        }
        System.out.println("====================================");


    }
}
