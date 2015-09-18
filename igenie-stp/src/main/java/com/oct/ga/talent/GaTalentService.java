package com.oct.ga.talent;

import java.util.List;

import com.oct.ga.comm.domain.talent.TalentInfo;
import com.oct.ga.comm.domain.talent.TalentScore;

public interface GaTalentService
{
	public void join(String accountId, int timestamp);

	public boolean isJoin(String accountId);

	public void vote(String accountId, String voteAccountId, int timestamp);

	public boolean isVote(String accountId, String voteAccountId);

	public TalentScore query(String accountId);

	public List<TalentInfo> query(int pageNum, int pageSize);
	
	public List<TalentInfo> queryVote(String accountId, int pageNum, int pageSize);
}
