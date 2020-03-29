package com.chance.springcloud.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component
@Slf4j
public class MyLogGatewayFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("come in MyLogGatewayFilter: {}", new Date());
        String username = exchange.getRequest().getQueryParams().getFirst("username");

        if (username == null || username.isEmpty()) {
            log.error("用户名为空，非法用户");
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            // 非法用户，响应错误
            return exchange.getResponse().setComplete();
        }
        // 合法用户，前往下一条过滤链
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
