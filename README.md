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
## SpringCloud Config
### 概述
SpringCloud Config为微服务架构中的微服务提供集中化的外部配置支持，配置服务器为各个不同微服务应用的所有环境提供一个中心化的外部配置。
### 服务端配置公共配置文件的git地址
```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/zzyybs/springcloud-config.git
          search-paths:
            - springcloud-config
      label: master # git分支
```
### 配置读取规则
将配置中心服务启动后，即可通过http://{服务地址:端口号}/{配置读取规则}，来获取配置信息。
常用的配置服务规则有：
* /{label}/{application}-{profile}.yml(推荐) -> http://localhost:3344/master/config-dev.yml
* /{application}-{profile}.yml -> http://localhost:3344/config-test.yml(默认读取master分支)
* /{application}/{profile}[/{label}](返回json串) -> http://localhost:3344/config/dev/master  
其中：label表示分支，application表示应用名称，profile表示环境。
### 客户端配置
客户端的配置文件需要命名为bootstrap.yml，这样会具有最高优先级。
```yaml
spring:
  cloud:
    # 客户端Config配置
    config:
      label: master # 分支名称
      name: config  # 配置文件名称
      profile: test # 配置文件后缀名称（可以设置为部署环境）
      uri: http://localhost:3344  # 配置中心地址
```
### 客户端动态刷新
避免每次更新配置信息后，都要重启客户端的微服务3355
* controller组件上添加@RefreshScope注解
* 添加配置
```yaml
# 暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: "*"
```
* 发送一次post请求去拉取最新配置：curl -X POST "http://localhost:3355/actuator/refresh"
注：这需要为每个客户端都发送一次post请求，以使得每个客户端都重新拉取最新配置，为避免这种问题，将在下节引入bus总线系统，来完成一次刷新，处处生效功能。
## SpringCloud Bus
### 概述
SpringCloud Bus是用来将分布式系统的节点与轻量级消息系统链接起来的框架，它整合了Java的事件处理机制和消息中间件的功能。SpringCloud Bus目前支持RabbitMQ和Kafka。
### rabbitmq安装
这里使用docker安装rabbitmq，通过以下命令可以完成rabbitmq的安装与启动，此时通过浏览器访问15672端口即可访问到rabbitmq的控制台页面，默认账户名和密码都是guest。
```shell script
# centos7下测试
systemctl start docker  # 启动docker
docker ps (-a)          # 查看运行中的docker容器，-a选项可以查看创建的所有容器
docker search rabbitmq  # 搜索出相关的rabbitmq镜像
docker pull rabbitmq:3.8.3-management # 冒号后指定版本号，management后缀的一般是带有控制台的版本
docker images           # 查看所有本地已下载的docker镜像
docker run -d --name test_rabbitmq_management -p 15672:15672 -p 5672:5672 [IMAGE_ID]  # 创建容器并启动，--name 设置容器名称，-p 指定端口映射，格式为：主机(宿主)端口:容器端口，[IMAGE_ID] 指定要启动的镜像id
```
以上是安装在linux系统的虚拟机下，当在本地的windows系统下访问该接口时需要关闭防火墙，或者开放15672端口。同时，还需要修改vmware虚拟机的网络设置，编辑->虚拟网络编辑器->NAT模式->更改设置->NAT设置->添加端口映射。
```shell script
service firewalld start   # 开启
service firewalld restart # 重启
service firewalld stop    # 关闭

firewall-cmd --list-all   # 查看防火墙规则

firewall-cmd --query-port=15672/tcp              # 查询端口是否开放
firewall-cmd --permanent --add-port=15672/tcp    # 开放80端口
firewall-cmd --permanent --remove-port=15672/tcp # 移除端口

firewall-cmd --reload     # 重启防火墙(修改配置后要重启防火墙)
```
### 相关配置
客户端的配置文件需要命名为bootstrap.yml，这样会具有最高优先级。
```yaml
# RabbitMQ相关配置(在配置中心服务端和客户端都要配置)
spring:
  rabbitmq:
    host: 192.168.204.128
    port: 15672
    username: guest
    password: guest

# rabbitmq相关配置，暴露bus刷新配置的端点（只需要在配置中心服务端配置即可）
management:
  endpoints:  # 暴露bus刷新配置的端点
    web:
      exposure:
        include: 'bus-refresh'
```
### 刷新配置
这里只需要在配置中心服务端发送一次post即可完成，所有服务端和客户端的配置刷新。  
刷新命令：curl -X POST "http://[配置中心服务端地址:端口号]/actuator/bus-refresh"
### 定点通知
bus可以选择只通知某一些客户端而不是广播给所有客户端。  
为了达到此功能，只需要在刷新命名后面添加上指定的客户端微服务名称(在配置文件中指定spring.application.name)和端口号：curl -X POST "http://[配置中心服务端地址:端口号]/actuator/bus-refresh/[客户端应用程序名称]:[某一客户端端口号]"