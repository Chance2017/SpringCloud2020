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
**服务注册与发现**：Eureka，Zookeeper，Consul，Nacos  
**负载均衡**：Ribbon  
**服务调用**：OpenFeign  
**服务熔断**：Hystrix
## Hystrix
### 概述
Hystrix是一个用于处理分布式系统的<font color=red>延迟</font>和<font color=red>容错</font>的开源库，在分布式系统里，许多依赖不可避免的会调用失败，比如超时、异常等，Hystrix能够保证在一个依赖的微服务出问题的情况下，<font color=red>不会导致整体服务失败，避免级联付账，以提高分布式系统的弹性</font>。  
"断路器"本身是一种开关装置，当某个服务单元发生故障之后，通过断路器的故障监控（类似熔断保险丝），向调用方返回一个符合预期、可处理的备选响应（FallBack），而不是长时间的等待或者抛出调用方无法处理的异常，这样就保证了服务调用方的线程不会被长时间、不必要地占用，从而避免了故障在分布式系统中的蔓延，乃至雪崩。










