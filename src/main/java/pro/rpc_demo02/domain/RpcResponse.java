package pro.rpc_demo02.domain;

import lombok.Data;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/28 9:33
 */
@Data
public class RpcResponse {
    /**
     * 响应ID
     */
    private String requestId;
    /**
     * 错误信息
     */
    private String error;
    /**
     * 返回的结果
     */
    private Object result;
}