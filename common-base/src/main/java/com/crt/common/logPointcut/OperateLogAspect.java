package com.crt.common.logPointcut;

import com.alibaba.fastjson.JSONObject;
import com.crt.common.constant.Constants;
import com.crt.common.jwtInterceptor.annotation.TokenAuthentication;
import com.crt.common.jwtInterceptor.constant.AuthLevel;
import com.crt.common.util.UserInfoUtil;
import com.crt.common.vo.E6Wrapper;
import com.crt.common.vo.E6WrapperUtil;
import com.crt.middle.base.MessageQueueService;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.ExecutorService;

/**
 * 修改（新增和编辑）操作日志切入类
 * @author malin
 */
@Aspect
@Component
public class OperateLogAspect {
    private static final Logger log = LoggerFactory.getLogger(OperateLogAspect.class);

    @Autowired
    private MessageQueueService messageQueueService;

    /**注入线程池**/
    @Autowired
    private ExecutorService executorService;

    /**
     * 切点， 切所有的Controller所有的写入方法， 包括新增、更新
     * TODO  把这个切点动态化
     */
    @Pointcut("execution(public * com.crt..*Controller.*(..)) " +
            "|| execution(public * com.crt..web.*Controller.*(..)) " +
            "|| execution(public * com.crt.*.*.*.*.*Controller.*(..)) ")
    private void controllerPiontcout() {

    }

    @Around("controllerPiontcout()")
    public Object around(ProceedingJoinPoint point) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        JSONObject logJson = new JSONObject();
        String logKey = "operateLog";
        logJson.put("logType",logKey);
        String msg;
        try {
            log.error("<============ 进入环绕通知 ============>");
            Object target = null;
            try {
                target = point.getTarget();
                Object[] args = point.getArgs();
                Object param = null;
                if (args != null && args.length > 0){
                    for (int k = 0; k < args.length; k++) {
                        Object arg = args[k];
                        // 获取对象类型
                        String typeName = arg.getClass().getTypeName();
                        for (String t : Constants.BASIC_TYPES) {
                            //1 判断是否是基础类型
                            if (!t.equals(typeName)) {
                                //Controller 入参的PO
                                param = arg;
                            }
                        }
                    }
                }
                //操作类型 默认为空
                logJson.put("operateType","");
            } catch (Exception e) {}
            //执行原方法
            Object operateResult = point.proceed();
            try {
                //操作模块
                String module = request.getRequestURI();
                module = module.substring(1,module.length());
                module = module.substring(0,module.indexOf("/"));
                logJson.put("operateModule",module);
                //操作方法
                String operateMethod = target.getClass().getName() +"."+ point.getSignature().getName() +"()";
                logJson.put("operateMethod",operateMethod);
                //操作人IP
                logJson.put("operateIp",request.getRemoteAddr());
                //操作请求
                logJson.put("operateUrl",request.getRequestURL().toString());
                //操作内容
                Signature signature = point.getSignature();
                MethodSignature methodSignature = (MethodSignature)signature;
                //获取方法对象
                Method targetMethod = methodSignature.getMethod();
                //获取方法上注解
                ApiOperation api = targetMethod.getAnnotation(ApiOperation.class);
                logJson.put("operateContent",api.value());
                //获取方法上注解
                TokenAuthentication auth = targetMethod.getAnnotation(TokenAuthentication.class);
                if (null == auth || !AuthLevel.NO_AUTH.equals(auth.authLevel())){
                    //当前操作用户
                    logJson.put("operaterCode",UserInfoUtil.getLoginUserCode());
                    logJson.put("operaterName",UserInfoUtil.getLoginUserRealName());
                }
                logJson.put("createdAt",new Date());
                Integer operaterResult = Constants.RESULT_UNKNOWN;
                if (null != operateResult){
                    E6Wrapper e6 = (E6Wrapper)operateResult;
                    if (Constants.STATUS_CODE_SUCCESS.equals(e6.getCode())){
                        operaterResult = Constants.RESULT_SUCCESS;
                    }else {
                        operaterResult = Constants.RESULT_FAIL;
                    }
                    logJson.put("result",operaterResult);
                }
                try {
                    executorService.execute(new MqRunnable(messageQueueService,logJson.toJSONString()));
                }catch (Exception e){
                    log.error("MQ调用异常{}", e.getMessage());
                }
            } catch (Exception e) {}
            return operateResult;
        } catch (Throwable throwable) {
            String ex_msg= stackTraceToString(throwable.getClass().getName(), throwable.getMessage(), throwable.getStackTrace());
            log.error("异常信息:"+ex_msg);
            msg = "系统繁忙，请稍后重试";
            return E6WrapperUtil.ok(Constants.INTERNAL_SERVER_ERROR,msg,null);
        }
    }

    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuffer strbuff = new StringBuffer();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet + "\n");
        }
        String message = exceptionName + ":" + exceptionMessage + "\n\t" + strbuff.toString();
        return message;
    }

    class MqRunnable implements Runnable {
        private MessageQueueService messageQueueService;
        private String logStr;

        public MqRunnable(MessageQueueService messageQueueService,String logStr){
            super();
            this.messageQueueService=messageQueueService;
            this.logStr = logStr;
        }
        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            String exchange = "crt_e6_log_exchange";
            String key = "crt_e6_log_routingkey";
            messageQueueService.send(exchange,key,logStr);
        }
    }
}
