# SpringCloud2020
SpringCloud 学习
## 工程运行准备
1. jdk：选用1.8及以上，Settings-Build,Execution,Deployment-Compiler-JavaCompiler选择java8版本；
2. maven：选用3.5及以上；
3. 字符编码：选用UTF8，Settings-Editor-FileEncodings，将其中Global Encodings/Project Encodings/Properties Files Default 
encoding 都设置为UTF-8编码；
4. 注解生效激活：Settings-Build,Execution,Deployment-Compiler-AnnotationProcessors，勾选Enable annotation processing；
5. 过滤显示Files：Settings-Editor-FileTypes，在下面Ignore files and folders中添加;*.idea,*iml。经过此步骤，idea左边工程栏中，
将不再显示这些文件，便于查看，此步可不做。
## 中间件选择
**服务注册与发现**：Eureka(停更)，Zookeeper，Consul，Nacos  
**负载均衡**：Ribbon  
**服务调用**：OpenFeign  
**服务熔断**：Hystrix(停更)
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









