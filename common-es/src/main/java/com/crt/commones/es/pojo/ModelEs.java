package com.crt.commones.es.pojo;


import com.frameworkset.orm.annotation.ESId;
import org.frameworkset.elasticsearch.entity.ESBaseData;

/**
 * ES Base Model
 *
 * @author dreamingo
 */
public abstract class ModelEs extends ESBaseData {

    /**
     * 数据对象主键
     * id: 索引文档主键
     */
    @ESId(readSet = true, persistent = true)
    public String objId;

    /**
     * 数据对象版本
     * version: 索引文档版本
     */
    public Long v = 1L;

    /**
     * 数据状态
     */
    public Integer st = 1;
}
