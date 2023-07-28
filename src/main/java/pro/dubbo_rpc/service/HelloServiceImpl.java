package pro.dubbo_rpc.service;

import pro.dubbo_rpc.service.HelloService;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/27 23:08
 */
public class HelloServiceImpl implements HelloService {


    @Override
    public String hello(String name) {
        System.out.println("远程调用" + name);
        return "远程调用" + name;
    }
}
