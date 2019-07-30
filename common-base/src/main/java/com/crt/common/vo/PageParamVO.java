package com.crt.common.vo;

import org.apache.commons.lang3.StringUtils;

/**
 * 分页查询实体基类
 */
public class PageParamVO<T> {

    /**
     * 当前页数
     */
    private Integer page;
    /**
     * 每页数量
     */
    private Integer limit;
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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        if (null == page || page < 1) {
            page = 1;
        }
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        if (null == limit || limit < 1) {
            limit  = 10;
        }
        this.limit = limit;
    }

    public Integer getStartPage() {
        return (this.page - 1) * this.limit;
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
