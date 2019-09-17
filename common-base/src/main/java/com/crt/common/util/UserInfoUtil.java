package com.crt.common.util;

import com.crt.common.config.UacCookieProperties;
import com.crt.common.constant.Constants;
import com.crt.common.redis.RedisUtil;
import com.crt.common.vo.E6Wrapper;
import com.crt.common.vo.E6WrapperUtil;
import com.crt.common.vo.RowAuthVO;
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
            Object result = userCache.get(token).get("userVO");
            if (result == null) {
                return E6WrapperUtil.error("token错误,用户信息获取失败");
            } else {
                UserRedisVO userRedisVO = new UserRedisVO();
                userRedisVO = (UserRedisVO) BeanUtil.Copy(userRedisVO, result, false);
                return E6WrapperUtil.ok(userRedisVO);
            }

        }
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
     * @param currentPath
     * @return
     */
    public static E6Wrapper<String> getRowDataAuthSQL(String currentPath) {
        StringBuffer sql = new StringBuffer();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            token = (String) request.getAttribute("Authorization");
        }
        if (StringUtils.isEmpty(token)) {
            return E6WrapperUtil.error("token不存在,用户信息获取失败");
        } else {
            if (StringUtils.isEmpty(currentPath)) {
                return E6WrapperUtil.error("当前路径为空");
            }
            if (userCache.get(token).containsKey("rowAuth")) {
                List<RowAuthVO> rowAuthList = (List<RowAuthVO>) userCache.get(token).get("rowAuth");
                if (null != rowAuthList && rowAuthList.size() > 0) {
                    List<RowAuthVO> currentRowAuthList = new ArrayList<>();
                    for (RowAuthVO rav : rowAuthList) {
                        if (rav.getUrl().equals(currentPath)) {
                            currentRowAuthList.add(rav);
                        }
                    }
                    if (currentRowAuthList.size() > 0) {
                        Collections.sort(currentRowAuthList, new Comparator<RowAuthVO>() {
                            @Override
                            public int compare(RowAuthVO o1, RowAuthVO o2) {
                                long diff = o1.getOrgRoleCode() - o2.getOrgRoleCode();
                                if (diff > 0) {
                                    return 1;
                                } else if (diff < 0) {
                                    return -1;
                                }
                                return 0;
                            }
                        }); // 按orgRoleCode排序
                        int count = 0;
                        sql.append(Constants.LEFT_PARENTHESES);
                        for (int i = 0; i < currentRowAuthList.size(); i++) {
                            RowAuthVO rav = currentRowAuthList.get(i);
                            if (count >= 1) {
                                RowAuthVO lastRav = currentRowAuthList.get(i - 1);
                                if (rav.getOrgRoleCode() == lastRav.getOrgRoleCode()) {
                                    sql.append(Constants.SPACE);
                                    sql.append(Constants.CONNECTOR_AND);
                                    sql.append(Constants.SPACE);
                                } else {
                                    sql.append(Constants.SPACE);
                                    sql.append(Constants.RIGHT_PARENTHESES);
                                    sql.append(Constants.SPACE);
                                    sql.append(Constants.CONNECTOR_OR);
                                    sql.append(Constants.SPACE);
                                    sql.append(Constants.LEFT_PARENTHESES);
                                    sql.append(Constants.SPACE);
                                }
                            }
                            //拼接SQL 表名.列名 操作符 值
                            sql.append(Constants.SPACE);
                            sql.append(rav.getSetTable());
                            sql.append(Constants.SPOT);
                            sql.append(rav.getSetColumn());
                            sql.append(Constants.SPACE);
                            sql.append(rav.getSetOperator().toUpperCase());
                            sql.append(Constants.SPACE);
                            if (Constants.NUMBER_TEPY.indexOf(rav.getColumnType()) >= 0) {
                                sql.append(rav.getSetValue());
                            } else {
                                sql.append(Constants.SINGLE_QUOTATION_MARK);
                                sql.append(rav.getSetValue());
                                if (Constants.FUZZY_QUERY_KEY.equals(rav.getSetOperator().toUpperCase())) {
                                    sql.append(Constants.PERCENTAGE_MARK);
                                    sql.append(Constants.SINGLE_QUOTATION_MARK);
                                }
                            }
                            sql.append(Constants.SPACE);
                            count++;
                        }
                        sql.append(Constants.RIGHT_PARENTHESES);
                    }
                }
            }
        }
        return E6WrapperUtil.ok(sql.toString());
    }
}
