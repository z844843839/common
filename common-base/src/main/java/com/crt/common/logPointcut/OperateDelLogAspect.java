package com.crt.common.logPointcut;

import com.alibaba.fastjson.JSONObject;
import com.crt.common.constant.Constants;
import com.crt.common.util.DateUtils;
import com.crt.common.util.UserInfoUtil;
import com.crt.common.vo.E6Wrapper;
import com.crt.common.vo.E6WrapperUtil;
import com.crt.middle.base.MessageQueueService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 删除操作日志切入类
 *
 * @author malin
 */
@Aspect
@Component
public class OperateDelLogAspect {
    private static final Logger log = LoggerFactory.getLogger(OperateDelLogAspect.class);

    @Autowired
    private MessageQueueService messageQueueService;

    /**
     * 切点， 切所有的Controller所有的写入方法， 包括新增、更新
     * TODO  把这个切点动态化
     */
    @Pointcut("execution(* com.crt.common.web.BaseController.delete(..)) " +
            " || execution(public * com.crt.*.*.*.*Controller.delete*(..))" +
            " || execution(public * com.crt.*.*.*.*Controller.del*(..))" +
            " || execution(public * com.crt.*.*.*.*Controller.remove*(..))")
    private void controllerPiontcout() {

    }

    @Around("controllerPiontcout()")
    public Object around(ProceedingJoinPoint point) {
        JSONObject logJson = new JSONObject();
        String logKey = "operateLog";
        logJson.put("logType",logKey);
        String msg;
        try {
            log.error("<============ 进入环绕通知 ============>");
            Object target = point.getTarget();
            Object[] args = point.getArgs();
            Object param = null;
            if (args != null && args.length > 0) {
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
            String module = target.getClass().getName();
            logJson.put("operateModule",module);
            /**
             * 操作类型
             */
            logJson.put("operateType",Constants.OPERATE_TYPE_DELETE);
            //执行原方法
            Object operateResult = point.proceed();
            //当前操作用户
            logJson.put("operaterCode",UserInfoUtil.getLoginUserCode());
            logJson.put("operaterName",UserInfoUtil.getLoginUserRealName());
            logJson.put("createdAt",new Date());
            Integer operaterResult = Constants.RESULT_UNKNOWN;
            if (null != operateResult) {
                E6Wrapper e6 = (E6Wrapper) operateResult;
                if (Constants.STATUS_CODE_SUCCESS.equals(e6.getCode())) {
                    operaterResult = Constants.RESULT_SUCCESS;
                } else {
                    operaterResult = Constants.RESULT_FAIL;
                }
                logJson.put("操作结果",operaterResult);
            }
            messageQueueService.send("crt_e6_log_exchange","crt_e6_log_routingkey",logJson.toString());
            return operateResult;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            msg = "系统繁忙，请稍后重试";
        }
        return new ResponseEntity(E6WrapperUtil.error(msg), HttpStatus.OK);
    }


}
