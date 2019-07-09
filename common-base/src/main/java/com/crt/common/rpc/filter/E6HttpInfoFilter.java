package com.crt.common.rpc.filter;


import com.crt.common.constant.ConstOfUserContext;
import com.crt.common.context.UserContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * 请求过滤器
 * 在收到请求后，把header中需要的信息 塞入threadLocal 及 Trace
 * 用于rpc调用双方
 */
@Component
@Order(-2)
@ConditionalOnProperty(value = "crt.frame.user.session",havingValue = "token",matchIfMissing = true)
public class  E6HttpInfoFilter extends GenericFilterBean {
    final static Logger logger = LoggerFactory.getLogger(E6HttpInfoFilter.class);


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("Filter just supports HTTP requests");
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        //如果是非静态请求，则把需要的header内容获取放入span中

        Arrays.stream(ConstOfUserContext.HEADERS).forEach(name->saveHeader(name,httpRequest.getHeader(name)));

        //debug模式下，打印header内容
        if(logger.isDebugEnabled()) {
            logger.debug("head begin ---");
            Enumeration<String> names =  httpRequest.getHeaderNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                logger.debug("http header:{}={}", name, httpRequest.getHeader(name));
            }
            logger.debug("head end ---");
        }
        chain.doFilter(request, response);
    }

    private void saveHeader(String headerName,String headerValue){
        if(StringUtils.isEmpty(headerName) || StringUtils.isEmpty(headerValue)){
            return;
        }
        //保存到threadLocal中
        UserContextHelper.saveHeaderInThreadLocal(headerName,headerValue);
    }
}
