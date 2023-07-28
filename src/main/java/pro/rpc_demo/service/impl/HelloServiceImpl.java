package pro.rpc_demo.service.impl;

import org.springframework.stereotype.Service;
import pro.rpc_demo.service.HelloService;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/27 16:58
 */
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}
