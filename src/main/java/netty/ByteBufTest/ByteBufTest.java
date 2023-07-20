package netty.ByteBufTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/18 18:07
 */
public class ByteBufTest {
    public static void main(String[] args) {
//        ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
//        ByteBuf x = allocator.buffer().writeBytes("hello world".getBytes());
//        System.out.println(x);
//        System.out.println(x.toString(Charset.defaultCharset()));

        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.heapBuffer();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();

        //
        System.out.println(byteBuf.getClass());
        System.out.println(buffer.getClass());
        

    }
}
