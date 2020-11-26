# 项目文档 
##Nacos使用
###1.安装
 下载文档解压运行
###2.Nacos作为注册中心
- 1、导入nacos服务注册中心功能的jar包
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```
- 2、开启服务注册发现功能
```java
@EnableDiscoveryClient
@Configuration
public class PmsCloudConfig {}
```

- 3、编写application.properties配置、说明nacos的地址即可
```properties
  spring.cloud.nacos.config.server-addr= 127.0.0.1:8848
```



