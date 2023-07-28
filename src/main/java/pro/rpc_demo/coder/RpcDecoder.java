package pro.rpc_demo.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import pro.rpc_demo.serial.Serializer;

import java.util.List;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/27 14:11
 */
public class RpcDecoder extends ByteToMessageDecoder {
    private Class<?> clazz;
    private Serializer serializer;

    public RpcDecoder(Class<?> clazz, Serializer serializer) {
        this.clazz = clazz;
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readInt() < 4) return;
        byteBuf.markReaderIndex();

        int dataLength = byteBuf.readInt();

        // 如果可读字节小于数据长度，说明数据还没到齐，重置读指针(等待数据到齐)
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[dataLength];
        byteBuf.readBytes(bytes);

        Object o = serializer.deserialize(clazz, bytes);
        // 将解码后的对象加入到解码列表中
        list.add(o);
    }
}
