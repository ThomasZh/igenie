package com.oct.ga.talent.dao;

import java.util.List;

import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.talent.TalentInfo;
import com.oct.ga.comm.domain.talent.TalentScore;

public interface GaTalentScoreDao
{
	public void add(String accountId, int timestamp);

	public boolean isExist(String accountId);

	public int queryVotedNum(String accountId);

	public void modifyVotedNum(String accountId, int num, int timestamp);

	public int queryMaxPosition();

	public int queryPosition(String accountId);

	public void modifyPosition(String accountId, int num, int timestamp);

	public List<TalentScore> queryTalentlistOrderByPosition(int votedNum);

	public TalentScore select(String accountId);

	public Page<TalentInfo> select(int pageNum, int pageSize);

	public int queryFollowingNum(String accountId);

	public int queryFollowedNum(String accountId);

	public int queryVoteNum(String accountId);

	public void modifyFollowingNum(String accountId, int num, int timestamp);

	public void modifyFollowedNum(String accountId, int num, int timestamp);

	public void modifyVoteNum(String accountId, int num, int timestamp);

}
