package util.exception;

import enumeration.RPCErrorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RPCException extends RuntimeException {

    public RPCException(RPCErrorType error){
        super(error.getMsg());

    }
}
