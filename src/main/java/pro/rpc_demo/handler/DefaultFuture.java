package pro.rpc_demo.handler;

import pro.rpc_demo.model.RpcResponse;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/27 15:10
 */
public class DefaultFuture {
    private RpcResponse rpcResponse;
    private volatile boolean isSucceed = false;
    private final Object object = new Object();

    // 异步获取响应结果
    public RpcResponse getRpcResponse(int timeOut) {
        synchronized (object) {
            // 如果还没收到响应，就一直循环等待
            while (!isSucceed) {
                try {
                    // 每个循环阻塞时间为timeOut
                    object.wait(timeOut);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return rpcResponse;
        }
    }


    public void setRpcResponse(RpcResponse rpcResponse) {
        if (isSucceed) {
            return;
        }
        synchronized (object) {
            this.rpcResponse = rpcResponse;
            this.isSucceed = true;
            // 唤醒getRpcResponse中阻塞的线程
            object.notify();
        }
    }
}
