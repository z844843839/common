package com.crt.common.vo;


/**
 * @Description
 * @Author changyandong@e6yun.com
 * @Created Date: 2019/1/18 10:02
 * @ClassName DefalutAbstractUserImpl
 * @Version: 1.0
 */
public class DefaultAbstractUser implements AbstractUser{

    private Integer userId;
    private Integer webgisUserId;
    private Integer corpId;
    private Integer orgId;
    private Integer parentId;

    public DefaultAbstractUser() {
    }

    public DefaultAbstractUser(Integer userId, Integer webgisUserId, Integer corpId, Integer orgId,Integer parentId) {
        this.userId = userId;
        this.webgisUserId = webgisUserId;
        this.corpId = corpId;
        this.orgId = orgId;
        this.parentId = parentId;
    }

    @Override
    public Integer getUserId() {
        return userId;
    }

    @Override
    public Integer getWebgisUserId() {
        return webgisUserId;
    }

    @Override
    public Integer getCorpId() {
        return corpId;
    }

    @Override
    public Integer getOrgId() {
        return orgId;
    }

    @Override
    public Integer getParentId(){return  parentId;}

    @Override
    public Integer getCarrierId() {
        return null;
    }

    @Override
    public Integer getUserSourceType() {
        return null;
    }

    @Override
    public Integer getUserSourceId() {
        return null;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setWebgisUserId(Integer webgisUserId) {
        this.webgisUserId = webgisUserId;
    }

    public void setCorpId(Integer corpId) {
        this.corpId = corpId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
