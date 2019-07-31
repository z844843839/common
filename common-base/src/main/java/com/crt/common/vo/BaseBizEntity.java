package com.crt.common.vo;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author : malin
 * @Description: 业务实体基类
 */
@MappedSuperclass
public class BaseBizEntity extends BaseEntity implements Serializable {

    /**
     * 创建人ID
     */
    @Column(name = "created_id")
    private Integer createdId;

    /**
     * 创建人姓名
     */
    @Column(name = "created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @Column(name = "created_at")
    private Date createdAt;

    /**
     * 修改人ID
     */
    @Column(name = "modified_id")
    private Integer modifiedId;

    /**
     * 修改人姓名
     */
    @Column(name = "modified_by")
    private String modifiedBy;

    /**
     * 修改时间
     */
    @Column(name = "modified_at")
    private Date modifiedAt;

    public Integer getCreatedId() {
        return createdId;
    }

    public void setCreatedId(Integer createdId) {
        this.createdId = createdId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getModifiedId() {
        return modifiedId;
    }

    public void setModifiedId(Integer modifiedId) {
        this.modifiedId = modifiedId;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
