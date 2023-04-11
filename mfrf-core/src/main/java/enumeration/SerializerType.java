package enumeration;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SerializerType {

    KRYO(0),
    JSON(1),
    HESSIAN(2),
    PROTOBUF(3);
    private final int code;

}
