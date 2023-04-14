package provider;

import enumeration.RPCErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import exception.RPCException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//本地保持提供服务的实例
public class ServiceProvider {
    //日志
    private static final Logger log = LoggerFactory.getLogger(ServiceProvider.class);
    //本地服务，<服务名,实例>
    private static final Map<String,Object> service = new ConcurrentHashMap<>();
    //服务名
    private static final Set<String> serviceName = new HashSet<>();


    public<T> void addProvider(T provider, String providerName){
        if(service.containsKey(providerName))
            return;

        serviceName.add(providerName);
        service.put(providerName,provider);
        log.info("本地注册服务：---->"+providerName);
    }

    public Object getProvider(String providerName){
        Object provider= service.get(providerName);
       if(provider == null)
           throw new RPCException(RPCErrorType.SERVICE_NOT_FOUND);
        return provider;
    }


}
