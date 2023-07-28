package pro.rpc_demo02.domain;

import lombok.Data;
import lombok.ToString;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/28 9:33
 */
@Data
@ToString
public class RpcRequest {
    /**
     * 请求对象的ID
     */
    private String requestId;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 入参
     */
    private Object[] parameters;
}