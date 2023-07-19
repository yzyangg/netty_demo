package netty.embedChannelTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/18 18:01
 */
@Slf4j
public class embedChannelTest {
    public static void main(String[] args) {
        ChannelInboundHandler channelInboundHandler1 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.debug("1");
                super.channelRead(ctx, msg);
            }
        };
        ChannelInboundHandler channelInboundHandler2 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.debug("2");
                super.channelRead(ctx, msg);
            }
        };
        ChannelInboundHandler channelInboundHandler3 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.debug("3");
                super.channelRead(ctx, msg);
            }
        };
        ChannelInboundHandler channelInboundHandler4 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.debug("4");
                super.channelRead(ctx, msg);
            }
        };

        EmbeddedChannel embedChannel = new EmbeddedChannel(channelInboundHandler1, channelInboundHandler2, channelInboundHandler3, channelInboundHandler4);
        embedChannel.writeInbound(ByteBufAllocator.DEFAULT.buffer().writeBytes("hello world".getBytes()));

    }
}
