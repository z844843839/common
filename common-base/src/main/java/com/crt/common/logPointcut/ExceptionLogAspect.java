package com.crt.common.logPointcut;

import com.alibaba.fastjson.JSONObject;
import com.crt.common.util.UserInfoUtil;
import com.crt.middle.base.MessageQueueService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

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
     * Service层切点
     */
    @Pointcut("execution(public * com.crt.*.*.*.service.*.*(..))")
    public void serviceAspect(){
    }


    /**
     * @Description  异常通知 用于拦截service层记录异常日志
     */
    @AfterThrowing(pointcut = "serviceAspect()",throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e){
        try {
            JSONObject logJson = new JSONObject();
            String logKey = "exceptionLog";
            logJson.put("logType",logKey);
            logJson.put("exceptionMethod", (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            logJson.put("exceptionCode",e.getClass().getName());
            logJson.put("exceptionMsg",getExceptionAllinformation(e));
            //当前操作用户
            logJson.put("operaterCode",UserInfoUtil.getLoginUserCode().toString());
            logJson.put("operaterName",UserInfoUtil.getLoginUserRealName());
            logJson.put("createdAt",new Date());
            /*==========数据库日志=========*/
            try {
                messageQueueService.send("crt_e6_log_exchange","crt_e6_log_routingkey",logJson.toString());
            }catch (NullPointerException en){
            }
        } catch (Exception e1) {
            log.error("日志记录异常{}",e1.getMessage());
        }
    }

    /**
     * 输出详细异常信息
     * @param e
     * @return String
     */
    private static String getExceptionAllinformation(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }
}
