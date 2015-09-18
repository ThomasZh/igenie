package com.oct.ga.badgenum.dao;

import com.oct.ga.comm.domain.AccountBadgeNumJsonBean;

public interface GaBadgeNumDao
{
	public AccountBadgeNumJsonBean select(String accountId);

	public boolean isExist(String accountId);

	public void add(String accountId);

	public short selectMessageNum(String accountId);

	public short selectTaskLogNum(String accountId);

	public short selectInviteNum(String accountId);

	public short selectApplyNum(String accountId);

	public void updateMessageNum(String accountId, short num);

	public void updateTaskLogNum(String accountId, short num);

	public void updateInviteNum(String accountId, short num);

	public void updateApplyNum(String accountId, short num);

	public short countMessageNum(String accountId);

	public short countTaskLogNum(String accountId);

	public short countInviteNum(String accountId);

	public short countInviteNum(short loginType, String loginName);

	public short countApplyNum(String accountId);

	public short countInviteFeedbackNum(String accountId);

	public short selectMomentLogNum(String accountId);

	public void updateMomentLogNum(String accountId, short num);

	public short countMomentLogNum(String accountId);

}
