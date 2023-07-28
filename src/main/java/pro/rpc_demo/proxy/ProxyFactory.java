package pro.rpc_demo.proxy;

import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/27 16:27
 */
public class ProxyFactory {
    public static <T> T create(Class<T> clazz) throws Exception {
        return (T) Proxy
                .newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new RpcClientProxy(clazz));
    }
}
