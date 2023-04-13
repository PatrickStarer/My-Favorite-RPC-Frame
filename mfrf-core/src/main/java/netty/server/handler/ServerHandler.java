package netty.server.handler;

import entity.RPCRequest;
import entity.RPCResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//接收数据，指定RPCRequest类型
public class ServerHandler extends SimpleChannelInboundHandler<RPCRequest> {
    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);
    private final RequestHandler requestHandler = new RequestHandler();

    //服务端 接收到请求 数据时
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCRequest msg) throws Exception {
        try {
            if (msg.getHeart()) {
                log.info("接收到客户端心跳");
            }
            Object res = requestHandler.questHandle(msg);
            if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                ctx.writeAndFlush(RPCResponse.success(res, msg.getReqId()));
            } else {
                log.error("通道故障");
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("出现有错误");
        cause.printStackTrace();
        ctx.close();
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent)evt).state();
            if(state == IdleState.READER_IDLE){
                log.info("规定时间未收到心跳，断开连接");
                ctx.close();
            }
        }else{
            super.userEventTriggered(ctx, evt);
        }
    }
}
