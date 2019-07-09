package com.crt.common.vo;

import java.util.List;

/**
 *
 */
public class PageParamVO {

    /**
     * 当前页数
     */
    private Integer curPage;
    /**
     * 每页数量
     */
    private Integer pageSize;
    /**
     * 排序字段
     */
    private String sortIndx;
    /**
     * 排序方式
     */
    private String sortDir;
    private Long totalRecords;
    private List<?> data;

    public Integer getCurPage() {
        return curPage;
    }

    public void setCurPage(Integer curPage) {
        this.curPage = curPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortIndx() {
        return sortIndx;
    }

    public void setSortIndx(String sortIndx) {
        this.sortIndx = sortIndx;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    public Long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PageParamVO{" +
                "curPage=" + curPage +
                ", pageSize=" + pageSize +
                ", sortIndx='" + sortIndx + '\'' +
                ", sortDir='" + sortDir + '\'' +
                ", totalRecords=" + totalRecords +
                ", data=" + data +
                '}';
    }
}
