package com.crt.common.vo;

/**
 * @Description 抽象登录用户接口
 * @Author changyandong@e6yun.com
 * @Created Date: 2018/11/12 17:20
 * @InterfaceName AbstractUser
 * @Version: 1.0
 */
public interface AbstractUser {
    Integer getUserId();
    Integer getWebgisUserId();
    Integer getCorpId();
    Integer getOrgId();
    Integer getCarrierId();
    Integer getUserSourceType();
    Integer getUserSourceId();
    Integer getParentId();
    // Map<String,Object> getExtendMap();
}
