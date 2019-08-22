package com.crt.common.vo;

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
public class BaseBpmTraceRecord {
	private Integer id;
	private Integer entityId;
	private String docType;
	private String businessKey;
	private String processId;
	private Integer assigneeId;
	private String approvalStatus;
	private Date approvalBtime;
	private Date approvalEtime;
	private Integer deleted;
	private String comments;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public Integer getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(Integer assigneeId) {
		this.assigneeId = assigneeId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Date getApprovalBtime() {
		return approvalBtime;
	}

	public void setApprovalBtime(Date approvalBtime) {
		this.approvalBtime = approvalBtime;
	}

	public Date getApprovalEtime() {
		return approvalEtime;
	}

	public void setApprovalEtime(Date approvalEtime) {
		this.approvalEtime = approvalEtime;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BaseBpmTraceRecord that = (BaseBpmTraceRecord) o;
		return id == that.id &&
				entityId == that.entityId &&
				assigneeId == that.assigneeId &&
				deleted == that.deleted &&
				Objects.equals(docType, that.docType) &&
				Objects.equals(businessKey, that.businessKey) &&
				Objects.equals(processId, that.processId) &&
				Objects.equals(approvalStatus, that.approvalStatus) &&
				Objects.equals(approvalBtime, that.approvalBtime) &&
				Objects.equals(approvalEtime, that.approvalEtime) &&
				Objects.equals(comments, that.comments);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id, entityId, docType, businessKey, processId, assigneeId, approvalStatus, approvalBtime, approvalEtime, deleted, comments);
	}
}
