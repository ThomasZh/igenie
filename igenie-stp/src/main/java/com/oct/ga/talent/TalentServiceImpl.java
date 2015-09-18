package com.oct.ga.talent;

import java.util.List;

import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.talent.TalentInfo;
import com.oct.ga.comm.domain.talent.TalentScore;
import com.oct.ga.talent.dao.GaTalentInfoDao;
import com.oct.ga.talent.dao.GaTalentScoreDao;
import com.oct.ga.talent.dao.GaTalentVoteDao;

public class TalentServiceImpl
		implements GaTalentService
{
	@Override
	public void join(String accountId, int timestamp)
	{
		if (!talentInfoDao.isExist(accountId))
			talentInfoDao.add(accountId, timestamp);
	}

	@Override
	public boolean isJoin(String accountId)
	{
		return talentInfoDao.isExist(accountId);
	}

	@Override
	public void vote(String accountId, String voteAccountId, int timestamp)
	{
		talentVoteDao.add(accountId, voteAccountId, timestamp);

		if (talentScoreDao.isExist(accountId)) {
			int num = talentScoreDao.queryVotedNum(accountId);
			List<TalentScore> scores = talentScoreDao.queryTalentlistOrderByPosition(num);
			talentScoreDao.modifyVotedNum(accountId, ++num, timestamp);

			int i = 0;
			int firstPosition = 0;
			int position = 0;
			for (TalentScore score : scores) {
				if (i == 0) {
					firstPosition = score.getPosition();
					position = firstPosition;
				}
				i++;
				position++;

				if (accountId.equals(score.getAccountId())) {
					talentScoreDao.modifyPosition(accountId, firstPosition, timestamp);
					break;
				} else {
					talentScoreDao.modifyPosition(score.getAccountId(), position, timestamp);
				}
			}
		} else { // not exist
			talentScoreDao.add(accountId, timestamp);
			talentScoreDao.modifyVotedNum(accountId, 1, timestamp);

			int maxPosition = talentScoreDao.queryMaxPosition();
			talentScoreDao.modifyPosition(accountId, ++maxPosition, timestamp);
		}
	}

	@Override
	public boolean isVote(String accountId, String voteAccountId)
	{
		return talentVoteDao.isExist(accountId, voteAccountId);
	}

	@Override
	public TalentScore query(String accountId)
	{
		return talentScoreDao.select(accountId);
	}

	@Override
	public List<TalentInfo> query(int pageNum, int pageSize)
	{
		Page<TalentInfo> pages = talentScoreDao.select(pageNum, pageSize);
		List<TalentInfo> array = pages.getPageItems();
		return array;
	}
	
	@Override
	public List<TalentInfo> queryVote(String accountId, int pageNum, int pageSize)
	{
		Page<TalentInfo> pages = talentVoteDao.selectVote(accountId, pageNum, pageSize);
		List<TalentInfo> array = pages.getPageItems();
		return array;
	}

	private GaTalentInfoDao talentInfoDao;
	private GaTalentVoteDao talentVoteDao;
	private GaTalentScoreDao talentScoreDao;

	public GaTalentVoteDao getTalentVoteDao()
	{
		return talentVoteDao;
	}

	public void setTalentVoteDao(GaTalentVoteDao talentVoteDao)
	{
		this.talentVoteDao = talentVoteDao;
	}

	public GaTalentInfoDao getTalentInfoDao()
	{
		return talentInfoDao;
	}

	public void setTalentInfoDao(GaTalentInfoDao talentInfoDao)
	{
		this.talentInfoDao = talentInfoDao;
	}

	public GaTalentScoreDao getTalentScoreDao()
	{
		return talentScoreDao;
	}

	public void setTalentScoreDao(GaTalentScoreDao talentScoreDao)
	{
		this.talentScoreDao = talentScoreDao;
	}



}
