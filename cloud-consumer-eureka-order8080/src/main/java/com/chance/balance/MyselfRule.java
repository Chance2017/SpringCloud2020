package com.chance.balance;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用于修改Ribbon负载均衡的调度规则
 */
@Configuration
public class MyselfRule {
    @Bean
    public IRule getRule() {
        return new RandomRule();    // 定义为随机策略，默认为轮询规则RoundRobinRule()
    }
}
