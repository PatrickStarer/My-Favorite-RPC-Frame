package serializer;

public interface Serializer {
//     Integer KRYO = 0;
     Integer JSON = 1;
//     Integer HESSIAN = 2;
//     Integer PROTOBUF = 3;

     Integer DEFAULT_SERIALIZER = JSON;

     static Serializer getByCode(int code) {
          switch (code) {
               case 0:

                    return new KryoSerializer();
               case 1:
                    return new JsonSerializer();
//
               case 2:

                    return new HessianSerializer();
               case 3:

                    return new ProtobufSerializer();
               default:
                    return null;
          }
     }

     byte[] toSerializer(Object object);

     Object backSerialize(byte[] bytes,Class<?> clazz);

     int getCode();
}
