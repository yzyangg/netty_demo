package netty.nio;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/18 15:38
 */
@Slf4j
public class BlockingEchoClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup(2);
        EventLoop next = group.next();

        Future<Integer> future = next.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("开始计算");
                Thread.sleep(1000);
                return 10;
            }
        });

        future.addListener(future1 -> {
            log.debug("计算结果：{}", future1.getNow());
        });


    }
}
