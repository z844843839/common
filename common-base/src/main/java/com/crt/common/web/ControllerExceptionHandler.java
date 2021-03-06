package com.crt.common.web;

import com.alibaba.fastjson.JSON;
import com.crt.common.constant.Constants;
import com.crt.common.vo.E6WrapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author liupengfei@e6yun.com
 * @Date 2019/7/8 13:55
 * @Description
 * 自定义错误码，全部用四位数字，和http错误码区分开，
 * 4xxx代表客户端错误（如：参数校验失败）
 * 5xxx代表服务端错误（内部处理异常）
 * 6xxx代表业务逻辑方面的错误
 *
 * 如果业务module中需要重写异常处理，请拷贝这个类过去，把名称修改为ModuleExceptionHandler即可覆盖这个默认实现
 *
 * ExceptionHandler方法的参数列表和返回值类型可以参考官方文档：
 * @https://docs.spring.io/spring/docs/5.0.12.RELEASE/spring-framework-reference/web.html#mvc-ann-exceptionhandler
 **/
@ConditionalOnMissingBean(name={"moduleExceptionHandler"})
@ControllerAdvice
@RestControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 所有验证框架异常捕获处理
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public Object validationExceptionHandler(Exception exception) {
        BindingResult bindResult = null;
        Map<String,String> paramMsg = new HashMap<>(Constants.HASHMAP_DEFAULT_SIZE);
        if (exception instanceof BindException) {
            bindResult = ((BindException) exception).getBindingResult();
        } else if (exception instanceof MethodArgumentNotValidException) {
            bindResult = ((MethodArgumentNotValidException) exception).getBindingResult();
        }
        String msg;
        if (bindResult != null && bindResult.hasErrors()) {
            for (FieldError error: bindResult.getFieldErrors()) {
                paramMsg.put(error.getField(),error.getDefaultMessage());
            }
            msg = JSON.toJSONString(paramMsg);
        }else {
            msg = "系统繁忙，请稍后重试";
        }
        //这里不应该把服务端的异常 返回给前端界面，会有安全隐患
        return E6WrapperUtil.ok(Constants.INTERNAL_SERVER_ERROR,msg,null);
    }

}
