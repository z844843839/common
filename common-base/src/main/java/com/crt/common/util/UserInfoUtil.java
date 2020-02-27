package com.crt.common.util;

import com.crt.common.config.UacCookieProperties;
import com.crt.common.constant.Constants;
import com.crt.common.redis.RedisUtil;
import com.crt.common.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 用户信息获取工具类
 *
 * @author malin
 */
@Component
public class UserInfoUtil {

    private static Logger logger = LoggerFactory.getLogger(UserInfoUtil.class);

    @Autowired
    private UacCookieProperties uacCookieProperties;

    private static UacCookieProperties uacCookieProperties1;

    @Autowired
    private RedisUtil<Map<String, Object>> cache;

    private static RedisUtil<Map<String, Object>> userCache;

    @PostConstruct
    public void beforeInit() {
        userCache = cache;
        uacCookieProperties1 = uacCookieProperties;

    }

    /**
     * 获取用户信息方法
     *
     * @return redis里面存储的用户信息
     */
    public static E6Wrapper<UserRedisVO> getUserInfo() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            token = (String) request.getAttribute("Authorization");
        }
        if (StringUtils.isEmpty(token)) {
            return E6WrapperUtil.error("token不存在,用户信息获取失败");
        } else {
            UserRedisVO userRedisVO = new UserRedisVO();
            Object result = userCache.get(token).get("userVO");
            if (result == null) {
//                String userKey = token + "user";
//                userRedisVO = CacheUtil.get(userKey, UserRedisVO.class);
                return E6WrapperUtil.error("token错误,用户信息获取失败");
            } else {
                userRedisVO = (UserRedisVO) BeanUtil.Copy(userRedisVO, result, false);
            }
            if (null != userRedisVO) {
                return E6WrapperUtil.ok(userRedisVO);
            } else {
                return E6WrapperUtil.error("token错误,用户信息获取失败");
            }
        }
    }

    /**
     * 获取登陆用户账号
     * redis里面存储的用户信息
     *
     * @return String
     */
    public static String getLoginAccount() {
        E6Wrapper<UserRedisVO> e6Wrapper = getUserInfo();
        if (e6Wrapper.success()) {
            UserRedisVO loginUser = e6Wrapper.getResult();
            return loginUser.getAccountNumber();
        }
        return null;
    }

    /**
     * 获取登陆用户真实姓名
     * redis里面存储的用户信息
     *
     * @return String
     */
    public static String getLoginUserRealName() {
        E6Wrapper<UserRedisVO> e6Wrapper = getUserInfo();
        if (e6Wrapper.success()) {
            UserRedisVO loginUser = e6Wrapper.getResult();
            return loginUser.getRealName();
        }
        return null;
    }

    /**
     * 获取登陆用户ID
     * redis里面存储的用户信息
     *
     * @return Integer
     */
    public static Integer getLoginUserId() {
        E6Wrapper<UserRedisVO> e6Wrapper = getUserInfo();
        if (e6Wrapper.success()) {
            UserRedisVO loginUser = e6Wrapper.getResult();
            return loginUser.getId();
        }
        return null;
    }

    /**
     * 获取登陆用户编码
     * redis里面存储的用户信息
     *
     * @return Long
     */
    public static Long getLoginUserCode() {
        E6Wrapper<UserRedisVO> e6Wrapper = getUserInfo();
        if (e6Wrapper.success()) {
            UserRedisVO loginUser = e6Wrapper.getResult();
            return loginUser.getUserCode();
        }
        return null;
    }

    /**
     * 获取登陆用户专业公司ID
     * redis里面存储的用户信息
     *
     * @return Integer
     */
    public static Integer getLongUserFirmId() {
        E6Wrapper<UserRedisVO> e6Wrapper = getUserInfo();
        if (e6Wrapper.success()) {
            UserRedisVO loginUser = e6Wrapper.getResult();
            return loginUser.getAttrInt1();
        }
        return null;
    }

    /**
     * 获取登陆用户区域公司ID
     * redis里面存储的用户信息
     *
     * @return Integer
     */
    public static Integer getLongUserAreaId() {
        E6Wrapper<UserRedisVO> e6Wrapper = getUserInfo();
        if (e6Wrapper.success()) {
            UserRedisVO loginUser = e6Wrapper.getResult();
            return loginUser.getAttrInt2();
        }
        return null;
    }

    /**
     * 获取登陆用户所属组织名称
     * redis里面存储的用户信息
     *
     * @return String
     */
    public static String getLoginUserOrgName() {
        E6Wrapper<UserRedisVO> e6Wrapper = getUserInfo();
        if (e6Wrapper.success()) {
            UserRedisVO loginUser = e6Wrapper.getResult();
            return loginUser.getOrgName();
        }
        return null;
    }

    /**
     * 获取登陆用户所属组织编码
     * redis里面存储的用户信息
     *
     * @return Long
     */
    public static Long getLoginUserOrgCode() {
        E6Wrapper<UserRedisVO> e6Wrapper = getUserInfo();
        if (e6Wrapper.success()) {
            UserRedisVO loginUser = e6Wrapper.getResult();
            return loginUser.getOrgCode();
        }
        return null;
    }

    /**
     * 获取登陆用户所属角色名称
     * redis里面存储的用户信息
     *
     * @return Long
     */
    public static String getLoginUserRoleName() {
        E6Wrapper<UserRedisVO> e6Wrapper = getUserInfo();
        if (e6Wrapper.success()) {
            UserRedisVO loginUser = e6Wrapper.getResult();
            return loginUser.getRoleName();
        }
        return null;
    }

    /**
     * 获取浏览器head中的属性值
     *
     * @param propertyName 属性名称
     * @return
     */
    public static E6Wrapper<Integer> getHeadInfoByProperty(String propertyName) {
        int result = 0;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (null != request.getHeader(propertyName)) {
            result = Integer.parseInt(String.valueOf(request.getHeader(propertyName)));
        }
        return E6WrapperUtil.ok(result);
    }

    /**
      * 根据当前请求路径 返回行级权限SQL
      *
      * @return String
      */
    public static String getRowDataAuthSQL() {
        StringBuffer sql = new StringBuffer();
        try{
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("Authorization");
            if (StringUtils.isBlank(token)) {
                token = (String) request.getAttribute("Authorization");
            }
            if (StringUtils.isEmpty(token)) {
                return null;
            }else {
                String url = request.getServletPath();
                String uri = url.substring(url.substring(url.indexOf("/") + 1).indexOf("/") + 2);
                if (uri.contains(Constants.CONTAINSTR)) {
                    uri = uri.substring(0, uri.indexOf(Constants.CONTAINSTR));
                }
                if (userCache.get(token).containsKey("rowAuth")) {
                    List<RowDataAuthVo> rowAuthList = (List<RowDataAuthVo>) userCache.get(token).get("rowAuth");
                    if (null != rowAuthList && rowAuthList.size() > 0) {
                        List<RowDataAuthVo> currentRowAuthList = new ArrayList<>();
                        for (RowDataAuthVo rav : rowAuthList) {
                            if (rav.getUrl().equals(uri)) {
                                currentRowAuthList.add(rav);
                            }
                        }
                        if (currentRowAuthList.size() > 0) {
                            for (int i=0;i<currentRowAuthList.size();i++){
                                if (currentRowAuthList.size() == 1){
                                    String formula = currentRowAuthList.get(i).getFormulaSql();
                                    if (formula.indexOf(Constants.CONNECTOR_OR) >= 0){
                                        sql.append(Constants.SPACE);
                                        sql.append(Constants.LEFT_PARENTHESES);
                                        sql.append(formula);
                                        sql.append(Constants.RIGHT_PARENTHESES);
                                        sql.append(Constants.SPACE);
                                    }else {
                                        sql.append(Constants.SPACE);
                                        sql.append(formula);
                                        sql.append(Constants.SPACE);
                                    }
                                }else {
                                    if (i == 0){
                                        sql.append(Constants.LEFT_PARENTHESES);
                                    }
                                    if (i > 0){
                                        sql.append(Constants.SPACE);
                                        sql.append(Constants.CONNECTOR_OR);
                                        sql.append(Constants.SPACE);
                                    }
                                    sql.append(Constants.LEFT_PARENTHESES);
                                    sql.append(Constants.SPACE);
                                    sql.append(currentRowAuthList.get(i).getFormulaSql());
                                    sql.append(Constants.SPACE);
                                    sql.append(Constants.RIGHT_PARENTHESES);
                                    if ((i+1) == currentRowAuthList.size()){
                                        sql.append(Constants.RIGHT_PARENTHESES);
                                    }
                                }
                            }
                        }
                    }
                    return sql.toString();
                }else {
                    return null;
                }

            }
        }catch (Exception e){
            return null;
        }
    }

//    /**
//     * 根据当前请求路径 返回行级权限SQL
//     *
//     * @param currentPath 分页查询请求路径
//     * @param tableAlias  表别名 【选填】 为null时 别名为原表名
//     * @return String
//     */
//    public static String getRowDataAuthSQL(String currentPath, String tableAlias) {
//        StringBuffer sql = new StringBuffer();
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String token = request.getHeader("Authorization");
//        if (StringUtils.isBlank(token)) {
//            token = (String) request.getAttribute("Authorization");
//        }
//        if (StringUtils.isEmpty(token)) {
//            return null;
//        } else {
//            if (StringUtils.isEmpty(currentPath)) {
//                String url = request.getServletPath();
//                String uri = url.substring(url.substring(url.indexOf("/") + 1).indexOf("/") + 2);
//                if (uri.contains(Constants.CONTAINSTR)) {
//                    uri = uri.substring(0, uri.indexOf(Constants.CONTAINSTR));
//                }
//                currentPath = uri;
//            }
//            if (userCache.get(token).containsKey("rowAuth")) {
//                List<RowAuthVO> rowAuthList = (List<RowAuthVO>) userCache.get(token).get("rowAuth");
//                if (null != rowAuthList && rowAuthList.size() > 0) {
//                    List<RowAuthVO> currentRowAuthList = new ArrayList<>();
//                    for (RowAuthVO rav : rowAuthList) {
//                        if (rav.getUrl().equals(currentPath)) {
//                            currentRowAuthList.add(rav);
//                        }
//                    }
//                    if (currentRowAuthList.size() > 0) {
//                        Collections.sort(currentRowAuthList, new Comparator<RowAuthVO>() {
//                            @Override
//                            public int compare(RowAuthVO o1, RowAuthVO o2) {
//                                long diff = o1.getOrgRoleCode() - o2.getOrgRoleCode();
//                                if (diff > 0) {
//                                    return 1;
//                                } else if (diff < 0) {
//                                    return -1;
//                                }
//                                return 0;
//                            }
//                        }); // 按orgRoleCode排序
//                        int count = 0;
//                        sql.append(Constants.LEFT_PARENTHESES);
//                        for (int i = 0; i < currentRowAuthList.size(); i++) {
//                            RowAuthVO rav = currentRowAuthList.get(i);
//                            if (count >= 1) {
//                                RowAuthVO lastRav = currentRowAuthList.get(i - 1);
//                                if (rav.getOrgRoleCode().equals(lastRav.getOrgRoleCode())) {
//                                    sql.append(Constants.SPACE);
//                                    sql.append(Constants.CONNECTOR_AND);
//                                    sql.append(Constants.SPACE);
//                                } else {
//                                    sql.append(Constants.SPACE);
//                                    sql.append(Constants.RIGHT_PARENTHESES);
//                                    sql.append(Constants.SPACE);
//                                    sql.append(Constants.CONNECTOR_OR);
//                                    sql.append(Constants.SPACE);
//                                    sql.append(Constants.LEFT_PARENTHESES);
//                                    sql.append(Constants.SPACE);
//                                }
//                            }
//                            //拼接SQL 表名.列名 操作符 值
//                            sql.append(Constants.SPACE);
//                            if (StringUtils.isNotEmpty(tableAlias)) {
//                                sql.append(tableAlias);
//                            } else {
//                                sql.append(rav.getSetTable());
//                            }
//                            sql.append(Constants.SPOT);
//                            if (StringUtils.isNotEmpty(rav.getColumnAlias())){
//                                sql.append(rav.getColumnAlias());
//                            }else {
//                                sql.append(rav.getSetColumn());
//                            }
//                            sql.append(Constants.SPACE);
//                            sql.append(rav.getSetOperator().toUpperCase());
//                            sql.append(Constants.SPACE);
//                            if (Constants.NUMBER_TEPY.indexOf(rav.getColumnType()) >= 0) {
//                                sql.append(rav.getSetValue());
//                            } else {
//                                sql.append(Constants.SINGLE_QUOTATION_MARK);
//                                sql.append(rav.getSetValue());
//                                if (Constants.FUZZY_QUERY_KEY.equals(rav.getSetOperator().toUpperCase())) {
//                                    sql.append(Constants.PERCENTAGE_MARK);
//                                }
//                                sql.append(Constants.SINGLE_QUOTATION_MARK);
//                            }
//                            sql.append(Constants.SPACE);
//                            count++;
//                        }
//                        sql.append(Constants.RIGHT_PARENTHESES);
//                    } else {
//                        return null;
//                    }
//                }
//            }
//        }
//        return sql.toString();
//    }


}
