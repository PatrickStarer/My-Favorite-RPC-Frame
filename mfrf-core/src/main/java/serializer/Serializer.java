package serializer;

public interface Serializer {

     byte[] toSerializer(Object object);

     Object backSerialize(byte[] bytes,Class<?> clazz);


}
