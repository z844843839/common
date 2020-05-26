package com.crt.common.jwtInterceptor.authentication;

import com.crt.common.jwtInterceptor.exception.TokenErrorException;
import com.crt.common.jwtInterceptor.exception.TokenExpiredException;
import com.crt.common.jwtInterceptor.utils.JwtUtils;
import com.crt.common.jwtInterceptor.vo.AuthUserVO;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Jwt 授权验证核心处理类
 */
@Component
public class JwtAuthorized {

    private final static Logger logger = LoggerFactory.getLogger(JwtAuthorized.class);

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 创建JWT Token令牌
     *
     * @param issuer
     *          签发者
     * @param authUserVO
     *          授权用户信息
     * @param expirationTime
     *           签名有效期，单位：分钟
     */
    public String createJwtToken(String issuer, AuthUserVO authUserVO, Long expirationTime) {

        // 创建jwt 内容体
        String jwtToken = jwtUtils.createJwtToken(issuer,authUserVO,expirationTime);

        return jwtToken;
    }

    /**
     * 解析jwt签名内容体
     *
     * @param jwtToken
     *          jwt授权签名
     */
    public Map<String,Object> parseJwtToken(String jwtToken) {
        // 返回内容声明结果集
        Map<String,Object> resultMap = new HashMap();

        try {
            // 解析签名
            resultMap = jwtUtils.parseJwtTokenPlus(jwtToken);

        } catch (ExpiredJwtException eje) {
            logger.error("Token签名已过期");
            throw new TokenExpiredException("登录超时，请重新登录!");
        } catch (UnsupportedJwtException uje) {
            logger.error("Token格式错误");
            throw new TokenErrorException("用户信息有误，请重新登录或联系管理员!");
        } catch (MalformedJwtException mje) {
            logger.error("Token格式错误");
            throw new TokenErrorException("用户信息有误,请重新登录或联系管理员!");
        } catch (SignatureException se) {
            logger.error("Token签名内容错误");
            throw new TokenErrorException("用户信息有误，请重新登录或联系管理员!");
        } catch (IllegalArgumentException iae) {
            logger.error("Token签名内容错误");
            throw new TokenErrorException("用户信息有误，请重新登录或联系管理员!");
        } catch (Exception e) {
            logger.error("Token签名验证错误");
            throw new TokenErrorException("用户信息有误，请重新登录或联系管理员!");
        }

        return resultMap;
    }

    /**
     * 刷新jwt签名
     * 说明：需要在token没过期之前，执行刷新操作
     *
     * @param expirationTime
     *          有效期
     * @param jwtToken
     *          原token签名
     * @return
     */
    public String refreshToken(Long expirationTime, String jwtToken) {
        String newJwtToken = "";
        try {
            // 解析签名
            Claims originalClaims = jwtUtils.parseJwtToken(jwtToken);

            // 刷新jwtToken
            newJwtToken = jwtUtils.refreshToken(expirationTime,originalClaims);

        } catch (ExpiredJwtException eje) {
            logger.error("Token签名已过期");
            throw new TokenExpiredException("登录超时，请重新登录!");
        } catch (UnsupportedJwtException uje) {
            logger.error("Token格式错误");
            throw new TokenErrorException("用户信息有误,请重新登录或联系管理员!");
        } catch (MalformedJwtException mje) {
            logger.error("Token格式错误");
            throw new TokenErrorException("用户信息有误,请重新登录或联系管理员!");
        } catch (SignatureException se) {
            logger.error("Token签名内容错误");
            throw new TokenErrorException("用户信息有误,请重新登录或联系管理员!");
        } catch (IllegalArgumentException iae) {
            logger.error("Token签名内容错误");
            throw new TokenErrorException("用户信息有误,请重新登录或联系管理员!");
        }

        return newJwtToken;
    }
}
