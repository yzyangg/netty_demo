package pro.rpc_demo.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pro.rpc_demo.proxy.ProxyFactory;
import pro.rpc_demo.service.HelloService;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/27 16:45
 */
@SpringBootApplication
@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
        HelloService helloService = ProxyFactory.create(HelloService.class);
        String yzy = helloService.hello("yzy");
        log.info("结果: {}", yzy);
    }
}
