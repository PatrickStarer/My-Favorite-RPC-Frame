package enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseStatusCode {

    SUCCESS(200, "调用成功"),
    FAIL(500, "调用失败"),
    NOT_FOUND_METHOD(604, "未找方法"),
    NOT_FOUND_CLASS(605, "未找类");

    private final int code;
    private final String message;


}
