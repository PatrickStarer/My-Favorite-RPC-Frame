package netty.client;

import entity.RPCResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 保存还没有完成的请求
 */
public class SaveRequests {
    private static Map<String, CompletableFuture<RPCResponse>> saveRequests = new ConcurrentHashMap<>();

    public void putRequests(String requestId,CompletableFuture<RPCResponse> future){
       saveRequests.put(requestId,future);
    }
    public void remove(String requestId){
        saveRequests.remove(requestId);
    }

    public void success(RPCResponse response){
        CompletableFuture<RPCResponse> future = saveRequests.remove(response.getReqId());
        if(future != null){
            future.complete(response);
        }else{
            throw new IllegalStateException();
        }

    }
}
