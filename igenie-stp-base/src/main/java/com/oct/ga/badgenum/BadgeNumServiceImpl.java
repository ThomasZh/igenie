package com.oct.ga.badgenum;

import java.util.List;

import com.oct.ga.badgenum.dao.GaBadgeNumDao;
import com.oct.ga.comm.domain.AccountBadgeNumJsonBean;
import com.oct.ga.comm.domain.account.LoginInfo;
import com.oct.ga.service.GaBadgeNumService;

public class BadgeNumServiceImpl
		implements GaBadgeNumService
{
	@Override
	public AccountBadgeNumJsonBean query(String accountId)
	{
		return badgeNumDao.select(accountId);
	}

	@Override
	public short countBadgeNum(String accountId)
	{
		AccountBadgeNumJsonBean badgeNum = badgeNumDao.select(accountId);
		short num = (short) (badgeNum.getMessageNum() + badgeNum.getInviteNum() + badgeNum.getApplyNum() + badgeNum
				.getMomentLogNum());
		return num;
	}

	@Override
	public short queryMessageNum(String accountId)
	{
		return badgeNumDao.selectMessageNum(accountId);
	}

	@Override
	public short queryTaskLogNum(String accountId)
	{
		return badgeNumDao.selectTaskLogNum(accountId);
	}

	@Override
	public short queryInviteNum(String accountId)
	{
		return badgeNumDao.selectInviteNum(accountId);
	}

	@Override
	public void modifyMessageNum(String accountId, short num)
	{
		if (num < 0)
			num = 0;

		if (!badgeNumDao.isExist(accountId))
			badgeNumDao.add(accountId);

		badgeNumDao.updateMessageNum(accountId, num);
	}

	@Override
	public void modifyTaskLogNum(String accountId, short num)
	{
		if (num < 0)
			num = 0;

		if (!badgeNumDao.isExist(accountId))
			badgeNumDao.add(accountId);

		badgeNumDao.updateTaskLogNum(accountId, num);
	}

	@Override
	public void modifyInviteNum(String accountId, short num)
	{
		if (num < 0)
			num = 0;

		if (!badgeNumDao.isExist(accountId))
			badgeNumDao.add(accountId);

		badgeNumDao.updateInviteNum(accountId, num);
	}

	@Override
	public short countMessageNum(String accountId)
	{
		return badgeNumDao.countMessageNum(accountId);
	}

	@Override
	public short countTaskLogNum(String accountId)
	{
		return badgeNumDao.countTaskLogNum(accountId);
	}

	@Override
	public short queryApplyNum(String accountId)
	{
		return badgeNumDao.selectApplyNum(accountId);
	}

	@Override
	public void modifyApplyNum(String accountId, short num)
	{
		if (num < 0)
			num = 0;

		if (!badgeNumDao.isExist(accountId))
			badgeNumDao.add(accountId);

		badgeNumDao.updateApplyNum(accountId, num);
	}

	@Override
	public short countApplyNum(String accountId)
	{
		return badgeNumDao.countApplyNum(accountId);
	}

	@Override
	public short countInviteNum(String accountId, List<LoginInfo> logins)
	{
		short total = 0;
		short inviteNumByAccountId = badgeNumDao.countInviteNum(accountId);
		total += inviteNumByAccountId;
		short inviteFeedbackNumByAccountId = badgeNumDao.countInviteFeedbackNum(accountId);
		total += inviteFeedbackNumByAccountId;

		for (LoginInfo login : logins) {
			short inviteNumByLoginName = badgeNumDao.countInviteNum(login.getLoginType(), login.getLoginName());
			total += inviteNumByLoginName;
		}
		return total;
	}

	@Override
	public short queryMomentLogNum(String accountId)
	{
		return badgeNumDao.selectMomentLogNum(accountId);
	}

	@Override
	public void modifyMomentLogNum(String accountId, short num)
	{
		if (num < 0)
			num = 0;

		if (!badgeNumDao.isExist(accountId))
			badgeNumDao.add(accountId);

		badgeNumDao.updateMomentLogNum(accountId, num);
	}

	@Override
	public short countMomentLogNum(String accountId)
	{
		return badgeNumDao.countMomentLogNum(accountId);
	}

	// ///////////////////////////////////////////////////////

	private GaBadgeNumDao badgeNumDao;

	public GaBadgeNumDao getBadgeNumDao()
	{
		return badgeNumDao;
	}

	public void setBadgeNumDao(GaBadgeNumDao badgeNumDao)
	{
		this.badgeNumDao = badgeNumDao;
	}

}
