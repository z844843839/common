package com.crt.common.vo;

public class QuickMenusRedisVO {
    //快捷菜单编码
    private long quickmenuCode;
    //菜单标题
    private String quickmenuTitle;
    //菜单图标
    private String quickmenuIcon;
    //菜单状态（0正常、1隐藏）
    private int menuState;
    //描述
    private String quickmenuDesc;
    //逻辑删除标识(0正常 1删除)
    private int deleted;
    //创建人id
    private long createdId;
    //创建人姓名
    private String createdBy;
    //修改人id
    private long modifiedId;
    //修改人姓名
    private String modifiedBy;

    public long getQuickmenuCode() {
        return quickmenuCode;
    }

    public void setQuickmenuCode(long quickmenuCode) {
        this.quickmenuCode = quickmenuCode;
    }

    public String getQuickmenuTitle() {
        return quickmenuTitle;
    }

    public void setQuickmenuTitle(String quickmenuTitle) {
        this.quickmenuTitle = quickmenuTitle;
    }

    public String getQuickmenuIcon() {
        return quickmenuIcon;
    }

    public void setQuickmenuIcon(String quickmenuIcon) {
        this.quickmenuIcon = quickmenuIcon;
    }

    public int getMenuState() {
        return menuState;
    }

    public void setMenuState(int menuState) {
        this.menuState = menuState;
    }

    public String getQuickmenuDesc() {
        return quickmenuDesc;
    }

    public void setQuickmenuDesc(String quickmenuDesc) {
        this.quickmenuDesc = quickmenuDesc;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public long getCreatedId() {
        return createdId;
    }

    public void setCreatedId(long createdId) {
        this.createdId = createdId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public long getModifiedId() {
        return modifiedId;
    }

    public void setModifiedId(long modifiedId) {
        this.modifiedId = modifiedId;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
