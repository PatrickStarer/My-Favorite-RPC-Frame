package nacos;


import com.alibaba.nacos.api.exception.NacosException;
import enumeration.RPCErrorType;
import exception.RPCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Nacos;

import java.net.InetSocketAddress;

public class Registry {
    private static final Logger log = LoggerFactory.getLogger(Registry.class);

    public void registry(String serviceName, InetSocketAddress inet){
        try{
            Nacos.register(serviceName,inet);
            log.info("nacos注册服务：----->"+serviceName);
        }catch (NacosException e){
            log.error("服务注册出现错误");
            throw new RPCException(RPCErrorType.REGISTRY_ERROR);
        }

    }


}
