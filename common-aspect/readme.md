
切面封装
 - 提供了两种使用场景：
    - 代替型，接口 com.crt.common.aspect.base.E6InterceptorOfReplace
    - 包含型，接口 com.crt.common.aspect.base.E6InterceptorOfContain
- 包含型使用封装场景示例：操作日志，com.crt.common.aspect.log 包下两个类
 - 代替型使用封装场景示例：功能扩展，com.crt.common.aspect.proxy包下两个类

 
 - 静态扩展（编译时决定）
 > 增加一个类，比如 XXXServiceImpl.method1(Object args...), 扩展方法 XXXServiceImplProxy.method1(Object args...)
 > bean名称匹配： XXXServiceImpl --> XXXServiceImplProxy
 > 方法名和参数列表匹配
 
 - 动态扩展（运行时决定）
 > 增加一个类，比如 XXXServiceImpl, 扩展类 XXXServiceImplRouter
 > bean名称匹配：XXXServiceImpl --> XXXServiceImplRouter
 > 上下文和参数路由，XXXServiceImplRouter的getProxy方法中决定返回具体的代理类
 > 代理类只需要重写需要扩展的方法，方法名和参数签名必须与被代理对象保持一致