package com.oct.ga.task.dao;

import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.taskext.GaTaskLog;

public interface GaTaskLogDao
{
	public void insert(GaTaskLog log, int timestamp);

	public GaTaskLog query(String logId);

	public void insert(String logId, String toAccountId, String channelId, short actionTag, short syncState,
			int timestamp);

	public Page<String> queryLogPagination(String toAccountId, short actionTag, int pageNum, int pageSize);

	public Page<String> queryLogPagination(String toAccountId, int pageNum, int pageSize);
}
