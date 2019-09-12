package com.crt.common.util;

import com.crt.common.config.UacCookieProperties;
import com.crt.common.constant.Constants;
import com.crt.common.redis.RedisUtil;
import com.crt.common.vo.E6Wrapper;
import com.crt.common.vo.E6WrapperUtil;
import com.crt.common.vo.UserRedisVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 用户信息获取工具类
 */
@Component
public class UserInfoUtil {

    @Autowired
    private UacCookieProperties uacCookieProperties;

    private static  UacCookieProperties uacCookieProperties1;

    @Autowired
    private RedisUtil<Map<String,Object>> cache;

    private static  RedisUtil<Map<String,Object>> userCache;

    @PostConstruct
    public void beforeInit() {
        userCache = cache;
        uacCookieProperties1 = uacCookieProperties;

    }

    /**
     * 获取用户信息方法
     * @return redis里面存储的用户信息
     */
    public static E6Wrapper<UserRedisVO> getUserInfo() {

        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

            String token = request.getHeader("Authorization");
            if(StringUtils.isBlank(token)){
                token=(String) request.getAttribute("Authorization");
            }
            if (StringUtils.isEmpty(token))
            {
                return E6WrapperUtil.error("token不存在,用户信息获取失败");
            }
            else
            {
                Object result = userCache.get(token).get("userVO");
                if (result == null)
                {
                    return E6WrapperUtil.error("token错误,用户信息获取失败");
                }
                else
            {
                UserRedisVO userRedisVO = new UserRedisVO();
                userRedisVO = (UserRedisVO) BeanUtil.Copy(userRedisVO,result,false);
                return E6WrapperUtil.ok(userRedisVO);
            }

        }
    }

    /**
     * 获取浏览器head中的属性值
     * @param propertyName 属性名称
     * @return
     */
    public static E6Wrapper<Integer> getHeadInfoByProperty(String propertyName) {
        int result  = 0 ;
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        if (null != request.getHeader(propertyName)){
            result = Integer.parseInt(String.valueOf(request.getHeader(propertyName)));
        }
        return E6WrapperUtil.ok(result);
    }

    /**
     * 根据当前请求路径 返回行级权限SQL
     * @param currentPath
     * @return
     */
    public static E6Wrapper<String> getRowDataAuthSQL(String currentPath){
        StringBuffer sql = new StringBuffer();
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        if(StringUtils.isBlank(token)){
            token=(String) request.getAttribute("Authorization");
        }
        if (StringUtils.isEmpty(token))
        {
            return E6WrapperUtil.error("token不存在,用户信息获取失败");
        }else {
            if (userCache.get(token).containsKey("rowAuth")) {
                Map<String, Object> result = (Map<String, Object>) userCache.get(token).get("rowAuth");
                if (null != result) {
                    int cons = 0;
                    for (Map.Entry<String, Object> entry : result.entrySet()) {
                        if (cons > 0){
                            sql.append(Constants.SPACE);
                            sql.append(Constants.CONNECTOR_OR);
                            sql.append(Constants.SPACE);
                        }
                        List<Map<String,Object>> value = (List<Map<String,Object>>)entry.getValue();
                        if (null != value && value.size() > 0){
                            if (result.size() > 1){
                                sql.append(Constants.LEFT_PARENTHESES);
                            }
                            for (int i = 0; i < value.size();i++){
                                if (i > 0){
                                    sql.append(Constants.SPACE);
                                    sql.append(Constants.CONNECTOR_AND);
                                    sql.append(Constants.SPACE);
                                }
                                Map<String,Object> map = (Map<String,Object>) value.get(i);
                                //判断当前路径是否存在行级权限
                                if (map.containsKey("url") && currentPath.equals(map.get("url").toString())){
                                    //拼接SQL 表名.列名 操作符 值
                                    sql.append(Constants.SPACE);
                                    sql.append(map.get("set_table"));
                                    sql.append(Constants.SPOT);
                                    sql.append(map.get("set_column"));
                                    sql.append(Constants.SPACE);
                                    sql.append(map.get("set_operator").toString().toUpperCase());
                                    sql.append(Constants.SPACE);
                                    if (Constants.NUMBER_TEPY.indexOf(map.get("column_type").toString()) >= 0){
                                        sql.append(map.get("set_value"));
                                    }else{
                                        sql.append(Constants.SINGLE_QUOTATION_MARK);
                                        sql.append(map.get("set_value"));
                                        if (Constants.FUZZY_QUERY_KEY.equals(map.get("set_operator").toString().toUpperCase())){
                                            sql.append(Constants.PERCENTAGE_MARK);
                                            sql.append(Constants.SINGLE_QUOTATION_MARK);
                                        }
                                    }
                                    sql.append(Constants.SPACE);
                                }else {
                                    break;
                                }
                            }
                            if (result.size() > 1){
                                sql.append(Constants.RIGHT_PARENTHESES);
                            }
                            cons ++ ;
                        }
                    }
                }
            }
        }
        if ("() OR ()".equals(sql.toString())){
            return E6WrapperUtil.ok("");
        }
        return E6WrapperUtil.ok(sql.toString());
    }
}
