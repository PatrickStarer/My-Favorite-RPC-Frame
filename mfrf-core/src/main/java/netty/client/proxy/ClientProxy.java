package netty.client.proxy;

import entity.RPCRequest;
import entity.RPCResponse;
import netty.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ClientProxy  implements InvocationHandler {
    private static final Logger log = LoggerFactory.getLogger(ClientProxy.class);
    private final Client client;
    public ClientProxy(Client client){
        this.client = client;

    }


    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<?> clazz){
       return(T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RPCRequest req = new RPCRequest(method.getDeclaringClass().getName(),method.getName(),args,
                method.getParameterTypes(), UUID.randomUUID().toString(), false);


        RPCResponse response = null;
        try{
            CompletableFuture<RPCResponse> completableFuture =(CompletableFuture<RPCResponse>)client.sendRequest(req);
            response = completableFuture.get();
        }catch (Exception e){
            log.error("代理fas失败");
            return null;
        }
        return response.getData();







    }
}
