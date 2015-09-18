package com.oct.ga.talent.dao;

import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.talent.TalentInfo;

public interface GaTalentVoteDao
{
	public void add(String accountId, String voteAccountId, int timestamp);

	public boolean isExist(String accountId, String voteAccountId);

	public Page<TalentInfo> selectVote(String accountId, int pageNum, int pageSize);
}
