package enumeration;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {

    REQUEST(0),
    RESPONSE(1);
    private final int code;

}
