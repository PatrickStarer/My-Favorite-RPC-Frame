package nacos;

import balancer.Balancer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import enumeration.RPCErrorType;
import exception.RPCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Nacos;

import java.net.InetSocketAddress;
import java.util.List;

public class Discovery {
    private static final Logger log = LoggerFactory.getLogger(Discovery.class);
    private final Balancer balancer;
    public Discovery(Balancer balancer){
        this.balancer = balancer;
    }

    public InetSocketAddress findService(String serviceName){
        try {
            List<Instance> lists = Nacos.getAllInstances(serviceName);
            if (lists.size() == 0) {
                log.error("找不到对应服务");
                throw new RPCException(RPCErrorType.SERVICE_NOT_FOUND);
            }
            Instance instance = balancer.select(lists);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        }catch (NacosException e){
            log.error("获取服务错误");
        }

        return null;
    }
}
