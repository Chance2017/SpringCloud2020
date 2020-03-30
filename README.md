# SpringCloud2020
SpringCloud 学习
## 工程运行准备
1. jdk：选用1.8及以上，Settings-Build,Execution,Deployment-Compiler-JavaCompiler选择java8版本；
2. maven：选用3.5及以上；
3. 字符编码：选用UTF8，Settings-Editor-FileEncodings，将其中Global Encodings/Project Encodings/Properties Files Default 
encoding 都设置为UTF-8编码；
4. 注解生效激活：Settings-Build,Execution,Deployment-Compiler-AnnotationProcessors，勾选Enable annotation processing；
5. 过滤显示Files：Settings-Editor-FileTypes，在下面Ignore files and folders中添加;*.idea,*iml。经过此步骤，idea左边工程栏中，
将不再显示这些文件，便于查看，此步可不做;
6. 修改hosts：为了在本地测试eureka服务集群，需要在本地hosts文件中添加两行："127.0.0.1 eureka7001.com"、 "127.0.0.1 eureka7002.com"。
## 中间件选择
**服务注册与发现**：Eureka(停更)，Zookeeper，Consul，Nacos  
**负载均衡**：Ribbon  
**服务调用**：OpenFeign  
**服务熔断**：Hystrix(停更)  
**服务网关**：Gateway
## 组件选型
![avatar](https://github.com/Chance2017/SpringCloud2020/blob/master/images/SpringCloud组件选型.png)
## Hystrix(停更)
### 概述
Hystrix是一个用于处理分布式系统的**延迟**和**容错**的开源库，在分布式系统里，许多依赖不可避免的会调用失败，比如超时、异常等，Hystrix能够保证在一个依赖的微服务出问题的情况下，**不会导致整体服务失败，避免级联付账，以提高分布式系统的弹性**。  
"断路器"本身是一种开关装置，当某个服务单元发生故障之后，通过断路器的故障监控（类似熔断保险丝），向调用方返回一个符合预期、可处理的备选响应（FallBack），而不是长时间的等待或者抛出调用方无法处理的异常，这样就保证了服务调用方的线程不会被长时间、不必要地占用，从而避免了故障在分布式系统中的蔓延，乃至雪崩。
### 应用
1. 服务降级(fallback)  
例如服务器忙时，应该向客户端返回请稍后再试的友好提示，而不是让客户端一直等待，这就是服务降级。  
以下情况会出现服务降级：(1) 程序运行异常 (2) 超时 (3) 服务熔断触发服务降级 (4) 线程池/信号量打满也会导致服务降级。
2. 服务熔断(break)  
例如，家用保险丝达到最大负荷后跳闸，当服务量达到最大后，直接拒绝访问，然后调用服务降级的方法进行友好提示，进而实行熔断，最后恢复调用链路。
3. 服务限流(flowlimit)  
例如，秒杀系统的高并发操作，严禁一窝蜂地拥挤过来，大家排队，一秒钟N个，有序进行。
## Gateway
### 概述
在1.x版本中，服务网关多使用Zuul1.x非Reactor模式的老版本做网关，2.x之后选择使用Gateway替代Zuul。而之所以选择Gateway的原因在于：
* Zuul1.x属于Netflix，其已经进入维护阶段（虽然Netflix早就发布了最新的Zuul2.x，但SpringCloud貌似并没有整个计划，而且Netflix很多组件
都进入维护期），而Gateway是SpringCloud团队自己研发的，整合度更高，值得信赖；  
* Zuul1.x是基于阻塞IO的，而Gateway是基于异步非阻塞模型进行开发的，性能方面不用担心，RPS（每秒请求数）是Zuul的1.6倍；
* SpringCloud Gateway还支持WebSocket，并且与Spring紧密继承拥有更好的开发体验。
Spring Cloud Gateway优势在于其建立在SpringBoot2.x，Spring WebFlux和Project Reactor等新技术之上，是原zuul1.x版本的替代，它旨在为
微服务架构提供一种简单有效的统一的API路由管理方式。为了提升网关的性能，SpringCloud Gateway是基于WebFlux框架实现的，而WebFlux框架底
层则使用了高性能的Reactor模式通信框架Netty。  
SpringCloud Gateway的目标是提供统一的路由方式且基于Filter过滤链的方式提供网关的基本功能，例如：反向代理，鉴权，监控/指标，熔断，限流等。  
### 核心概念解释 
**Route（路由）**：路由是网关的基本单元，由ID、URI、一组Predicate、一组Filter组成，根据Predicate进行匹配转发。  
**Predicate（谓语、断言）**：路由转发的判断条件，目前SpringCloud Gateway支持多种方式，常见如：Path、Query、Method、Header等。  
**Filter（过滤器）**：过滤器是路由转发请求时所经过的过滤逻辑，可用于修改请求、响应内容。  
### 路由与断言配置方法
#### yml配置(首选)
```yaml
spring:
  cloud:
    gateway:
      routes: # 路由
        - id: payment_route1          # 路由的ID，没有固定规则，但要求唯一，建议配合服务名
#          uri: http://localhost:8001  # 匹配后提供服务的路由地址
          uri: lb://cloud-payment-service # 动态匹配注册中心中的路由地址
          predicates: # 断言匹配条件
            - Path=/payment/get/**    # 路径相匹配的进行路由

        - id: payment_route2
#          uri: http://localhost:8001
          uri: lb://cloud-payment-service # 动态匹配注册中心中的路由地址
          predicates: # 断言
            - Path=/payment/lb/**
            - After=2020-03-26T21:34:13.609+08:00[Asia/Shanghai]
#            - Cookie=username,chance  # 设置Cookie限制，值分别是属性名称和正则匹配表达式

```
#### 代码配置
添加Spring配置类
```java
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
```
### 过滤器配置方法
新建一个过滤器类，通过@Component注解添加到Spring容器中，并实现GlobalFilter, Ordered接口。
```java
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
```


