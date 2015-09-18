package com.oct.ga.moment.dao;

import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.moment.GaMomentFavoriteObject;

public interface GaMomentFavoriteDao
{
	public void add(String momentId, String accountId, int timestamp);

	public boolean isExist(String momentId, String accountId);
	
	public int countNum(String momentId);

	public Page<GaMomentFavoriteObject> queryPagination(String momentId, short pageNum, short pageSize);
}
