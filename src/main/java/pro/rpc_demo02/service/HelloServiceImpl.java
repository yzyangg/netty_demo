package pro.rpc_demo02.service;

import org.springframework.stereotype.Service;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/28 9:44
 */
//服务端实现
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "hello, " + name;
    }
}