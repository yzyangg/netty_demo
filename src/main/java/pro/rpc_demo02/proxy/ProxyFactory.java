package pro.rpc_demo02.proxy;

import org.springframework.cglib.proxy.Proxy;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/28 9:38
 */
public class ProxyFactory {
    public static <T> T create(Class<T> interfaceClass) throws Exception {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new RpcClientDynamicProxy<T>(interfaceClass));
    }
}