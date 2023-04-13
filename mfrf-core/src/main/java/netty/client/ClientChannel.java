package netty.client;

import coder.Decoder;
import coder.Encoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import netty.client.handler.ClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serializer.Serializer;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ClientChannel {
    private static final Logger log = LoggerFactory.getLogger(ClientChannel.class);
    private static EventLoopGroup eventLoopGroups;
    private static Bootstrap bootstrap= initializeBootstrap();
    private static Map<String ,Channel> saveChannels = new ConcurrentHashMap<>();


    //得到管道
    public static Channel get(InetSocketAddress inetSocketAddress, Serializer serializer){
        String key = inetSocketAddress.toString() + serializer.getCode();
        if(saveChannels.containsKey(key)){
            Channel c = saveChannels.get(key);
            if(saveChannels != null && c.isActive()){
                return c;
            }else{
                //如果管道不存活 直接remove
                saveChannels.remove(key);
            }
        }
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                 ch.pipeline().addLast(new Encoder(serializer))
                         .addLast(new IdleStateHandler(0,5,0, TimeUnit.SECONDS))
                         .addLast(new Decoder())
                         .addLast(new ClientHandler());
            }
        });
        Channel channel = null;
        try{
            channel = connect(bootstrap,inetSocketAddress);
        }catch (Exception e){
            log.error("连接失败");
            return null;
        }
        saveChannels.put(key,channel);
        return  channel;
    }





    //客户端连接
    private static Channel connect(Bootstrap bootstrap, InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> channelFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener)future ->{
            if(future.isSuccess()){
                log.info("客户端连接成功");
               channelFuture.complete(future.channel());
            }else{
                throw new IllegalStateException();
            }
        });
        return channelFuture.get();

    }



    //初始换bootstrap
    private static Bootstrap initializeBootstrap(){
        eventLoopGroups = new NioEventLoopGroup();
        Bootstrap boot = new Bootstrap();
        boot.group(eventLoopGroups)
                .channel(NioSocketChannel.class)
               //连接超时
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                //tcp 保活机制
                .option(ChannelOption.SO_KEEPALIVE,true)
                //减少网络延迟
                .option(ChannelOption.TCP_NODELAY,true);
        return boot;
    }

}
