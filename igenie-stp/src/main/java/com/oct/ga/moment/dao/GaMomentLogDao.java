package com.oct.ga.moment.dao;

import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.moment.GaMomentLogObject;

public interface GaMomentLogDao
{
	public void add(String momentId, String accountId, short action, String txt, String toAccountId, int timestamp);

	public Page<GaMomentLogObject> queryPagination(String toAccountId, int pageNum, int pageSize);

	public int modifySyncState(String toAccountId, short syncState);
}
