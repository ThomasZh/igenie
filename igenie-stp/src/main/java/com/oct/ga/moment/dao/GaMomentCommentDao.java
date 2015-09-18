package com.oct.ga.moment.dao;

import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.moment.GaMomentCommentObject;

public interface GaMomentCommentDao
{
	public void add(String momentId, String accountId, String txt, int timestamp);

	public int countNum(String momentId);

	public Page<GaMomentCommentObject> queryPagination(String momentId, short pageNum, short pageSize);
}
