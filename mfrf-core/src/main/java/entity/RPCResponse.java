package entity;

import com.alibaba.nacos.api.common.ResponseCode;
import enumeration.ResponseStatusCode;
import lombok.Data;
import serializer.Serializer;

import javax.xml.ws.Response;
import java.io.Serializable;
//响应消息体
@Data
public class RPCResponse<T> implements Serializable {
   //响应数据
    private T data;

    //响应对应的请求序号
    private String ReqId;

    //状态码
    private Integer status;

    //对响应状态码的解释
    private String message;

    public static <T> RPCResponse<T> success(T data,String resId){
     RPCResponse<T> response = new RPCResponse<>();
     response.setData(data);
     response.setReqId(resId);
     response.setStatus(ResponseStatusCode.SUCCESS.getCode());
     response.setMessage(ResponseStatusCode.SUCCESS.getMessage());
     return response;
    }

    public static <T> RPCResponse<T> fail(ResponseStatusCode responseCode , String resId){
     RPCResponse<T> response = new RPCResponse<>();
     response.setReqId(resId);
     response.setStatus(responseCode.getCode());
     response.setMessage(responseCode.getMessage());
     return response;
    }



}
