package com.crt.common.vo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author : malin
 * @Description: 所有实体的基类
 */
@MappedSuperclass
public class BaseEntity implements Serializable {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
