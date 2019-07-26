package com.crt.common.vo;

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
    private Integer created_id;

    /**
     * 创建人姓名
     */
    private String created_by;

    /**
     * 创建时间
     */
    private Date created_at;

    /**
     * 修改人ID
     */
    private Integer modified_id;

    /**
     * 修改人姓名
     */
    private String modified_by;

    /**
     * 修改时间
     */
    private Date modified_at;

    public void setCreated_id(Integer created_id) {
        this.created_id = created_id;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public void setModified_id(Integer modified_id) {
        this.modified_id = modified_id;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public void setModified_at(Date modified_at) {
        this.modified_at = modified_at;
    }
}
