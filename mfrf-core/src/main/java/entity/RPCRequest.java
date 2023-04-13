package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RPCRequest implements Serializable {

    //要请求的接口
    private String interfaceName;
    //要调用哪个方法
    private String methodName;
    //参数集合
    private Object[] params;
    //参数类型集合
    private Class<?>[] paramType;
    //请求序号
    private String ReqId;
    //心跳请求
    private Boolean heart;



}
