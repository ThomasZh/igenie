package com.oct.ga.comm.domain.task;

import java.io.Serializable;

public class TaskMember implements Serializable {

	public TaskMember() {
	}

	public TaskMember(String taskId, String accountId, String accountName,
			short isCreater, short state, int createTime, int lastUpdateTime) {
		this.setAccountId(accountId);
		this.setAccountName(accountName);
		this.setTaskId(taskId);
		this.setInviteState(state);
		this.setIsCreater(isCreater);
		this.setCreateTime(createTime);
		this.setLastUpdateTime(lastUpdateTime);
	}

	private String taskId;
	private String accountId;
	private String accountName;
	private short isCreater;
	private short inviteState;
	private int createTime;
	private int lastUpdateTime;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String TaskId) {
		this.taskId = TaskId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String AccountId) {
		this.accountId = AccountId;
	}

	public short getIsCreater() {
		return isCreater;
	}

	public void setIsCreater(short isCreate) {
		this.isCreater = isCreate;
	}

	public short getInviteState() {
		return inviteState;
	}

	public void setInviteState(short State) {
		this.inviteState = State;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public int getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(int lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getCreateTime() {
		return createTime;
	}

	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	private static final long serialVersionUID = 3255347399784184530L;
}
