package serializer;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.RPCRequest;
import enumeration.SerializerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.SerializeException;

import java.io.IOException;

public class JsonSerializer implements Serializer{

    private ObjectMapper mapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(JsonSerializer.class);

    @Override
    public byte[] toSerializer(Object object) {
       try{
           return mapper.writeValueAsBytes(object);
        }catch (JsonProcessingException e){
           log.error("json序列化异常");
           throw new SerializeException("json序列化异常");
       }
    }

    @Override
    public Object backSerialize(byte[] bytes, Class<?> clazz) {
        try {
            Object object = mapper.readValue(bytes, clazz);
            //如果是 请求类型
            if(object instanceof RPCRequest)
                object = jsonHandler(object);
            return object;
        } catch (IOException e) {
            log.error("json反序列化时有错误发生:", e);
            throw new SerializeException("json反序列化时有错误发生");
        }
    }

    @Override
    public int getCode() {
        return SerializerType.valueOf("JSON").getCode();
    }

    //这是因为JSON是一种面向文本的数据格式，它只能表示简单数据类型（如字符串、数字、布尔值等）和一些复杂数据类型（如对象、数组等）。
    // 在序列化和反序列化Object数组时，如果数组中包含了不同类型的实例，JSON序列化通常只会将这些实例转换为它们的默认toString()方法返回的字符串形式 ，
    //反序列化时再将其转换回相应的类型。这可能会导致一些信息丢失或类型转换错误。
    // 因此，在使用JSON序列化和反序列化时，我们应该尽可能避免使用Object类型，而是尽可能使用具体的类类型。

    private  Object jsonHandler(Object object) throws IOException{
        RPCRequest rpcRequest = (RPCRequest) object;
        //对请求的参数数组遍历
        for(int i=0;i<rpcRequest.getParamType().length;i++){
            Class<?> clazz = rpcRequest.getParamType()[i];
            //判断 a 是否 为b的父类或者接口 或者 是同类 是返回true
           //判断参数是否来源于参数类型 如果不是 将参数序列话为数组 再反序列化为String
            if(!clazz.isAssignableFrom(rpcRequest.getParams()[i].getClass())){
                byte[] bytes = mapper.writeValueAsBytes(rpcRequest.getParams()[i]);
                rpcRequest.getParams()[i]= mapper.readValue(bytes, clazz);
            }
        }
        return rpcRequest;
    }
}

//假设有一个Animal类和其子类Dog、Cat，它们都有一个name属性。现在我们用JSON序列化将一个包含多个Animal对象的数组转换为字符串，
// 并将其反序列化回来：
//Animal[] animals = {new Dog("Fido"), new Cat("Whiskers")};
//String jsonStr = new Gson().toJson(animals);
//Animal[] deserializedAnimals = new Gson().fromJson(jsonStr, Animal[].class);
// 这时，我们发现deserializedAnimals数组中的元素虽然是Animal类型，但实际上只是它们的toString()方法返回的字符串，
// 而不是真正的Dog或Cat对象。这是因为JSON序列化无法区分不同的子类类型，所以反序列化后的结果只能是父类Animal类型。
// 如果我们在反序列化后尝试对某个元素调用子类特有的方法，就会出现信息丢失和类型转换错误。