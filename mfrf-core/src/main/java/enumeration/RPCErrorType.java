package enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RPCErrorType {

    SERVICE_NOT_FOUND("找不到对应的服务"),
    UNKNOWN_ERROR("未知错误"),
    NOT_FOUND_ANNOTATION("缺少ProviderLoader注解");
    private final String msg;

}
