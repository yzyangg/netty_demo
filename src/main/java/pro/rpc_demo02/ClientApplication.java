package pro.rpc_demo02;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pro.rpc_demo02.proxy.ProxyFactory;
import pro.rpc_demo02.service.HelloService;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/28 9:44
 */
@SpringBootApplication
@Slf4j
public class ClientApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ClientApplication.class, args);
        HelloService helloService = ProxyFactory.create(HelloService.class);
        log.info("响应结果“: {}", helloService.hello("yzy"));
    }
}