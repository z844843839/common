package com.crt.common.jwtInterceptor.constant;

/**
 * Jwt 常量定义类
 */
public class JwtAuthorizedConstant {

    /** 授权令牌类型——JWT*/
    public final static String AUTHORIZ_TOKEN_TYPE = "JWT";

    /** JWT签名加密算法类型*/
    public final static String JWT_SIGNATURE_ALGORITHM = "HS256";

    /** JWT签名过期，响应结果状态定义*/
    public final static Integer TOKEN_EXPIRED_RESPONSE_STATUS = 299;


}
