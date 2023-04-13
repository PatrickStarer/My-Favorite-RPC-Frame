package util;

import java.util.HashMap;
import java.util.Map;

public  class Singleton {
    private static Map<Class<?>, Object> map = new HashMap<>();

    private Singleton() {

    }

    public static <T> T getObject(Class<T> clazz) {
        Object object = map.get(clazz);
        if (object == null) {
            synchronized (Singleton.class) {
                if (object == null) {
                    try {
                        object = clazz.newInstance();
                        map.put(clazz, object);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

//      cast()方法  将obj转化成T类型。
        return clazz.cast(object);

    }
}
