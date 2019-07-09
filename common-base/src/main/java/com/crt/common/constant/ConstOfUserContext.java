package com.crt.common.constant;

/**
 * @Author liupengfei@e6yun.com
 * @Date 2018/7/25 14:57
 * @Description 用户上下文常量
 **/
public class ConstOfUserContext {
    /**
     * 用户ID 在header中的名称
     */
    public static final String USER_ID = "userId";
    /**
     * webgisuserid 在header中的名称
     */
    public static final String WEBGIS_USER_ID = "webgisUserId";
    /**
     * 公司ID 在header中的名称
     */
    public static final String CORP_ID = "corpId";
    /**
     * 组织结构ID 在header中的名称
     */
    public static final String ORG_ID = "orgId";

    public static final String PARENT_ID = "parentId";

    /**
     * 需要透传的header的名称
     */
    public static final String[] HEADERS = {USER_ID,WEBGIS_USER_ID,CORP_ID,ORG_ID,PARENT_ID};
}
