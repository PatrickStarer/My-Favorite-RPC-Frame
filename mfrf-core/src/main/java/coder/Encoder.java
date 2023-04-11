package coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 */
public class Encoder extends MessageToByteEncoder {

    //定义魔术
    private static final int MAGIC = 0x1ACEBDF1;

//    private final CommonSer


    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        //八位魔数
        out.writeInt(MAGIC);


    }
}
