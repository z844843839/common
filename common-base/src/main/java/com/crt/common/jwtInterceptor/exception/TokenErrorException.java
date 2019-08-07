package com.crt.common.jwtInterceptor.exception;

/**
 * 令牌错误自定义异常
 */
public class TokenErrorException extends BaseException {

    private static final long serialVersionUID = 1L;

    public TokenErrorException(String message) {
        super(message);
    }
}
