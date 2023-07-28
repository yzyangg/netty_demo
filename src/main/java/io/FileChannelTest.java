package io;

import io.netty.buffer.ByteBuf;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/23 10:41
 */
public class FileChannelTest {
    public static void main(String[] args) throws IOException {
//        String str = "hello,world";
//        FileOutputStream fileOutputStream = new FileOutputStream("data.txt");
//        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();
//        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
//        byteBuffer.put(str.getBytes());
//        byteBuffer.flip();
//        fileOutputStreamChannel.write(byteBuffer);
//        fileOutputStream.close();


        File file = new File("data.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        fileChannel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array()));

        RandomAccessFile randomAccessFile = new RandomAccessFile("data.txt", "rw");
        FileChannel fileChannel1 = randomAccessFile.getChannel();
        fileChannel1.position(fileChannel1.size());
        fileChannel1.write  (ByteBuffer.wrap("hello,world".getBytes()));
        fileChannel1.close();
        randomAccessFile.close();

    }
}
