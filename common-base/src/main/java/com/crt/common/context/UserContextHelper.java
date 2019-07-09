package com.crt.common.context;



import com.crt.common.constant.ConstOfUserContext;
import org.slf4j.MDC;

import static java.lang.Integer.parseInt;

/**
 * @Description  获取用户上下文工具类
 * @Author changyandong@e6yun.com
 * @Created Date: 2018/7/19 10:26
 * @ClassName UserContextHelper
 * @Version: 1.0
 */
public class UserContextHelper {
    private final static ThreadLocal<Integer> USER_ID = new ThreadLocal<Integer>();
    private final static ThreadLocal<Integer> WEBGIS_USER_ID = new ThreadLocal<Integer>();
    private final static ThreadLocal<Integer> CORP_ID = new ThreadLocal<Integer>();
    private final static ThreadLocal<Integer> ORG_ID = new ThreadLocal<Integer>();
    private final static ThreadLocal<Integer> PARENT_ID = new ThreadLocal<>();
    //日志的MDC中用户ID对应的key名称
    public final static String USER_ID_IN_MDC = "userId";

    /**
     * 保存对应值到threadLocal中
     * 增加字段，这个switch case也要增加
     * @param headerName
     * @param headerValue
     */
    public static void saveHeaderInThreadLocal(String headerName,String headerValue){
        switch (headerName){
            case ConstOfUserContext.USER_ID:
                //在MDC中添加用户ID，方便日志输出
                MDC.put(USER_ID_IN_MDC,headerValue);
                UserContextHelper.setUserId(parseInt(headerValue));break;
            case ConstOfUserContext.WEBGIS_USER_ID :
                UserContextHelper.setWebgisUserId(parseInt(headerValue));break;
            case ConstOfUserContext.CORP_ID :
                UserContextHelper.setCorpId(parseInt(headerValue));break;
            case ConstOfUserContext.ORG_ID :
                UserContextHelper.setOrgId(parseInt(headerValue));break;
            case ConstOfUserContext.PARENT_ID :
                UserContextHelper.setParentId(parseInt(headerValue));
        }
    }

    public static Integer getUserId(){
        return USER_ID.get();
    }

    public static Integer getWebgisUserId() {
        return WEBGIS_USER_ID.get();
    }

    public static Integer getCorpId() {
        return CORP_ID.get();
    }

    public static Integer getOrgId() {
        return ORG_ID.get();
    }

    public static void setUserId(Integer userId){
        USER_ID.set(userId);
    }

    public static void setWebgisUserId(Integer webgisUserId){
        WEBGIS_USER_ID.set(webgisUserId);
    }

    public static void setCorpId(Integer corpId){
        CORP_ID.set(corpId);
    }

    public static void setOrgId(Integer orgId){
        ORG_ID.set(orgId);
    }

    public static Integer getParentId() {
        return PARENT_ID.get();
    }
    public static void setParentId(Integer parentId) {
        PARENT_ID.set(parentId);
    }
}
