package com.crt.common.jwtInterceptor.utils;

import cn.hutool.core.util.StrUtil;
import com.crt.common.jwtInterceptor.constant.JwtAuthorizedConstant;
import com.crt.common.jwtInterceptor.config.JwtAuthorizedProperties;
import com.crt.common.jwtInterceptor.vo.AuthUserVO;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt 工具帮助类
 */
@Component
public class JwtUtils {

    @Autowired
    private JwtAuthorizedProperties jwtAuthorizedProperties;

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
        Claims parClaims = buildJwtClaims(issuer, authUserVO, expirationTime);

        // 创建jwt token 签名
        String jwtToken = executeCreateJwt(parClaims);
        return jwtToken;
    }

    /**
     * 解析jwt签名内容体
     *
     * @param jwtToken
     *          jwt授权签名
     * @return jwt 内容体原生对象
     */
    public Claims parseJwtToken(String jwtToken) throws ExpiredJwtException, UnsupportedJwtException,
            MalformedJwtException, SignatureException, IllegalArgumentException{

        // 创建jwt解析器
        JwtParser jwtParser = Jwts.parser();
        // 设置jwt签名字符串加密秘钥
        jwtParser.setSigningKey(jwtAuthorizedProperties.getJwtCustomSecret());

        // 执行内容体
        Claims originalClaims = jwtParser.parseClaimsJws(jwtToken).getBody();

        return originalClaims;
    }

    /**
     * 解析jwt签名内容体
     *
     * @param jwtToken
     *          jwt授权签名
     * @return jwt 内容体Map对象
     */
    public Map<String,Object> parseJwtTokenPlus(String jwtToken) throws ExpiredJwtException, UnsupportedJwtException,
            MalformedJwtException, SignatureException, IllegalArgumentException{

        // 解析签名
        Claims claims = this.parseJwtToken(jwtToken);

        // 执行转换操作
        return this.converJwtClaimsToMap(claims);
    }

    /**
     * 刷新jwt签名
     *
     * @param expirationTime
     *          有效期
     * @param claims
     *          原token签名体
     * @return
     */
    public String refreshToken(Long expirationTime, Claims claims) {
        // 设置jwt签发者
        String issuer = claims.getIssuer();
        // 设置jwt受签用户，当前用户
        String subject = claims.getSubject();
        // 设置jwt接收者
        String audience = claims.getAudience();
        String customAttr = claims.get("customAttr") != null ? claims.get("customAttr").toString() :"";

        // 授权用户信息
        AuthUserVO authUserVO = new AuthUserVO();
        authUserVO.setUsername(subject);
        authUserVO.setUserAgent(audience);
        authUserVO.setCustomAttr(customAttr);

        // 创建jwt 内容体
        Claims parClaims = buildJwtClaims(issuer, authUserVO, expirationTime);

        // 创建jwt token 签名
        String jwtToken = executeCreateJwt(parClaims);
        return jwtToken;
    }

    /**
     * 构建jwt内容体
     *
     * @param issuer
     *          签发者
     * @param authUserVO
     *          授权用户信息
     * @param expirationTime
     *          token有效期
     * @return Claims jwt内容体
     */
    private Claims buildJwtClaims(String issuer, AuthUserVO authUserVO, Long expirationTime) {
        // 创建标准jwt内容声明对象
        Claims parClaims = Jwts.claims();
        // 设置jwtId
        parClaims.setId(String.valueOf(System.currentTimeMillis()));

        // 设置jwt签发者
        if (StrUtil.isNotEmpty(issuer)){
            parClaims.setIssuer(issuer);
        }

        // 设置jwt受签用户，当前用户
        parClaims.setSubject(authUserVO.getUsername());

        // 设置jwt接收者
        String audience = "";
        if (StrUtil.isNotEmpty(authUserVO.getUserAgent())){
            audience = authUserVO.getUsername()+" "+authUserVO.getUserAgent();
        } else {
            audience = authUserVO.getUsername();
        }
        parClaims.setAudience(audience);

        // 定义有效期
        if (expirationTime == null){
            // 设置签名有效期，则默认8小时,480分钟, 1秒=1000毫秒，1分钟=1000*60
            expirationTime = (1000*60) * Long.valueOf(jwtAuthorizedProperties.getJwtExpirationTimeDefult());
        }

        // 当前系统时间戳，毫秒
        Long nowMillis = System.currentTimeMillis();
        // 签名有效期 = 当前系统时间戳 + 有效期
        expirationTime = nowMillis + expirationTime;

        // 设置jwt签发时间
        parClaims.setIssuedAt(new Date(nowMillis));
        // 设置jwt签名有效期
        parClaims.setExpiration(new Date(expirationTime));

        // 自定义声明信息，设置自定义属性值
        if (StrUtil.isNotEmpty(authUserVO.getCustomAttr())){
            parClaims.put("customAttr",authUserVO.getCustomAttr());
        }

        return parClaims;
    }

    /**
     * 执行JWT签名创建
     *
     * @param parClaims
     *          标准jwt标准声明信息
     * @return jwtToken
     *              jwt签名字符串
     */
    private String executeCreateJwt(Claims parClaims) {
        // JWT秘钥加密方式 SHA-256
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 创建jwt对象
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setHeaderParam("type", JwtAuthorizedConstant.AUTHORIZ_TOKEN_TYPE);
        jwtBuilder.setHeaderParam("alg",JwtAuthorizedConstant.JWT_SIGNATURE_ALGORITHM);

        // 绑定标准jwt内容声明信息
        jwtBuilder.setClaims(parClaims);

        // 执行jwt签名加密，加密方式为 HmacSHA256
        jwtBuilder.signWith(signatureAlgorithm,jwtAuthorizedProperties.getJwtCustomSecret());

        // 执行jwt签名创建，格式为：头部.内容体.秘钥
        return jwtBuilder.compact();
    }

    /**
     * 转换jwt内容体为 map结构
     *
     * @param claims
     *          jwt内容体
     * @return
     */
    private Map<String,Object> converJwtClaimsToMap(Claims claims) {
        Map<String,Object> pareInfo = new HashMap();

        // 获取jwt签发者
        String issuer = claims.getIssuer();
        // 受签用户
        String username = claims.getSubject();
        // 签名接受者
        String audience = claims.getAudience();
        // 签发时间
        long issuedAt = claims.getIssuedAt().getTime();
        // 有效期
        long expiration = claims.getExpiration().getTime();
        // 自定义签名属性
        String customAttr = claims.get("customAttr") == null ? "":claims.get("customAttr").toString();

        // 设置签名返回结果
        pareInfo.put("issuer",issuer);
        pareInfo.put("username",username);
        pareInfo.put("audience",audience);
        pareInfo.put("customAttr",customAttr);
        pareInfo.put("issuedAt",issuedAt);
        pareInfo.put("expiration",expiration);

        return pareInfo;
    }
}
