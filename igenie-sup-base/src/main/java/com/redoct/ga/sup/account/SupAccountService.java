package com.redoct.ga.sup.account;

import java.util.List;

import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.account.AccountDetail;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.redoct.ga.sup.account.domain.LostPwdEkey;
import com.redoct.ga.sup.account.domain.VerificationCode;

public interface SupAccountService
{
	public boolean verifyExist(short loginType, String loginName)
			throws SupSocketException;

	public String verifyLogin(short loginType, String loginName, String md5pwd)
			throws SupSocketException;

	public void resetPwd(short loginType, String loginName, String md5pwd, int timestamp)
			throws SupSocketException;

	public String createAccount(String nickname, String avatarUrl, String desc, int timestamp)
			throws SupSocketException;

	public void modifyAccountBasicInfo(AccountBasic account, int timestamp)
			throws SupSocketException;

	public void createLogin(String accountId, short loginType, String loginName, int timestamp)
			throws SupSocketException;

	public void modifyAccountId4Login(String accountId, short loginType, String loginName, int timestamp)
			throws SupSocketException;

	public String createEkey(short loginType, String loginName, int timestamp)
			throws SupSocketException;

	public LostPwdEkey queryEkey(String ekey)
			throws SupSocketException;

	public AccountBasic queryAccount(String accountId)
			throws SupSocketException;

	public AccountBasic queryAccount(short loginType, String loginName)
			throws SupSocketException;

	public AccountMaster queryAccountMaster(String accountId)
			throws SupSocketException;

	public List<AccountDetail> queryAccountDetails(List<String> ids)
			throws SupSocketException;

	public String queryLoginName(String accountId, short loginType)
			throws SupSocketException;

	public String applyVerificationCode(short verificationType, String deviceId, String phone, int timestamp)
			throws SupSocketException;

	public VerificationCode queryVerificationCode(short verificationType, String deviceId)
			throws SupSocketException;

	// ///////////////////////////////////////////////////////////
	// administrator

	public List<AccountBasic> queryAllAccountBasic()
			throws SupSocketException;

}
