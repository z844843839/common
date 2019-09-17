package com.crt.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class MenusRedisVO {
    private Integer id;
    /**
     * 数据类型(0菜单 1资源)
     */
    private Integer tabMold;
    /**
     * 菜单编码
     */
    private Long menuCode;
    /**
     * 父菜单编码
     */
    private Long parentId;
    /**
     * 请求url
     */
    private String url;
    /**
     * 路径编码
     */
    private String path;
    /**
     * 组件
     */
    private String component;
    /**
     * 组件名称
     */
    private String componentName;
    /**
     * 子集 忽略空值输出JSON
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MenusRedisVO> children;
    /**
     * 菜单标题
     */
    private String title;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 菜单类型（0 目录 1.菜单 2url 3按钮）
     */
    private Integer menuType;
    /**
     * 菜单状态（0正常、1隐藏）
     */
    private Integer menuState;
    /**
     * 前端重定向
     */
    private String redirect;
    /**
     * 是否外联（0内部 1外部）
     */
    private Integer linkType ;
    /**
     * 是否缓存 0 缓存 1 不缓存
     */
    private Integer alwaysShow;
    /**
     * 菜单归属分类
     */
    private Long ascription;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTabMold() {
        if (null == tabMold){
            return 0;
        }
        return tabMold;
    }

    public void setTabMold(Integer tabMold) {
        this.tabMold = tabMold;
    }

    public Long getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(Long menuCode) {
        this.menuCode = menuCode;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
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

    public Integer getMenuType() {
        if (null == menuType){
            return 0;
        }
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public Integer getMenuState() {
        if (null == menuState){
            return 0;
        }
        return menuState;
    }

    public void setMenuState(Integer menuState) {
        this.menuState = menuState;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public Integer getLinkType() {
        if (null == linkType){
            return 0;
        }
        return linkType;
    }

    public void setLinkType(Integer linkType) {
        this.linkType = linkType;
    }

    public Integer getAlwaysShow() {
        if (null == alwaysShow){
            return 0;
        }
        return alwaysShow;
    }

    public void setAlwaysShow(Integer alwaysShow) {
        this.alwaysShow = alwaysShow;
    }

    public Long getAscription() {
        return ascription;
    }

    public void setAscription(Long ascription) {
        this.ascription = ascription;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    @Override
    public String toString() {
        return "MenusRedisVO{" +
                "id=" + id +
                ", tabMold=" + tabMold +
                ", menuCode=" + menuCode +
                ", parentId=" + parentId +
                ", url='" + url + '\'' +
                ", path='" + path + '\'' +
                ", component='" + component + '\'' +
                ", children=" + children +
                ", title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", menuType=" + menuType +
                ", menuState=" + menuState +
                ", redirect='" + redirect + '\'' +
                ", linkType=" + linkType +
                ", alwaysShow=" + alwaysShow +
                ", ascription=" + ascription +
                '}';
    }
}
