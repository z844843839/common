package com.crt.common.logPointcut;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.crt.common.constant.Constants;
import com.crt.common.util.UserInfoUtil;
import com.crt.middle.base.MessageQueueService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常日志切入类
 *
 * @author malin
 */
@Aspect
@Component
public class ExceptionLogAspect {
    private static final Logger log = LoggerFactory.getLogger(ExceptionLogAspect.class);

    @Autowired
    private MessageQueueService messageQueueService;

    /**
     * 设置操作异常切入点记录异常日志 扫描所有controller包下操作
     */
    @Pointcut("execution(public * com.crt..*Controller.*(..))")
    public void operExceptionLogPoinCut() {

    }


    /**
     * 设置操作异常切入点记录异常日志 扫描所有service包下操作
     */
    @Pointcut("execution(public * com.crt..service..*.*(..))")
    public void operExceptionLogServicePoinCut() {

    }

   @AfterThrowing(pointcut = "operExceptionLogServicePoinCut()", throwing = "e")
    public void doRpcAfterThrowing(JoinPoint joinPoint, Throwable e) {
        JSONObject logJson = new JSONObject();
        String logKey = "exceptionLog";
        logJson.put("logType", logKey);
        try {

            try {
                // 获取RequestAttributes
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                // 从获取RequestAttributes中获取HttpServletRequest的信息
                HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
                //异常请求
                String exceptionUrl = request.getRequestURL().toString();
                logJson.put("exceptionUrl", exceptionUrl);
                //异常模块
                String module = request.getRequestURI();
                module = module.substring(1, module.length());
                module = module.substring(0, module.indexOf("/"));
                logJson.put("exceptionModule", module);
            } catch (Exception e1) {}

            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            //异常类名
            logJson.put("exceptionClass", className);
            // 获取请求的方法名
            String methodName = method.getName();
            //异常方法
            logJson.put("exceptionMethod", methodName);
            // 请求的参数
            String params = JSON.toJSONString(joinPoint.getArgs());
            //异常参数
            logJson.put("exceptionParam", params);
            //转换异常信息为字符串
            String exceptionMsg = stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace());
            //异常信息
            logJson.put("exceptionMsg", exceptionMsg);

            try {
                //当前操作用户编码
                logJson.put("operaterCode", UserInfoUtil.getLoginUserCode().toString());
                //当前操作用户姓名
                logJson.put("operaterName", UserInfoUtil.getLoginUserRealName());
            } catch (Exception e1) {
                //当前操作用户编码
                logJson.put("operaterCode", "0");
                //当前操作用户姓名
                logJson.put("operaterName", "系统管理");
            }

            //当前操作时间
            logJson.put("createdAt", new Date());
            /*==========数据库日志=========*/
            try {
                new Thread( () ->{
                    messageQueueService.send("crt_e6_log_exchange", "crt_e6_log_routingkey", logJson.toString());
                }).start();
            } catch (NullPointerException en) {
            }
        } catch (Exception e1) {
            log.error("日志记录异常{}", e1.getMessage());
        }
    }


    /**
     * @Description 异常通知 用于拦截service层记录异常日志
     */
   // @AfterThrowing(pointcut = "operExceptionLogPoinCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        JSONObject logJson = new JSONObject();
        String logKey = "exceptionLog";
        logJson.put("logType", logKey);
        try {
            //异常请求
            String exceptionUrl = request.getRequestURL().toString();
            logJson.put("exceptionUrl", exceptionUrl);
            //异常模块
            String module = request.getRequestURI();
            module = module.substring(1, module.length());
            module = module.substring(0, module.indexOf("/"));
            logJson.put("exceptionModule", module);
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            //异常类名
            logJson.put("exceptionClass", className);
            // 获取请求的方法名
            String methodName = method.getName();
            //异常方法
            logJson.put("exceptionMethod", methodName);
            // 请求的参数
            Map<String, String> rtnMap = converMap(request.getParameterMap());
            // 将参数所在的数组转换成json
            String params = JSON.toJSONString(rtnMap);
            //异常参数
            logJson.put("exceptionParam", params);
            //转换异常信息为字符串
            String exceptionMsg = stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace());
            //异常信息
            logJson.put("exceptionMsg", exceptionMsg);
            //当前操作用户编码
            logJson.put("operaterCode", UserInfoUtil.getLoginUserCode().toString());
            //当前操作用户姓名
            logJson.put("operaterName", UserInfoUtil.getLoginUserRealName());
            //当前操作时间
            logJson.put("createdAt", new Date());
            /*==========数据库日志=========*/
            try {
                new Thread( () ->{
                    messageQueueService.send("crt_e6_log_exchange", "crt_e6_log_routingkey", logJson.toString());
                }).start();
            } catch (NullPointerException en) {
            }
        } catch (Exception e1) {
            log.error("日志记录异常{}", e1.getMessage());
        }
    }

    /**
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     */
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<>(Constants.HASHMAP_DEFAULT_SIZE);
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    /**
     * 转换异常信息为字符串
     *
     * @param exceptionName    异常名称
     * @param exceptionMessage 异常信息
     * @param elements         堆栈信息
     */
    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuffer strbuff = new StringBuffer();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet + "\n");
        }
        String message = exceptionName + ":" + exceptionMessage + "\n\t" + strbuff.toString();
        message=message.length()>2000?message.substring(0,2000):message;
        return message;
    }
}
