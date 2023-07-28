package pro.dubbo_rpc.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;
import pro.dubbo_rpc.service.HelloServiceImpl;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/27 23:20
 */
@Slf4j
public class DubboServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("msg: {}", msg);
        if (msg.toString().startsWith("hello#")) {
            HelloServiceImpl service = new HelloServiceImpl();
            String result = service.hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }
}
