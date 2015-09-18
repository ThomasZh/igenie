package com.oct.ga.comm.domain.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TemplateDefineJsonBean
		implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8103595584829402200L;
	private String templateId;
	private String templateName;
	/**
	 * ProjectTemplate=301, TaskTemplate=302
	 */
	private short templateType;
	/**
	 * Recommend 321, Vendor 322, Mine 323
	 */
	private short supplierType;
	private int version;
	private String templatePid;
	private String templateDesc;
	private int startTime;
	private int endTime;
	/**
	 * normal task=311,checklist=312,clwc=313,input=314
	 */
	private short extAttrType;
	private List<ChecknameJsonBean> extAttr = new ArrayList<ChecknameJsonBean>();
	private List<TemplateDefineJsonBean> children = new ArrayList<TemplateDefineJsonBean>();
	private List<AttachmentJsonBean> attachments;

	private String accountId;
	private String accountName;
	private String taskId;

	private int permissionMode;
	private boolean splitForEachMember;
	private boolean feedbackInvite;
	private boolean feedbackUpdate;
	private int createTime;
	private int lastUpdateTime;
	private int copys;

	public void addChildExtAttr(ChecknameJsonBean childAttr)
	{
		extAttr.add(childAttr);
	}

	public short getSupplierType()
	{
		return supplierType;
	}

	public void setSupplierType(short supplierType)
	{
		this.supplierType = supplierType;
	}

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public String getAccountName()
	{
		return accountName;
	}

	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}

	public int getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(int createTime)
	{
		this.createTime = createTime;
	}

	public int getLastUpdateTime()
	{
		return lastUpdateTime;
	}

	public void setLastUpdateTime(int lastUpdateTime)
	{
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getPermissionMode()
	{
		return permissionMode;
	}

	public void setPermissionMode(int permissionMode)
	{
		this.permissionMode = permissionMode;
	}

	public boolean isSplitForEachMember()
	{
		return splitForEachMember;
	}

	public void setSplitForEachMember(boolean splitForEachMember)
	{
		this.splitForEachMember = splitForEachMember;
	}

	public boolean isFeedbackInvite()
	{
		return feedbackInvite;
	}

	public void setFeedbackInvite(boolean feedbackInvite)
	{
		this.feedbackInvite = feedbackInvite;
	}

	public boolean isFeedbackUpdate()
	{
		return feedbackUpdate;
	}

	public void setFeedbackUpdate(boolean feedbackCompleted)
	{
		this.feedbackUpdate = feedbackCompleted;
	}

	public short getTemplateType()
	{
		return templateType;
	}

	public void setTemplateType(short templateType)
	{
		this.templateType = templateType;
	}

	public int getVersion()
	{
		return version;
	}

	public void setVersion(int version)
	{
		this.version = version;
	}

	public String getTemplatePid()
	{
		return templatePid;
	}

	public void setTemplatePid(String templatePid)
	{
		this.templatePid = templatePid;
	}

	public String getTemplateDesc()
	{
		return templateDesc;
	}

	public void setTemplateDesc(String templateDesc)
	{
		this.templateDesc = templateDesc;
	}

	public int getStartTime()
	{
		return startTime;
	}

	public void setStartTime(int startTime)
	{
		this.startTime = startTime;
	}

	public int getEndTime()
	{
		return endTime;
	}

	public void setEndTime(int endTime)
	{
		this.endTime = endTime;
	}

	public short getExtAttrType()
	{
		return extAttrType;
	}

	public void setExtAttrType(short extAttrType)
	{
		this.extAttrType = extAttrType;
	}

	public List<ChecknameJsonBean> getExtAttr()
	{
		return extAttr;
	}

	public void setExtAttr(List<ChecknameJsonBean> extAttr)
	{
		this.extAttr = extAttr;
	}

	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

	public String getTemplateName()
	{
		return templateName;
	}

	public void setTemplateName(String templateName)
	{
		this.templateName = templateName;
	}

	public List<TemplateDefineJsonBean> getChildren()
	{
		return children;
	}

	public void setChildren(List<TemplateDefineJsonBean> children)
	{
		this.children = children;
	}

	public List<AttachmentJsonBean> getAttachments()
	{
		return attachments;
	}

	public void setAttachments(List<AttachmentJsonBean> attachments)
	{
		this.attachments = attachments;
	}

	public int getCopys()
	{
		return copys;
	}

	public void setCopys(int copys)
	{
		this.copys = copys;
	}

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}
}
