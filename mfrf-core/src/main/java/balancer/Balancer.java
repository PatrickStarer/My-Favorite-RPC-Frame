package balancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

public interface Balancer {
    public Instance select(List<Instance> instances);
}
