package pro.rpc_demo.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import pro.rpc_demo.model.RpcRequest;
import pro.rpc_demo.model.RpcResponse;

import java.lang.reflect.InvocationTargetException;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/27 16:04
 */
@Component
@Slf4j
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest> implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        try {
            // 代理对象获取请求后处理的结果
            Object result = handler(rpcRequest);
            log.info("服务端执行结果: {}", result);
            rpcResponse.setResult(result);
        } catch (Throwable throwable) {
            rpcResponse.setError(throwable.toString());
            log.error("服务端执行失败:{}", throwable);
        }
        channelHandlerContext.writeAndFlush(rpcResponse);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 代理对象获取请求后处理的结果
     * @param rpcRequest
     * @return
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     */
    private Object handler(RpcRequest rpcRequest) throws ClassNotFoundException, InvocationTargetException {
        Class<?> clazz = Class.forName(rpcRequest.getClassName());
        Object serviceBean = applicationContext.getBean(clazz);
        log.info("serviceBean: {}", serviceBean);
        Class<?> serviceBeanClass = serviceBean.getClass();
        log.info("serviceBeanClass: {}", serviceBeanClass);


        // cglib 反射获取信息
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] parameters = rpcRequest.getParameters();

        log.info("开始调用cglib动态代理执行服务端方法");
        Object invoke = FastClass
                .create(serviceBeanClass)
                .getMethod(methodName, parameterTypes).invoke(serviceBean, parameters);

        return invoke;
    }


}
