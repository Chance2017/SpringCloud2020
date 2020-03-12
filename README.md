# SpringCloud2020
SpringCloud 学习
## 工程运行准备
1. jdk：选用1.8及以上，Settings-Build,Execution,Deployment-Compiler-JavaCompiler选择java8版本；
2. maven：选用3.5及以上；
3. 字符编码：选用UTF8，Settings-Editor-FileEncodings，将其中Global Encodings/Project Encodings/Properties Files Default encoding 都设置为UTF-8编码；
4. 注解生效激活：Settings-Build,Execution,Deployment-Compiler-AnnotationProcessors，勾选Enable annotation processing；
5. 过滤显示Files：Settings-Editor-FileTypes，在下面Ignore files and folders中添加;*.idea,*iml。经过此步骤，idea左边工程栏中，将不再显示这些文件，便于查看，此步可不做。
## 中间件简介
**服务注册与发现**：Eureka，Zookeeper，Consul，Nacos

**负载均衡**：Ribbon

**服务调用**：OpenFeign

**服务熔断**：Hystrix
## Hystrix
### 概述
Hystrix是一个用于处理分布式系统的延迟和容错的











