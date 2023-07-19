package netty.pipeline;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/18 16:33
 */
@Slf4j
public class PipeLineServer {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channelFuture = new ServerBootstrap().group(new NioEventLoopGroup()).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) {
                        channel.pipeline().addLast("one", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        log.error("one");
                                        ByteBuf byteBuf = (ByteBuf) msg;
                                        String name = byteBuf.toString(Charset.defaultCharset());
                                        log.error("{}", name.getClass());
                                        super.channelRead(ctx, name);
                                    }
                                })
                                .addLast("two", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object name) throws Exception {
                                        log.error("two");
                                        Student student = new Student((String) name);
                                        log.error("{}", student.getClass());
                                        super.channelRead(ctx, student);
                                    }
                                })

                                .addLast("three", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object student) throws Exception {
                                        log.error("three");
                                        Student one = (Student) student;
                                        log.error("{}", one.getClass());
                                        super.channelRead(ctx, one);
                                    }
                                });
                    }
                }).bind("localhost", 8080);
        channelFuture.sync();


    }

    static class Student {
        private String name;

        public Student(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
