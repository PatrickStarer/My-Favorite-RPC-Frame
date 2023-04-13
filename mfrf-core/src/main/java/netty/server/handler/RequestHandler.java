package netty.server.handler;


import entity.RPCRequest;
import entity.RPCResponse;
import enumeration.ResponseStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.ServiceProvider;

import java.lang.reflect.Method;

//请求拦截器
public class RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    //TODO 优化为static
    private static final ServiceProvider serviceProvider = new ServiceProvider();


    //请求处理器 ，使用反射 调用方法
    public Object questHandle(RPCRequest rpcRequest){
        //得到 要调用的接口
        Object service = serviceProvider.getProvider(rpcRequest.getInterfaceName());
        Object res ;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamType());
            res = method.invoke(service,rpcRequest.getParams());
        } catch (Exception e) {
            return RPCResponse.fail(ResponseStatusCode.FAIL,rpcRequest.getReqId());
        }

        return res;
    }


}
