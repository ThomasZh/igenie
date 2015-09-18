package com.oct.ga.task.dao;

import com.oct.ga.comm.domain.BadgeNumberJsonBean;
import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.msg.NotifyTaskLog;

public interface GaTaskActivityDao
{
	public BadgeNumberJsonBean queryUnreadNumberByProject(String projectId, String accountId, int lastTryTime);

	/**
	 * 
	 * include children task's activity
	 * 
	 * @param taskId
	 * @param timestamp
	 * @return List<NotifyTaskLog>
	 */
	public NotifyTaskLog queryLastOneByProject(String projectId, String toAccountId, int timestamp);

	/**
	 * 
	 * 
	 * @param taskId
	 * @param pageNum
	 * @param pageSize
	 * @return Page<NotifyTaskLog>
	 */
	public Page<NotifyTaskLog> queryPagination(String taskId, String accountId, int timestamp, int pageNum, int pageSize);

	/**
	 * 
	 * 
	 * @param myAccountId
	 * @param pageNum
	 * @param pageSize
	 * @return Page<NotifyTaskLog>
	 */
	public Page<NotifyTaskLog> queryPaginationByAccount(String accountId, int timestamp, int pageNum, int pageSize);

	/**
	 * 
	 * 
	 * @param data
	 */
	public void add(NotifyTaskLog data);

	public void addExtend(String activityId, String userId, short state, int timestamp);

	public void updateExtendState(String activityId, String userId, short state, int timestamp);

	public int batchUpdateToSyncState(String taskId, String userId, short state, int timestamp);
}