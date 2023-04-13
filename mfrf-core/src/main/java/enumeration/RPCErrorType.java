package enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RPCErrorType {

    SERVICE_NOT_FOUND("找不到对应的服务"),
    UNKNOWN_ERROR("未知错误"),
    NOT_FOUND_ANNOTATION("缺少ProviderLoader注解"),
    MAGIC_ERROR("魔数错误"),
    MESSAGE_ERROR("数据类型错误"),
    NOT_FOUND_SERIALIZER("没有对应的序列化方法"),
    NACOS_ERROR("连接Nacos发生错误"),
    REGISTRY_ERROR("服务注册出现异常");

    private final String msg;

}
