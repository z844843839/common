# 负载均衡器
主要解决本地单模块开发 可以控制服务路由的问题

## 使用方法：

### 一、配置文件
   - linux配置文件：/opt/settings/e6RpcConfig.properties
   - windows配置文件：C:/opt/settings/e6RpcConfig.properties
   - 注意：文件名大小写，请按照这个标准来
   - 配置文件格式如下：
 ```
     #providerA=172.16.56.66,172.16.56.66
     providerA=172.16.56.57:8081
     bbb=172.16.56.2
     ccc=172.16.56.3
   ```
   - 配置文件加载是用java的Properties类加载，所以注释可以用“#”
   - key为服务名，value为期望的IP
   - ip后如果加端口，则是指定机器端口访问，程序不会再从eurekaServer返回的服务清单中选择，而是直接访问(未注册也无妨)
   - value支持多个IP逗号分隔
 
###  二、maven依赖 （目前仅支持springBoot工程）
 ```
            <dependency>
                   <groupId>com.crt</groupId>
                   <artifactId>common-loadbalance</artifactId>
                   <version>1.0.0-SNAPSHOT</version>
           </dependency>
 ```

### 三、开启配置(默认关闭)
  ```
    crt.rpc.config.local.enable=true
  ```

### 四、遗留问题
 - 暂时只在本地服务调用时生效，对于多级的调用链中未传递，传递思路：需要把本地配置通过header传递给下一个服务
 


 
