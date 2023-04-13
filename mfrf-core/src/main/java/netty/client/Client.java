package netty.client;

import entity.RPCRequest;
import entity.RPCResponse;
import enumeration.RPCErrorType;
import enumeration.ResponseStatusCode;
import exception.RPCException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serializer.Serializer;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;


public class Client {

    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private static final EventLoopGroup eventLoopGroup=new NioEventLoopGroup();

    private static final Bootstrap bootstrap = new Bootstrap().group(eventLoopGroup).channel(NioSocketChannel.class);
//    private final ServiceDiscovery serviceDiscovery;
    private final Serializer serializer;

    private final SaveRequests saveRequests;

    public Client(Serializer serializer, SaveRequests saveRequests) {
        this.serializer = serializer;
        this.saveRequests = saveRequests;
    }
    public CompletableFuture<RPCResponse> sendRequest(RPCRequest rpcRequest){
        if(serializer == null){
            log.error("未设置序列化方式");
            throw new RPCException(RPCErrorType.NOT_FOUND_SERIALIZER);
        }
        CompletableFuture<RPCResponse>  res= new CompletableFuture<>();
//        try{
//            InetSocketAddress inet =
//        }
        return null;


    }


}
