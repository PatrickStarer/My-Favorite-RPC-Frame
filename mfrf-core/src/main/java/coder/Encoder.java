package coder;

import entity.RPCRequest;
import enumeration.MessageType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import serializer.Serializer;

import java.security.spec.ECField;

/**
 * 编码器
 */
@Data
public class Encoder extends MessageToByteEncoder {

    //定义魔术
    private static final int MAGIC = 0x1ACEBDF1;

    private final Serializer serializer;
    public Encoder(Serializer serializer){
        this.serializer =serializer;
    }
    public Encoder(){
        this.serializer = Serializer.getByCode(1);
    }
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        //八位魔数
        out.writeInt(MAGIC);
        //四位 请求或者响应
        if(msg instanceof RPCRequest){
            out.writeInt(MessageType.REQUEST.getCode());
        }else{
            out.writeInt(MessageType.RESPONSE.getCode());
        }
        //四位序列化号
        out.writeInt(serializer.getCode());
        byte[] bytes = serializer.toSerializer(msg);
       //四位长度
        out.writeInt(bytes.length);
        out.writeBytes(bytes);

    }
}
