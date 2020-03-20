package com.chance.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();
        routes.route("route_chance1", r -> r.path("/guonei").uri("http://news.baidu.com"));
        routes.route("route_chance2", r -> r.path("/guoji").uri("http://news.baidu.com"));
        return routes.build();
    }
}
