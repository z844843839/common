package com.crt.common.vo;

/**
 * 数据权限VO
 * @author malin
 */
public class RowAuthVO{

    private String tag;
    private Long orgRoleCode;
    private String setTable;
    private String setColumn;
    private String columnType;
    private String setOperator;
    private String setValue;
    private String url;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getOrgRoleCode() {
        return orgRoleCode;
    }

    public void setOrgRoleCode(Long orgRoleCode) {
        this.orgRoleCode = orgRoleCode;
    }

    public String getSetTable() {
        return setTable;
    }

    public void setSetTable(String setTable) {
        this.setTable = setTable;
    }

    public String getSetColumn() {
        return setColumn;
    }

    public void setSetColumn(String setColumn) {
        this.setColumn = setColumn;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getSetOperator() {
        return setOperator;
    }

    public void setSetOperator(String setOperator) {
        this.setOperator = setOperator;
    }

    public String getSetValue() {
        return setValue;
    }

    public void setSetValue(String setValue) {
        this.setValue = setValue;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "RowAuthVO{" +
                "tag='" + tag + '\'' +
                ", orgRoleCode=" + orgRoleCode +
                ", setTable='" + setTable + '\'' +
                ", setColumn='" + setColumn + '\'' +
                ", columnType='" + columnType + '\'' +
                ", setOperator='" + setOperator + '\'' +
                ", setValue='" + setValue + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
