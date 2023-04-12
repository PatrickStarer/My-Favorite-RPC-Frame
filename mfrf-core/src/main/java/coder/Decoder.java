package coder;

import entity.RPCRequest;
import entity.RPCResponse;
import enumeration.MessageType;
import enumeration.RPCErrorType;
import exception.RPCException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serializer.Serializer;

import java.security.MessageDigest;
import java.util.List;
import java.util.Set;


//ByteToMessageCodec 和 ReplayingDecoder
public class Decoder extends ByteToMessageDecoder {
    private static final Logger log = LoggerFactory.getLogger(Decoder.class);
    private static final int MAGIC = 0x1ACEBDF1;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //读取魔数
        int magic = in.readInt();
        if(magic != MAGIC){
            log.error("魔数错误");
            throw new RPCException(RPCErrorType.MAGIC_ERROR);
        }
        int msgType = in.readInt();
        Class<?> clazz ;
        if(msgType == MessageType.REQUEST.getCode()){
            clazz = RPCRequest.class;
        }else if(msgType == MessageType.RESPONSE.getCode()){
            clazz = RPCResponse.class;
        }else{
            log.error("数据类型错误");
            throw new RPCException(RPCErrorType.MESSAGE_ERROR);
        }
        int serialize = in.readInt();
        Serializer serializer = Serializer.getByCode(serialize);
        if(serializer == null){
            log.error("没有对应的序列化方法");
            throw new RPCException(RPCErrorType.NOT_FOUND_SERIALIZER);
        }
        int len = in.readInt();
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        Object object = serializer.backSerialize(bytes,clazz);
        out.add(object);
    }
}
