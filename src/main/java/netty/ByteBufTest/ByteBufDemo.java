package netty.ByteBufTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/19 17:35
 */
public class ByteBufDemo {
    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer(10);
        System.out.println("原始ByteBuf：");
        printBuffer(buffer);


        System.out.printf("写入4个字节：");
        String love = "love";
        buffer.writeBytes(love.getBytes());
        printBuffer(buffer);

        System.out.printf("读取数据");
        while (buffer.isReadable()) {
            System.out.print((char) buffer.readByte());
        }
        System.out.println();

        printBuffer(buffer);


        System.out.println("清空缓冲区");
        buffer.clear();
        printBuffer(buffer);
    }
    private static void printBuffer(ByteBuf buffer) {
        System.out.println("readerIndex：" + buffer.readerIndex());
        System.out.println("writerIndex：" + buffer.writerIndex());
        System.out.println("capacity：" + buffer.capacity());
    }
}
