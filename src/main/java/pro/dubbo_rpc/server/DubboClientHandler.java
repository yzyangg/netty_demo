package pro.dubbo_rpc.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/27 23:40
 */

@Slf4j
public class DubboClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext context;
    private String result;
    private String para;

    // 外部调用自动执行
    @Override
    public Object call() throws Exception {
        log.info("发送数据");
        context.writeAndFlush(para);
        // 等待channelRead从服务端获取消息
        wait();
        // 服务端返回的结果
        log.info("异步等到了返回结果");
        return result;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("等待结果ing");
        result = msg.toString();
        log.info("等到结果啦");
        notify();// 读到消息了，唤醒等待的线程call()
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

    void setPara(String para) {
        this.para = para;
    }
}
