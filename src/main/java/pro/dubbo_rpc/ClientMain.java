package pro.dubbo_rpc;

import pro.dubbo_rpc.server.Client;
import pro.dubbo_rpc.service.HelloService;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/28 7:45
 */
public class ClientMain {
    public static String providerName = "hello#";

    public static void main(String[] args) {
        Client client = new Client();
        Object service = client.getBean(HelloService.class, providerName);
        System.out.println("service的类型是 => "+service.getClass());
        HelloService helloService = (HelloService) service;
        String hello = helloService.hello("你好呀！");
        System.out.println("返回结果 => " + hello);

    }
}
