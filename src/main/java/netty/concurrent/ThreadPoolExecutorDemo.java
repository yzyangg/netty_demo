package netty.concurrent;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/21 17:34
 */
public class ThreadPoolExecutorDemo {
    public static void main(String[] args) {
        LinkedBlockingDeque<Runnable> blockingDeque = new LinkedBlockingDeque<>(10);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, blockingDeque);

        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + " is running");
                try {
                    Thread.sleep(3 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

    }
}
