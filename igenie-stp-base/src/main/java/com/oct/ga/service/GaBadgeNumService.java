package com.oct.ga.service;

import java.util.List;

import com.oct.ga.comm.domain.AccountBadgeNumJsonBean;
import com.oct.ga.comm.domain.account.LoginInfo;

public interface GaBadgeNumService
{
	public AccountBadgeNumJsonBean query(String accountId);

	public short queryMessageNum(String accountId);

	public short queryTaskLogNum(String accountId);

	public short queryInviteNum(String accountId);

	public short queryApplyNum(String accountId);

	public short queryMomentLogNum(String accountId);

	public void modifyMessageNum(String accountId, short num);

	public void modifyTaskLogNum(String accountId, short num);

	public void modifyInviteNum(String accountId, short num);

	public void modifyApplyNum(String accountId, short num);

	public void modifyMomentLogNum(String accountId, short num);

	public short countBadgeNum(String accountId);

	public short countMessageNum(String accountId);

	public short countTaskLogNum(String accountId);

	public short countApplyNum(String accountId);

	public short countInviteNum(String accountId, List<LoginInfo> logins);

	public short countMomentLogNum(String accountId);

}
