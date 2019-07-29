package com.crt.common.vo;

import org.apache.commons.lang3.StringUtils;

/**
 * 分页查询实体基类
 */
public class PageParamVO<T> {

    /**
     * 当前页数
     */
    private Integer curPage;
    /**
     * 每页数量
     */
    private Integer pageSize;
    /**
     * 起始查询数
     */
    private Integer startPage;
    /**
     * 排序字段
     */
    private String sortIndx;
    /**
     * 排序方式
     */
    private String sortDir;
    /**
     * 实体泛型
     */
    private T entity;

    public Integer getCurPage() {
        return curPage;
    }

    public void setCurPage(Integer curPage) {
        if (null == curPage || curPage < 1) {
            curPage = 1;
        }
        this.curPage = curPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (null == pageSize || pageSize < 1) {
            pageSize  = 10;
        }
        this.pageSize = pageSize;
    }

    public Integer getStartPage() {
        return (this.curPage - 1) * this.pageSize;
    }

    public String getSortIndx() {
        return sortIndx;
    }

    public void setSortIndx(String sortIndx) {
        if (StringUtils.isEmpty(sortIndx)){
            this.sortIndx = "id";
        }else{
            this.sortIndx = sortIndx;
        }
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        if (StringUtils.isEmpty(sortDir)){
            this.sortDir = "DESC";
        }else{
            this.sortDir = sortDir;
        }
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
