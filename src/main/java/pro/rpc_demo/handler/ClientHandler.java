package pro.rpc_demo.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import pro.rpc_demo.model.RpcRequest;
import pro.rpc_demo.model.RpcResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/27 15:08
 */

@ChannelHandler.Sharable
public class ClientHandler extends ChannelDuplexHandler {
    private final Map<String, DefaultFuture> futureMap = new ConcurrentHashMap<>();


    // netty 客户端读取到要要返回的消息
    // 收到消息后就写入DefaultFuture的rpcResponse中
    // 异步的实现具体是在DefaultFuture中
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof RpcResponse) {
            RpcResponse response = (RpcResponse) msg;
            DefaultFuture defaultFuture = futureMap.get(response.getRequestId());
            // 将响应结果放入DefaultFuture
            defaultFuture.setRpcResponse(response);
        }
        // 将消息传递给下一个handler
        super.channelRead(ctx, msg);
    }


    // 发送请求之前，构建一个DefaultFuture，用于接收响应结果
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof RpcRequest) {
            RpcRequest request = (RpcRequest) msg;
            futureMap.putIfAbsent(request.getRequestId(), new DefaultFuture());
        }
        // 将消息传递给下一个handler
        super.write(ctx, msg, promise);
    }

    public RpcResponse getResponse(String requestId) {
        try {
            DefaultFuture defaultFuture = futureMap.get(requestId);
            return defaultFuture.getRpcResponse(10000 / 2);
        } finally {
            futureMap.remove(requestId);
        }
    }

}
