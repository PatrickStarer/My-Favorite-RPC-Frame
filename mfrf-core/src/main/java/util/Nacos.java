package util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import enumeration.RPCErrorType;
import exception.RPCException;
import jdk.nashorn.internal.objects.NativeObject;
import org.omg.PortableInterceptor.INACTIVE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;



public class Nacos {
    private static final Logger log = LoggerFactory.getLogger(Nacos.class);
    //nacos 用来管理服务
    private static final NamingService naming = get();
    //存放服务名
    private static final Set<String> setName = new HashSet<>();
    //InetSocketAddress实例
    private static  InetSocketAddress address;
    //地址串
    private static final String SERVER_ADDR ="127.0.0.1:8849";

    //连接nacos，初始换nacos连接实例
    public static NamingService get() {
        try {
            return NamingFactory.createNamingService(SERVER_ADDR);
        }catch (NacosException e){
            log.error("连接Nacos发生错误");
            throw new RPCException(RPCErrorType.NACOS_ERROR);
        }
    }

    //注册服务，保存服务名和服务所在的地址串
    public static void register(String serviceName, InetSocketAddress address) throws NacosException {
        naming.registerInstance(serviceName,address.getHostName(),address.getPort());
       Nacos.address = address;
        setName.add(serviceName);
    }

    //返回服务实例
    public static List<Instance> getAllInstances(String serviceName) throws NacosException {
        return naming.getAllInstances(serviceName);
    }


    //清除服务
    public static void clear(){
        if(!setName.isEmpty() && address!=null){
            String host = address.getHostName();
            int port = address.getPort();
            Iterator<String> iterator = setName.iterator();
            while(iterator.hasNext()){
                String name = iterator.next();
                try{
                    naming.deregisterInstance(name,host,port);
                }catch (NacosException e){
                    log.error(name+"---"+host+"---"+port+"---注册失败");
                }
            }
        }
    }

}
