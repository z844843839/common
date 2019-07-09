# 开发调试工具
主要解决本地开发提升效率问题

## 功能如下：

### ADevAbstractUserServiceImpl的作用
   - 所有url前面加上springApplicationName，方便本地和前端联调
   - 开发默认用户

#### 使用方法

   - pom引入方法
   ```
        <dependency>
            <groupId>com.crt</groupId>
            <artifactId>common-devtools</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
   ```

   -  resources目录下新增文件devUsers.txt,文件内容格式如下：
    
 ```
     {"userId":1,"webgisUserId":1,"corpId":1,"orgId":1,"parentId":1}
     {"userId":2,"webgisUserId":2,"corpId":2,"orgId":2,"parentId":2}
     {"userId":3,"webgisUserId":3,"corpId":3,"orgId":3,"parentId":3}
     {"userId":4,"webgisUserId":4,"corpId":4,"orgId":4,"parentId":4}
   ```
   -  配置让这个类生效（推荐这两行配置放到application-dev.properties文件中）
   ```
        crt.frame.user.session=local
   ```
   -  指定默认用户
   ```
       crt.frame.user.dev.userId=3
   ```
   - 开发中如果要修改这个默认用户，可以通过swagger直接访问ADevAbstractUserServiceImpl这个类的接口getCurrentUser查询当前默认用户ID，也可以通过refreshUserId来修改默认用户ID而无需重启应用
   
   - 其他说明
     - 这个类承担了两个角色，一是AbstractUserService接口的实现，另一个是作为一个Controller
     - 这个类上面加了ConditionalOnProperty，配置如下：
   ```
          @ConditionalOnProperty(
                  value = {"crt.frame.user.session"},
                  havingValue = "local",
                  matchIfMissing = false
          )
   ```
   

 
