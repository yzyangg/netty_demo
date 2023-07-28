package pro.rpc_demo.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.InvocationHandler;
import pro.rpc_demo.model.RpcRequest;
import pro.rpc_demo.model.RpcResponse;
import pro.rpc_demo.server.Client;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/27 16:16
 */
@Slf4j
public class RpcClientProxy<T> implements InvocationHandler {
    private Class<T> clazz;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public RpcClientProxy(Class<T> clazz) throws Exception {
        this.clazz = clazz;
    }

    /**
     * 代理类调用方法时会调用这个方法(远程调用)
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        String requestId = UUID.randomUUID().toString();

        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();

        Class<?>[] parameterTypes = method.getParameterTypes();

        rpcRequest.setRequestId(requestId);
        rpcRequest.setClassName(className);
        rpcRequest.setMethodName(methodName);
        rpcRequest.setParameterTypes(parameterTypes);
        rpcRequest.setParameters(args);
        log.info("客户端发送的请求: {}", rpcRequest);

        Client client = new Client("localhost", 8080);
        log.info("客户端开始连接服务端: {}", dateFormat.format(new Date()));

        client.connect();

        RpcResponse response = client.send(rpcRequest);
        Object result = response.getResult();
        log.info("客户端接收到服务端响应: {}", result);
        return result;
    }
}
