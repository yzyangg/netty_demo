package netty.reactor.basic;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/22 12:06
 */
public class Main {
    public static void main(String[] args) throws IOException {
        new Thread(new Reactor(8080)).start();
//        new LengthFieldBasedFrameDecoder()
    }
}
