package com.crt.common.vo;

import lombok.Data;

/**
 * 数据权限PO
 * @author malin
 */
@Data
public class RowDataAuthVo extends BaseEntity {
    /**
     * 组织角色编码
     */
    private Long orgRoleCode;
    /**
     * 菜单编码
     */
    private Long menuCode;
    /**
     * 过滤表达式（展示）
     */
    private String formula;
    /**
     * 过滤表达式（sql）
     */
    private String formulaSql;
    /**
     * 请求url
     */
    private String url;

}
