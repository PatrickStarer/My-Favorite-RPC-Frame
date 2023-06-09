package netty.server;


import coder.Decoder;
import coder.Encoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Data;
import netty.server.handler.ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scan.ProviderScan;
import serializer.Serializer;

import java.util.concurrent.TimeUnit;


@Data
public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);
    private Integer port;
    private String host;
    private final    Serializer serializer;
    private static ProviderScan providerScan = new ProviderScan();
    public Server(Integer port, String host) {
        this(port,host,1);
    }
    public Server(Integer port, String host,Integer serializer) {
        this.port = port;
        this.host = host;
        this.serializer = Serializer.getByCode(serializer);
        providerScan.scanProvider(port,host);
    }
    private Server(){
        this(8888,"127.0.0.1",1);

    }



    public void start() {

        //负责连接的连接池组
        EventLoopGroup boss = new NioEventLoopGroup();
        //负责处理io的池组
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            //添加工作组
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
//                未处理的连接请求最大值
                    .option(ChannelOption.SO_BACKLOG, 256)
                    //tcp 的 keppalive机制
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // 无tcp延迟
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline channelPipeline = ch.pipeline();
                            //如果 30seconds server没有读操作，则执行IdleStateHandler处理器。
                            channelPipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS))
                                    .addLast(new Decoder())
                                    .addLast(new Encoder(serializer))
                                    .addLast(new ServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("服务端启动失败");
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }


}
