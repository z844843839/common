package com.crt.common.vo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * @Description  拥有接收MQ返回业务数据的对象
 * @Author zhuhao@e6yun.com
 * @Created Date 2019/8/21 12:35
 * @Modifier
 * @Modified Date
 * @Modified Remark
 * @Version 1.0
 **/
@Data
public class BaseBpmTraceRecord {
	private Integer id;
	private Integer entityId;
	private String docType;
	private String businessKey;
	private String processId;
	private String assigneeId;
	/**
	 * complete 完成
	 * active   审批中
	 * rollback 驳回到发起人
	 * delete   终止
	 * noRepairValue  无维修价值
	 */
	private String approvalStatus;
	private Date approvalBtime;
	private Date approvalEtime;
	private Integer deleted;
	private String comments;
}
