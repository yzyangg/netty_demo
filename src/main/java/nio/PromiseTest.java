package nio;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/18 16:23
 */
@Slf4j
public class PromiseTest {
    public static void main(String[] args) {
        EventLoop next = new NioEventLoopGroup().next();
        DefaultPromise<Object> promise = new DefaultPromise<>(next);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                promise.setSuccess("成功");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        promise.addListener(future -> {
            log.debug("{}", future.getNow());
        });
    }
}
