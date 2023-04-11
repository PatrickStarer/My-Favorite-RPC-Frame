package netty;


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
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import netty.handler.ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


@Data

@NoArgsConstructor
public class Server {
    private Integer port;
    private String host;
    private Integer serializer;


    public Server(Integer port, String host) {
        this(port,host,1);
    }
    public Server(Integer port, String host,Integer serializer) {
        this.port = port;
        this.host = host;
        this.serializer = serializer;

    }


    private static final Logger log = LoggerFactory.getLogger(Server.class);

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
                            channelPipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS))
                                    .addLast(new Decoder())
                                    .addLast(new Encoder())
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
