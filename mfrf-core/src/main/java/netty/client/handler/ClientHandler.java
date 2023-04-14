package netty.client.handler;

import entity.RPCRequest;
import entity.RPCResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import netty.client.Client;
import netty.client.ClientChannel;
import netty.client.SaveRequests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serializer.Serializer;
import util.Singleton;

import java.net.InetSocketAddress;
import java.util.UUID;

public class ClientHandler extends SimpleChannelInboundHandler<RPCResponse> {
    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);
    private final SaveRequests saveRequests ;
    public ClientHandler( ){
        this.saveRequests = Singleton.getObject(SaveRequests.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCResponse msg) throws Exception {
        try{
            log.info("客户端接收到响应--->" + msg);
            saveRequests.success(msg);
        }catch (Exception e){
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent) evt).state();
            //读心跳
            if(state == IdleState.WRITER_IDLE){
                log.info("向服务端发送心跳包");
                Channel channel = ClientChannel.get((InetSocketAddress) ctx.channel().remoteAddress(), Serializer.getByCode(Serializer.DEFAULT_SERIALIZER));
                RPCRequest rpcRequest = new RPCRequest();
                rpcRequest.setReqId(UUID.randomUUID().toString());
                rpcRequest.setHeart(true);
                channel.writeAndFlush(rpcRequest).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }else{
                super.userEventTriggered(ctx,evt);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("客户端出现错误");
        cause.printStackTrace();
        ctx.close();
    }
}
