package netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/19 18:16
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        String s = byteBuf.toString(Charset.defaultCharset());
        System.out.println("服务端收到客户端发送的消息: " + s);
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
