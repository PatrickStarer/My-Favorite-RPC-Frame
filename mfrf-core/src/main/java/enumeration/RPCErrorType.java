package enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RPCErrorType {

    SERVICE_NOT_FOUND("找不到对应的服务");

    private final String msg;

}
