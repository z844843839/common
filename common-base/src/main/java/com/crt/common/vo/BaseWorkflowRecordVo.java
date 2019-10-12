package com.crt.common.vo;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 工作流记录Vo
 * @author malin
 */
@Data
public class BaseWorkflowRecordVo extends BaseEntity {
    /**
     * 类型代码
     */
    private String docType;
    /**
     * 实体ID
     */
    private String entityId;
    /**
     * 任务ID
     */
    private Integer taskId;

    /**
     * 流程实例id
     */
    private Integer processInstanceId;
    /**
     * 任务状态（0驳回 1终止）
     */
    @NonNull
    private Integer state;

    /**
     * 创建时间
     */
    private Date createAt;

}
