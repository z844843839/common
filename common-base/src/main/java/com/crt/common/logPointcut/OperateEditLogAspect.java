package com.crt.common.logPointcut;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.crt.common.constant.Constants;
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
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.security.Permission;
import java.util.Date;

/**
 * 修改（新增和编辑）操作日志切入类
 * @author malin
 */
@Aspect
@Component
public class OperateEditLogAspect {
    private static final Logger log = LoggerFactory.getLogger(OperateEditLogAspect.class);

    @Autowired
    private MessageQueueService messageQueueService;

    /**
     * 切点， 切所有的Controller所有的写入方法， 包括新增、更新
     * TODO  把这个切点动态化
     */
    @Pointcut("execution(* com.crt.common.web.BaseController.save(..)) " +
            " || execution(public * com.crt.*.*.*.*Controller.save*(..))" +
            " || execution(public * com.crt.*.*.*.*Controller.audit*(..))" +
            " || execution(public * com.crt.*.*.*.*Controller.freezing(..))" +
            " || execution(public * com.crt.*.*.*.*Controller.unfreeze(..))" +
            " || execution(public * com.crt.*.*.*.*Controller.initPassword(..))" +
            " || execution(public * com.crt.*.*.*.*Controller.submitAnswer(..))" +
            " || execution(public * com.crt.*.*.*.*Controller.approve(..))" +
            " || execution(public * com.crt.*.*.*.*Controller.enable(..))" +
            " || execution(public * com.crt.*.*.*.*Controller.discard(..))" +
            " || execution(public * com.crt.*.*.*.*Controller.examine(..))" +
            " || execution(public * com.crt.*.*.*.*Controller.publish(..))" +
            " || execution(public * com.crt.*.*.*.*Controller.register(..))" +
            " || execution(public * com.crt.*.*.*.*Controller.insert*(..))" +
            " || execution(public * com.crt.*.*.*.*Controller.reset*(..))" +
            " || execution(* com.crt.common.web.BaseController.update*(..)) " +
            " || execution(* com.crt.common.web.BaseController.modify(..)) " +
            " || execution(* com.crt.*.*.*.*Controller.modify*(..))")
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
            Object target = point.getTarget();
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
            /**
             * 操作类型
             */
            String operateType = Constants.OPERATE_TYPE_SAVE;
            if (null != param){
                try{
                    JSONObject json = JSON.parseObject(JSONObject.toJSON(param).toString());
                    String pk = "id";
                    if (json.getInteger(pk)!=null){
                        operateType = Constants.OPERATE_TYPE_MODIFY;
                    }
                }catch (JSONException e){
                    operateType = Constants.OPERATE_TYPE_MODIFY;
                }
            }
            logJson.put("operateType",operateType);
            //执行原方法
            Object operateResult = point.proceed();
            //操作模块
            String module = request.getRequestURI();
            module = module.substring(1,module.length());
            module = module.substring(0,module.indexOf("/"));
            logJson.put("operateModule",module);
            //操作方法
            String operateMethod = target.getClass().getName() +"."+ point.getSignature().getName() +"()";
            logJson.put("operateMethod",operateMethod);
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
            //当前操作用户
            logJson.put("operaterCode",UserInfoUtil.getLoginUserCode());
            logJson.put("operaterName",UserInfoUtil.getLoginUserRealName());
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
                messageQueueService.send("crt_e6_log_exchange","crt_e6_log_routingkey",logJson.toString());
            }catch (NullPointerException e){
            }
            return operateResult;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            msg = "系统繁忙，请稍后重试";
        }
        return new ResponseEntity(E6WrapperUtil.error(msg), HttpStatus.OK);
    }

}
