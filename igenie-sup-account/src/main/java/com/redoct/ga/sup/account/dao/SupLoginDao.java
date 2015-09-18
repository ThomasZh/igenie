package com.redoct.ga.sup.account.dao;

import java.util.List;

import com.oct.ga.comm.domain.account.LoginInfo;

public interface SupLoginDao
{
	public boolean isExist(short loginType, String loginName);

	public String queryAccountId(short loginType, String loginName);

	public String queryLoginName(String accountId, short loginType);

	public boolean isExist(short loginType, String loginName, String ecryptPwd);

	public String querySalt(short loginType, String loginName);

	public void updatePwd(short loginType, String loginName, String ecryptPwd, String salt, int timestamp);

	public void updateState(short loginType, String loginName, short state, int timestamp);

	public void add(String accountId, short loginType, String loginName, String ecryptPwd, String salt, int timestamp);

	public void add(String accountId, short loginType, String loginName, int timestamp);

	public void updateAccountId(short loginType, String loginName, String accountId, int timestamp);

	public List<LoginInfo> selectLoginList(String accountId);
}
