package pro.rpc_demo.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/27 14:02
 */

@Data
@ToString
public class RpcRequest {
    /**
     * 请求id
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
     * 参数
     */
    private Object[] parameters;
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;
}
