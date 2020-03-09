package com.chance.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MyLoadBalancer implements LoadBalancer {

    private AtomicInteger count = new AtomicInteger(0);

    public final int getAndIncrement() {
        int current;
        int next;
        do {
            current = this.count.get();
            next = current >= 0x7fffffff ? 0 : current + 1;
        } while (!this.count.compareAndSet(current, next));
        System.out.println("累加次数：" + next);
        return next;
    }

    @Override
    public ServiceInstance getInstance(List<ServiceInstance> serviceInstances) {
        int index = getAndIncrement() % serviceInstances.size();
        return serviceInstances.get(index);
    }
}
