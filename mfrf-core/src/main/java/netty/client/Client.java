package netty.client;

import balancer.Balancer;
import balancer.RandomBalancer;
import coder.Decoder;
import entity.RPCRequest;
import entity.RPCResponse;
import enumeration.RPCErrorType;
import exception.RPCException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.nio.NioSocketChannel;
import nacos.Discovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serializer.Serializer;
import util.Singleton;

import java.awt.image.BandCombineOp;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;


public class Client {

    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private static final EventLoopGroup eventLoop=new NioEventLoopGroup();
    private static final Bootstrap bootstrap = new Bootstrap().group(eventLoop).channel(NioSocketChannel.class);

    private final Discovery discovery;
    private final Serializer serializer;
    private final SaveRequests saveRequests;


    public Client (Integer serializer){
        this(new RandomBalancer(),serializer);
    }
    public Client(Balancer balancer){
        this(balancer,1);

    }
    public Client(Balancer balancer,Integer serializer){
        this.serializer = Serializer.getByCode(serializer);
        this.discovery  = new Discovery(balancer);
        this.saveRequests = Singleton.getObject(SaveRequests.class);
    }


    public CompletableFuture<RPCResponse> sendRequest(RPCRequest rpcRequest){
        if(serializer == null){
            log.error("未设置序列化方式");
            throw new RPCException(RPCErrorType.NOT_FOUND_SERIALIZER);
        }
        CompletableFuture<RPCResponse>  res= new CompletableFuture<>();
        try{
            InetSocketAddress inet = discovery.findService(rpcRequest.getInterfaceName());
            Channel channel = ClientChannel.get(inet,serializer);
            if(!channel.isActive()){
                eventLoop.shutdownGracefully();
                return null;
            }
            saveRequests.putRequests(rpcRequest.getReqId(),res);
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener)future->{
                if(future.isSuccess()){
                    log.info("客户端发送消息"+rpcRequest.getReqId());
                }else{
                    future.channel().closeFuture();
                    res.completeExceptionally(future.cause());
                    log.error("客户端发送错误");
                }
            });
        }catch (Exception e){
            saveRequests.remove(rpcRequest.getReqId());
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return res;


    }


}
