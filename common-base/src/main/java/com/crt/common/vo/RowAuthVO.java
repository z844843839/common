package com.crt.common.vo;

import lombok.Data;

/**
 * 数据权限VO
 * @author malin
 */
@Data
public class RowAuthVO{
    /**
     * 组织角色编码
     */
    private Long orgRoleCode;
    /**
     * 表名
     */
    private String setTable;
    /**
     * 列名
     */
    private String setColumn;
    /**
     * 列类型
     */
    private String columnType;
    /**
     * 操作符
     */
    private String setOperator;
    /**
     * 值
     */
    private String setValue;
    /**
     * 列表请求
     */
    private String url;
    /**
     * 列别名
     */
    private String columnAlias;
}
