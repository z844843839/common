# 通用国际化支持
我们目前采用前后台分离的开发模式，页面内容的国际化由前端框架完成，那么后台返回的特定字符串如果国际化，就需要后台国际化方案了。

spring本来就有比较完善的i8n体系支持，但是基于静态配置文件，本项目要解决的问题就是增加从数据库动态加载的功能，支持程序不重启的情况下增加新的语言和值。

## 使用方法：

### 一、数据库建表（MYSQL）
 ```
 CREATE TABLE `i18n_message` (
     `code` char(50) COLLATE utf8mb4_unicode_ci NOT NULL,
     `zh-CN` char(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
     `zh-TW` char(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
     `en-US` char(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
     PRIMARY KEY (`code`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
   ```
   - 注意1：表名称默认为i18n_message，如果改表名，需要通过配置修改查询SQL
   - 注意2：表中第一个字段为变量的名称，默认为code，如果修改，需要通过配置修改这个值
   - 注意3：表无特殊要求，所有字段为字符串即可，需要有一列为主键（用于和程序中传递的变量对应），其他列均对应各语言-地区对应的值，大小写不敏感
 
###  二、maven依赖 （目前仅支持springBoot工程）
 ```
            <dependency>
               <groupId>com.crt</groupId>
                <artifactId>common-i18n</artifactId>
                <version>1.0.0-SNAPSHOT</version>
           </dependency>
 ```

### 三、项目里增加配置类
  ```
    @Configuration
    public class I18nConfiguration {
        @Bean
        public E6I18nDataLoadService initE6I18nDataLoadService(DataSource dataSource){
            DefaultE6I18nDataLoadServiceImpl defaultE6I18nDataLoadService = new DefaultE6I18nDataLoadServiceImpl();
            defaultE6I18nDataLoadService.setDataSource(dataSource);
            //如果修改表名称，请在这里修改（大小写按照数据库中实际表名来设置）
            //defaultE6I18nDataLoadService.setSql("select * from myTable");
            //如果修改表主键名称，请在这里修改（大小写不敏感）
            //defaultE6I18nDataLoadService.setColumnNameCode("myCode");
            return defaultE6I18nDataLoadService;
        }
    }
  ```
  - 注意这里的DataSource，是需要对应上面建表的数据源，如果用了多数据源，需要用@Qualifier("primaryDataSource") DataSource dataSource来明确指定用的哪个，不然就用primary的了
 
### 四、使用示例
   ```
   #原来的写法
   #可能字符串是写死的
   infos.put("message", "密码不正确");
   #使用国际化的写法
   #只需要把字符串用工具方法com.crt.common.i18n.I18nDictUtil.getI18nValue包装一次
   infos.put("message", I18nDictUtil.getI18nValue("密码不正确"));
   #其实就需要在i18n_message表中有一个code值为“密码不正确”的数据，框架会自动去找对应的其他语言值。
   ```
### 五、配置说明
    ```
    #共2个配置项
    # 配置缓存时间,可以忽略，默认1小时
    e6.i18n.cacheTimeMs = 600000
    # 刷新数据库的时间，需要springBoot工程开启@EnableScheduling，默认值为1小时一次
    e6.i18n.reload.cron = 0 0 0/1 * * ?
     ```
### 六、实现思路
 - 语言类型的定义是包含两部分，语言-地区，比如中文简体是 zh-cn,繁体是 zh-tw,英文美国是en-us
 - i18n_message表中，除了主键外，其他列名称都是 语言-地区的格式，可以根据自己需求增加新的语言类型
 - 程序会从i18n_message表中加载所有数据到内存中
 - com.e6yun.project.common.i18n.I18nDictUtil类中注册了一个拦截器，主要是把当前请求的语言类型写入ThreadLocal中
 - 通过I18nDictUtil.getI18nValue("myKey")方法，会从ThreadLocal中获取当前请求的语言地区信息，按照以下次序取值：
  - 第一步：从数据库结果中获取 语言-地区 的对应的值，如果没有或者为空，则下一步
  - 第二步：从数据库结果中获取 语言 的对应值，（举例：如果en-us没有找到，则找en的），如果没有或者为空，则下一步
  - 第三步：如果没有找到且不是默认语言包，则从数据库结果取默认语言对应的值，如果没有或者为空，则下一步
  - 第四步：尝试从本地配置文件加载期望的语言-地区&语言是否有值，如果没有或者为空，则下一步
  - 第五步：如果没有找到且不是默认语言包，尝试从本地配置文件加载默认的语言 是否有值，如果没有或者为空，则下一步
  - 第六步：返回key值 （也就是说，如果以上都找不到，就会返回传入的变量本身，所以这个传入的key值尽量有明确含义）

### 七、扩展性
  - 如何增加自定义的加载模式？
    - 只需要增加一个com.e6yun.project.common.i18n.E6I18nDataLoadService这个接口的实现，返回List<E6I18nDictByLocaleEntity>格式数据，并把这个新定义的类加入spring容器即可，程序会自动扫描到这个类并把这个结果纳入到内存里的国际化里来。
  - 如何手动触发国际化内存刷新？
    - 需要从内存中获取com.e6yun.project.common.i18n.E6MessageResource这个类的实例，执行reload()方法即可。

### 八、遗留问题
 - I18nDictUtil类中获取语言类型的方法，是来自请求header里的信息，应该先判断cookie中是否有自定义的语言类型（比如用户从界面选择了语言类型，就需要通过cookie来传递）
 - 并未在除了mysql之外的数据源上测试，如果有同学测试成功，请告诉我
 
### 九、参考资料
 - chrome浏览器如何设置语言 https://jingyan.baidu.com/article/ca00d56c28d450e99eebcfdb.html
 - chrome修改完语言后需要将当前语言置顶
 
 
### 十、关闭国际化自动加载方案
  - crt.common.i18n.enable=false

 
