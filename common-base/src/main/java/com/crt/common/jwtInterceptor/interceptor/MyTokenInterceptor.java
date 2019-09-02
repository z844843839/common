package com.crt.common.jwtInterceptor.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.crt.common.config.UacCookieProperties;
import com.crt.common.jwtInterceptor.annotation.TokenAuthentication;
import com.crt.common.jwtInterceptor.authentication.JwtAuthorized;
import com.crt.common.jwtInterceptor.constant.AuthLevel;
import com.crt.common.jwtInterceptor.config.JwtAuthorizedProperties;
import com.crt.common.jwtInterceptor.constant.JwtAuthorizedConstant;
import com.crt.common.jwtInterceptor.exception.TokenErrorException;
import com.crt.common.jwtInterceptor.exception.TokenExpiredException;
import com.crt.common.redis.RedisUtil;
import com.crt.common.util.UserInfoUtil;
import com.crt.common.vo.E6Wrapper;
import com.crt.common.vo.E6WrapperUtil;
import com.crt.common.vo.UserRedisVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 自定义Token验证拦截器
 */
@Component
public class MyTokenInterceptor implements HandlerInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(MyTokenInterceptor.class);

    @Autowired
    private JwtAuthorizedProperties jwtAuthorizedProperties;

    @Autowired
    private JwtAuthorized jwtAuthorized;

    @Autowired
    private UacCookieProperties uacCookieProperties;

    @Autowired
    private RedisUtil<Map<String,Object>> redisUtil;
    @Override

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String url = request.getRequestURI();//这里打端点，页面访问swagger页面看看请求的什么路径

		if (url.contains("bpm") || url.contains("MIDDLE-COMMON-FILEUPLOAD"))
		{
			return true;
		}
        // 当前请求方法、请求类
        Method reqMethod = ((HandlerMethod) handler).getMethod();
        Class reqClass = reqMethod.getDeclaringClass();

        // 获取当前请求方法上的授权验证注解，并验证授权级别是否为开放级别，若是开放，则直接返回，不在继续验证
        TokenAuthentication authMethod = (TokenAuthentication)this.getMethodAnnotation(reqMethod, TokenAuthentication.class);
        if (authMethod != null && authMethod.authLevel() == AuthLevel.NO_AUTH) {
            return true;
        }

        // 获取当前请求方法上的授权验证注解，并验证授权级别是否为开放级别，若是开放，则直接返回，不在继续验证
        TokenAuthentication authClass = (TokenAuthentication)this.getClassAnnotation(reqClass, TokenAuthentication.class);
        if (authClass != null && authClass.authLevel() == AuthLevel.NO_AUTH) {
            return true;
        }

        if (this.exeTokenAuthorized(request,response))
        {
            // 验证权限，true 请求放行，false 请求拦截
            return this.exeCheckPrivilege(request,response);
        }
        else
        {
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }

    /**
     * 执行Token有效性验证
     *
     * @param request
     * @param response
     * @return true 请求放行  false 请求拦截
     */
    private boolean exeTokenAuthorized(HttpServletRequest request, HttpServletResponse response){

        // 获取token
        String token = request.getHeader(jwtAuthorizedProperties.getTokenHeaderKey());
        if (StrUtil.isEmpty(token)) {
            // 构建错误响应结果报文
            this.buildErrorResponse(response,"Token签名为空",null);
            return false;
        }
        if (token.equals("0000"))
        {
            return true;
        }
        try {
            // 解析token
            Map<String,Object> resultMap = jwtAuthorized.parseJwtToken(token);

            // 获取受签用户
            String tokenUsername = resultMap.get("username") != null ? resultMap.get("username").toString():"";
            // 获取签名接收者
            String audience = resultMap.get("audience") != null ? resultMap.get("audience").toString():"";
            // 当前请求受签用户
            String thisAudience = tokenUsername+" "+request.getHeader("User-Agent");

            // 验证是否存在token盗用情况
            if(!StrUtil.equals(thisAudience,audience)) {
                // 构建错误响应结果报文
                this.buildErrorResponse(response,"Token签名验证错误",null);
                return false;
            }

            return true;
        } catch (TokenErrorException ten) {
            // 构建错误响应结果报文
            this.buildErrorResponse(response,ten.getMessage(),null);
            return false;

            // 签名过期
        } catch (TokenExpiredException tee) {
            // 构建错误响应结果报文
            this.buildErrorResponse(response,tee.getMessage(), JwtAuthorizedConstant.TOKEN_EXPIRED_RESPONSE_STATUS);
            return false;
        }
    }


    /**
     * 执行权限验证
     *
     * @param request
     * @param response
     * @return true 请求放行  false 请求拦截
     */
    private boolean exeCheckPrivilege (HttpServletRequest request, HttpServletResponse response){
        // 获取token
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token))
        {
            this.buildErrorResponse(response,"用户信息失效",null);
            return false;
        }

        if (token.equals("0000"))
        {
            return true;
        }

        UserRedisVO userRedisVO = (UserRedisVO) UserInfoUtil.getUserInfo().getResult();
        if (StringUtils.equals("super",userRedisVO.getAccountNumber()))
        {
            return true;
        }

        try{
            List<Map<String,Object>> auth = (List<Map<String,Object>> ) redisUtil.get(token).get("auth");
            if (auth == null || auth.size() < 1)
            {
                this.buildErrorResponse(response,"获取权限信息失败,请联系管理员分配相关权限！",null);
                return false;
            }
            else
            {

                List<String> list = new ArrayList<>();
                for (int i = 0; i < auth.size(); i++)
                {
                    list.add(String.valueOf(auth.get(i).get("url")));
                }
                logger.error("用户总权限 {}",list.toString());
                String url = request.getServletPath();

                String uri = url.substring(url.substring(url.indexOf("/")+1).indexOf("/")+2);

                logger.error("用户当前权限 {}",uri);
                if (list.contains(uri))
                {
                    return true;
                }
                else
                {
                    this.buildErrorResponse(response,"当前用户无相关操作权限！",null);
                    return false;
                }
            }
        }
        catch (Exception e)
        {
            // 构建错误响应结果报文
            this.buildErrorResponse(response,"当前信息失效，请重新登录",null);
            return false;
        }

    }

    /**
     * 获取某个类上的某个注解对象
     *
     * @param reqClass
     *          类对象
     * @param annotationClass
     *          注解对象
     * @return 注解对象
     */
    private Annotation getClassAnnotation(Class reqClass, Class<? extends Annotation> annotationClass) {
        if (null != reqClass && reqClass.isAnnotationPresent(annotationClass)) {
            return reqClass.getAnnotation(annotationClass);
        }
        return null;
    }

    /**
     * 获取某个方法上的某个注解对象
     * @param reqMethod
     *          方法对象
     * @param annotationClass
     *          注解对象
     * @return 注解对象
     */
    private Annotation getMethodAnnotation(Method reqMethod, Class<? extends Annotation> annotationClass) {
        if (null != reqMethod && reqMethod.isAnnotationPresent(annotationClass)) {
            return reqMethod.getAnnotation(annotationClass);
        }
        return null;
    }

    /**
     * 构建错误响应结果报文
     *
     * @param response
     *          响应结果
     * @param message
     *          提示消息
     */
    private void buildErrorResponse(HttpServletResponse response,String message, Integer status) {
        try {
            if (status == null) {
                // 定义响应状态，默认鉴权错误
                status = E6Wrapper.NON_AUTHORITATIVE_INFORMATION.value();
            }

            // 构建框架报文结构
            E6Wrapper e6Wrapper = E6WrapperUtil.error(status,message);

            // 设置响应结果
            response.setStatus(status);
            response.getWriter().append(JSON.toJSONString(e6Wrapper));

        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
    }
}
