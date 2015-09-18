package com.redoct.ga.sup.account.dao;

import java.util.List;

import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.account.AccountMaster;

public interface SupAccountDao
{
	public void add(String accountId, String nickName, String avatarUrl, String desc, int timestamp);

	public void updateState(String accountId, short state, int timestamp);

	public void updateNickname(String accountId, String nickname, int timestamp);

	public void updateDesc(String accountId, String desc, int timestamp);

	public void updateAvatarUrl(String accountId, String avatarUrl, int timestamp);

	public AccountBasic queryBasic(String accountId);

	public List<AccountBasic> queryAllBasic();

	public AccountMaster queryMaster(String accountId);

}
