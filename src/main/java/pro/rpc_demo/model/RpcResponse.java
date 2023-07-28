package pro.rpc_demo.model;

import lombok.Data;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/27 14:04
 */
@Data
public class RpcResponse {
    /**
     * 请求id
     */
    private String requestId;
    /**
     * 返回结果
     */
    private Object result;
    /**
     * 错误信息
     */
    private String error;
}
