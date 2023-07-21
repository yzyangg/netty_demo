package netty.ByteBufTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.Charset;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/20 13:54
 */
public class SliceTest {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        String[] s = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        for (int i = 0; i < s.length; i++) {
            buffer.writeBytes(s[i].getBytes());
        }
        ByteBuf slice = buffer.slice(0, 5);

//        buffer.release();
        slice.retain();
        slice.release();
        System.out.println(buffer);
        System.out.println(slice);

    }

}
