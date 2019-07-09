package com.crt.common.rpc.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.crt.common.util.RequestBodyUtils;
import com.crt.common.vo.AbstractUser;
import com.crt.common.vo.DefaultAbstractUser;
import com.crt.common.vo.E6WrapperUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * @Author liupengfei@e6yun.com
 * @Date 2018/10/18 13:43
 * @Description 通过filter实现对rpc请求的过滤，不经过controller直接到达service的实现类上，支持其他模块引用本模块rpcService实现自动穿透调用
 * 其他模块在调用这些rpcservice的时候，会根据本地是否有实现，优先用本地实现，如果本地没有，则通过远程方式
 * WebFilter这个注解要起作用，必须在springBoot启动类上加 @ServletComponentScan(basePackages = {"xxx"})
 * 要注意这个filter和shiro的互相影响
 **/
@ConditionalOnProperty(value = "crt.frame.open.cloud",havingValue = "true",matchIfMissing = true)
@Order(-1)
@Component
public class RpcRequestFilter implements Filter {
    static Logger logger = LoggerFactory.getLogger(RpcRequestFilter.class);
    final static String RPC_URL_PRE = "/rpc/";

    static Map<Class, Function<String, Object>> typeFunctionMap = new HashMap();
    static String contentType = "application/json;charset=UTF-8";

    static {
        typeFunctionMap.put(String.class, (value) -> {
            return value;
        });
        typeFunctionMap.put(Integer.class, Integer::parseInt);
        typeFunctionMap.put(Float.class, Float::parseFloat);
        typeFunctionMap.put(Double.class, Double::parseDouble);
        typeFunctionMap.put(Long.class, Long::parseLong);
        typeFunctionMap.put(Boolean.class, Boolean::parseBoolean);
    }

    public RpcRequestFilter() {
        logger.info("RPC过滤器运行...，支持把service暴露为rpc接口");
    }

    @Autowired
    DefaultListableBeanFactory defaultListableBeanFactory;

    /**
     * service名字和spring中bean的对应关系,key为service接口全路径
     */
    static Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    /**
     * url和方法的对应关系,key为url
     */
    static Map<String, Method> methodMap = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String url = ((HttpServletRequest) servletRequest).getRequestURI();
        //rest请求，一般都返回json格式，如果不加这一行，rpc调用方无法正确解析出来
        servletResponse.setContentType(contentType);
        logger.info("request.url={}", url);
        if (!url.startsWith("/rpc")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        //内部rpc调用，任何情况下，都要返回一个E6Wrapper
        String[] serviceNameAndMethod = url.replace(RPC_URL_PRE, StringUtils.EMPTY).split("/");
        logger.info("serviceNameAndMethod={}.{}", serviceNameAndMethod[0], serviceNameAndMethod[1]);
        PrintWriter printWriter = servletResponse.getWriter();
        try {
            Object serviceBean = serviceMap.get(serviceNameAndMethod[0]);
            Method targetMethod = methodMap.get(url);
            //从缓存里如果没有取到，则取一次
            if (targetMethod == null) {
                Class serviceClass = Class.forName(serviceNameAndMethod[0]);
                Map<String, Object> beanMap = defaultListableBeanFactory.getBeansOfType(serviceClass);
                AtomicReference<Object> service = new AtomicReference<>();
                beanMap.entrySet().forEach(objService -> {
                    // 每个rpc接口，springCloud已经自动生成了一个代理，所以这里要过滤到这个自动生成的代理类
                    if (!objService.getValue().getClass().getName().contains("com.sun.proxy.$Proxy")) {
                        service.set(objService.getValue());
                    }
                });
                serviceBean = service.get();
                if (serviceBean == null) {
                    logger.error("没有找到class={}", serviceNameAndMethod[0]);
                    printWriter.write(JSON.toJSONString(E6WrapperUtil.error("没有找到class=" + serviceNameAndMethod[0])));
                    return;
                }
                Method[] methods = serviceClass.getDeclaredMethods();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equals(serviceNameAndMethod[1])) {
                        targetMethod = methods[i];
                        break;
                    }
                }
                //放入map缓存
                serviceMap.put(serviceNameAndMethod[0], serviceBean);
                methodMap.put(url, targetMethod);
            }
            if (targetMethod == null) {
                logger.error("没有找到方法:{}.{}", serviceNameAndMethod[0], serviceNameAndMethod[1]);
                printWriter.write(JSON.toJSONString(E6WrapperUtil.error("没有找到方法:" + serviceNameAndMethod[0] + "." + serviceNameAndMethod[1])));
                return;
            }

            //参数类型
            Class[] parameterTypes = targetMethod.getParameterTypes();
            //参数的枚举
            Type[] genericParameterTypes = targetMethod.getGenericParameterTypes();
            //参数的注解
            Annotation[][] parameterAnnotations = targetMethod.getParameterAnnotations();

            //读取request的参数信息
            Map<String, String[]> parameterMap = servletRequest.getParameterMap();
            //读body体里的内容
            BufferedReader bufferedReader = servletRequest.getReader();
            String bodyStr = RequestBodyUtils.read(bufferedReader);
            //建一个空数组，用来存参数，反射调用方法的时候用到
            Object[] parameters4Method = new Object[parameterTypes.length];
            for (int index = 0; index < parameterAnnotations.length; index++) {
                //判断是否有requestBody
                boolean useRequestBody = false;
                String name = null;
                Annotation[] parameterAnnotation4OneParam = parameterAnnotations[index];
                for (Annotation annotation : parameterAnnotation4OneParam) {
                    if (annotation.annotationType().isAssignableFrom(RequestParam.class)) {
                        RequestParam requestParam = (RequestParam) annotation;
                        name = requestParam.value();
                        break;
                    } else if (annotation.annotationType().isAssignableFrom(RequestBody.class)) {
                        useRequestBody = true;
                        break;
                    }
                }
                //如果没有参数名称，也不是requestBody，则接口定义有问题，无法正确处理
                if (name == null && !useRequestBody) {
                    logger.error("参数至少包含一个注解RequestParam或RequestBody:{}", targetMethod.getName());
                    printWriter.write(JSON.toJSONString(E6WrapperUtil.error("参数至少包含一个注解RequestParam或RequestBody:" + targetMethod.getName())));
                    return;
                }
                String value = bodyStr;
                String[] values = null;
                //如果不是requestBody 则从 参数中取
                if (!useRequestBody) {
                    values = parameterMap.get(name);
                    if (values != null) {
                        value = values[0];
                    }
                }
                Class paramClassType = parameterTypes[index];
                Function<String, Object> dataChangeTypeFunction = typeFunctionMap.get(paramClassType);
                if (dataChangeTypeFunction != null) {
                    //普通参数类型
                    parameters4Method[index] = dataChangeTypeFunction.apply(value);
                    continue;
                } else if (paramClassType.isArray()) {
                    //List没有指定泛型类型 无法解析则直接报错
                    if (useRequestBody) {
                        //RequestBody模式,class.getComponentType() 获取到数组的实际class
                        List list = JSONArray.parseArray(bodyStr, paramClassType.getComponentType());
                        //创建一个数组
                        Object array = Array.newInstance(paramClassType.getComponentType(), list.size());
                        for (int i = 0; i < list.size(); i++) {
                            Array.set(array, i, list.get(i));
                        }
                        parameters4Method[index] = array;
                    } else {
                        //ReqestParam模式
                        String[] arrayParams = value.split(",");
                        Function<String, Object> dataChangeTypeFunction4array = typeFunctionMap.get(paramClassType.getComponentType());
                        if (dataChangeTypeFunction4array == null) {
                            logger.error("不支持的数组类型:{}", paramClassType.getComponentType());
                            printWriter.write(JSON.toJSONString(E6WrapperUtil.error("不支持的数组类型:" + paramClassType.getComponentType())));
                            return;
                        }
                        //创建一个数组
                        Object array = Array.newInstance(paramClassType.getComponentType(), arrayParams.length);
                        for (int i = 0; i < arrayParams.length; i++) {
                            String paramString = arrayParams[i];
                            Array.set(array, i, dataChangeTypeFunction4array.apply(paramString));
                        }
                        parameters4Method[index] = array;
                    }
                    continue;
                } else if (List.class.isAssignableFrom(paramClassType)) {
                    //List没有指定泛型类型 无法解析则直接报错
                    if (Class.class.isAssignableFrom(genericParameterTypes[index].getClass())) {
                        logger.error("List类型参数没有指定泛型类型,{},{}", serviceNameAndMethod[0], serviceNameAndMethod[1]);
                        printWriter.write(JSON.toJSONString(E6WrapperUtil.error("List类型参数没有指定泛型类型:" + serviceNameAndMethod[0] + "." + serviceNameAndMethod[1])));
                        return;
                    }
                    List paramList = this.doList(serviceNameAndMethod, genericParameterTypes, values, value, bodyStr, useRequestBody, index);
                    parameters4Method[index] = paramList;
                    continue;
                } else if (Set.class.isAssignableFrom(paramClassType)) {
                    //List没有指定泛型类型 无法解析则直接报错
                    if (Class.class.isAssignableFrom(genericParameterTypes[index].getClass())) {
                        logger.error("Set类型参数没有指定泛型类型,{},{}", serviceNameAndMethod[0], serviceNameAndMethod[1]);
                        printWriter.write(JSON.toJSONString(E6WrapperUtil.error("Set类型参数没有指定泛型类型:" + serviceNameAndMethod[0] + "." + serviceNameAndMethod[1])));
                        return;
                    }
                    //先转成List然后把List转成Set
                    List paramList = this.doList(serviceNameAndMethod, genericParameterTypes, values, value, bodyStr, useRequestBody, index);
                    parameters4Method[index] = new HashSet<>(paramList);
                    continue;
                } else if (Map.class.isAssignableFrom(paramClassType)) {
                    if (useRequestBody) {
                        Map paramMap = JSON.parseObject(bodyStr, genericParameterTypes[index]);
                        parameters4Method[index] = paramMap;
                    } else {
                        logger.error("Map类型参数只支持reqestBody,{}.{}", serviceNameAndMethod[0], serviceNameAndMethod[1]);
                        printWriter.write(JSON.toJSONString(E6WrapperUtil.error("Map类型参数只支持reqestBody," + serviceNameAndMethod[0] + "." + serviceNameAndMethod[1])));
                        return;
                    }
                    continue;
                } else if (AbstractUser.class.isAssignableFrom(paramClassType)) {
                    parameters4Method[index] = JSON.parseObject(value, DefaultAbstractUser.class);
                } else if (value.indexOf("{") == 0) {
                    //内容包含json的认为是对象，从json反序列化对象
                    parameters4Method[index] = JSON.parseObject(value, parameterTypes[index]);
                    continue;
                } else {
                    logger.error("没有找到类型转换器,{}", parameterTypes[index].getName());
                    printWriter.write(JSON.toJSONString(E6WrapperUtil.error("没有找到类型转换器," + parameterTypes[index].getName())));
                    return;
                }
            }
            Object result = targetMethod.invoke(serviceBean, parameters4Method);
            Class returnType = targetMethod.getReturnType();
            //TODO 返回类型为void，则返回空json，联调的时候要留意测试一下
            if ("void".equals(returnType.getName())) {
                printWriter.write("{}");
            } else {
                printWriter.write(JSON.toJSONString(result));
            }
        } catch (ClassNotFoundException e) {
            logger.error("没有找到class={}", serviceNameAndMethod[0], e);
            printWriter.write(JSON.toJSONString(E6WrapperUtil.error(e.getMessage())));
        } catch (IllegalAccessException e) {
            logger.error("方法签名对不上method={},class={}", serviceNameAndMethod[1], serviceNameAndMethod[0], e);
            printWriter.write(JSON.toJSONString(E6WrapperUtil.error(e.getMessage())));
        } catch (InvocationTargetException e) {
            logger.error("方法内部异常method={},class={}", serviceNameAndMethod[1], serviceNameAndMethod[0], e);
            printWriter.write(JSON.toJSONString(E6WrapperUtil.error(e.getMessage())));
        } catch (Exception e) {
            logger.error("发生异常,url={}", url, e);
            printWriter.write(JSON.toJSONString(E6WrapperUtil.error(e.getMessage())));
        } finally {
            printWriter.flush();
            printWriter.close();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }


    private List doList(String[] serviceNameAndMethod, Type[] genericParameterTypes, String[] values, String value, String bodyStr, boolean useRequestBody, int index) {
        ParameterizedType parameterizedType = (ParameterizedType) genericParameterTypes[index];
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

        Function<String, Object> dataChangeTypeFunction4list = typeFunctionMap.get((Class) actualTypeArguments[0]);
        List paramList = null;
        if (dataChangeTypeFunction4list == null) {
            //复杂数据类型，rpc中用requstBody传递，直接调用json工具反序列化
            paramList = JSONArray.parseArray(value, (Class) actualTypeArguments[0]);
        } else {
            if (useRequestBody) {
                //简单数据类型，rpc中用requstBody传递
                paramList = JSONArray.parseArray(bodyStr, (Class) actualTypeArguments[0]);
            } else {
                //简单数据类型，rpc中用requstParam传递
                paramList = new ArrayList(values.length);
                for (String val : values) {
                    paramList.add(dataChangeTypeFunction4list.apply(val));
                }
            }
        }
        return paramList;
    }

}
