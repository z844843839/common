package com.crt.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class MenusRedisVO {
    private Integer id;
    //数据类型(0菜单 1资源)
    private int tabMold;
    //菜单编码
    private long menuCode;
    //父菜单编码
    private long parentId;
    //请求url
    private String url;
    //路径编码
    private String path;
    //组件
    private String component;
    //子集 忽略空值输出JSON
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MenusRedisVO> children;
    //菜单标题
    private String title;
    //菜单图标
    private String icon;
    //菜单类型（0 目录 1.菜单 2url 3按钮）
    private int menuType;
    //菜单状态（0正常、1隐藏）
    private int menuState;
    //前端重定向
    private String redirect;
    //是否外联(0否、1是)
    private int alwaysShow;
    //菜单归属分类
    private long ascription;

    public int getTabMold() {
        return tabMold;
    }

    public void setTabMold(int tabMold) {
        this.tabMold = tabMold;
    }

    public long getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(long menuCode) {
        this.menuCode = menuCode;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public List<MenusRedisVO> getChildren() {
        return children;
    }

    public void setChildren(List<MenusRedisVO> children) {
        this.children = children;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getMenuType() {
        return menuType;
    }

    public void setMenuType(int menuType) {
        this.menuType = menuType;
    }

    public int getMenuState() {
        return menuState;
    }

    public void setMenuState(int menuState) {
        this.menuState = menuState;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public int getAlwaysShow() {
        return alwaysShow;
    }

    public void setAlwaysShow(int alwaysShow) {
        this.alwaysShow = alwaysShow;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getAscription() {
        return ascription;
    }

    public void setAscription(long ascription) {
        this.ascription = ascription;
    }
}
