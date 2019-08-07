package com.crt.common.jwtInterceptor.exception;

/**
 * 令牌过期自定义异常
 */
public class TokenExpiredException extends BaseException {

    private static final long serialVersionUID = 1L;

    public TokenExpiredException(String message) {
        super(message);
    }
}
