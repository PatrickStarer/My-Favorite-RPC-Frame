package scan;

import annotation.Provider;
import annotation.ProviderLoader;
import enumeration.RPCErrorType;
import exception.RPCException;
import nacos.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.ServiceProvider;
import util.Reflect;

import java.net.InetSocketAddress;
import java.util.Set;

public class ProviderScan {
    private Logger log = LoggerFactory.getLogger(ProviderScan.class);
    private String host;
    private Integer port;
    //本地服务
    private ServiceProvider serviceProvider = new ServiceProvider();
    //nacos远程服务
    private Registry registry = new Registry();

    public void scanProvider(Integer port,String host){
        this.host = host;
        this.port =port;
        //org.mfrf.ProviderStart
        String mainClassName = Reflect.getStack();
        Class<?> startClass;
        // 判断 启动类有没有带注解
        try{
            //class org.mfrf.ProviderStart
            startClass = Class.forName(mainClassName);
            if(!startClass.isAnnotationPresent(ProviderLoader.class)){
                log.info("启动类缺少ProviderLoader注解");
                throw new RPCException(RPCErrorType.NOT_FOUND_ANNOTATION);
            }
        }catch (ClassNotFoundException e){
            log.error("扫描注解出现错误");
            throw new RPCException(RPCErrorType.NOT_FOUND_ANNOTATION);
        }
        // 得到启动类所在的包
        String basePackage =startClass.getAnnotation(ProviderLoader.class).value();

        if("".equals(basePackage)){
            //class org.mfrf
            basePackage = mainClassName.substring(0,mainClassName.lastIndexOf('.'));
        }

        // class org.mfrf.impl.UserServiceImpl  、 class org.mfrf.ProviderStart
        Set<Class<?>> classes = Reflect.getClass(basePackage);

        //对启动类所在的包及子包进行 注解扫描 发现有server注解的服务提供者
        for (Class<?> clazz : classes) {
            //判断有没有 Provider注解
            if(clazz.isAnnotationPresent(Provider.class)){
                String serviceName = clazz.getAnnotation(Provider.class).name();
                Object object;
                try{
                    object = clazz.newInstance();
                }catch (Exception e){
                    log.error("创建---"+serviceName+"---服务实例发生错误");
                    continue;
                }

                //如果没有服务名
                if("".equals(serviceName)){
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> clazzInterface : interfaces) {
                        //使用 该实现了接口的 全路径名 代替
                        putService(object,clazzInterface.getCanonicalName());
                    }
                }else{
                    putService(object,serviceName);
                }
            }
        }
    }
    private <T> void putService(T service,String serviceName){
        //添加服务提供者
        serviceProvider.addProvider(service,serviceName);
        registry.registry(serviceName,new InetSocketAddress(host,port));
    }





}
