package com.crt.common.vo;

import java.util.List;

public class Pages<T> {
    //总数
    private long total;
    //结果集
    private List<T> data;

    public Pages(){

    }
    public Pages(long total,List<T> data){
        this.total=total;
        this.data=data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
