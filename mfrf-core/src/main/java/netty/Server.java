package netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Server {
    private Integer port;
    private  String host;
    private Integer serializer;


    public Server(Integer port, String host) {
        this.port = port;
        this.host = host;
        this.serializer = 1;
    }

    public void start(){
        //负责连接的连接池组
        EventLoopGroup boss =new NioEventLoopGroup();
        //负责处理io的池组
        EventLoopGroup worker =new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        //添加工作组
        bootstrap.group(boss,worker)
                .channel(ServerChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO));
//                .


    }



}
