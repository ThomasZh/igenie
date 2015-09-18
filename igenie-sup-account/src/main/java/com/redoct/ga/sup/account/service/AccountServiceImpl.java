package com.redoct.ga.sup.account.service;

import java.util.List;
import java.util.UUID;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.EcryptUtil;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.StringUtil;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.account.AccountDetail;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.dao.SupAccountDao;
import com.redoct.ga.sup.account.dao.SupEkeyDao;
import com.redoct.ga.sup.account.dao.SupLoginDao;
import com.redoct.ga.sup.account.domain.LostPwdEkey;
import com.redoct.ga.sup.account.domain.VerificationCode;

public class AccountServiceImpl
		implements SupAccountService
{
	@Override
	public boolean verifyExist(short loginType, String loginName)
	{
		if (loginType == GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL)
			loginName = loginName.toLowerCase();
		return loginDao.isExist(loginType, loginName);
	}

	@Override
	public String verifyLogin(short loginType, String loginName, String md5Pwd)
	{
		if (loginType == GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL)
			loginName = loginName.toLowerCase();
		String ecryptedPwd = null;

		String salt = loginDao.querySalt(loginType, loginName);
		if (salt != null && salt.length() > 0) {
			ecryptedPwd = EcryptUtil.md5(md5Pwd + EcryptUtil.md5(salt));
		} else {
			ecryptedPwd = md5Pwd;
		}

		if (loginDao.isExist(loginType, loginName, ecryptedPwd)) {
			return loginDao.queryAccountId(loginType, loginName);
		} else
			return null;
	}

	@Override
	public void resetPwd(short loginType, String loginName, String md5pwd, int timestamp)
	{
		if (loginType == GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL)
			loginName = loginName.toLowerCase();
		String salt = EcryptUtil.salt();
		String ecryptPwd = EcryptUtil.md5(md5pwd + EcryptUtil.md5(salt));

		loginDao.updatePwd(loginType, loginName, ecryptPwd, salt, timestamp);
	}

	@Override
	public String createAccount(String nickname, String avatarUrl, String desc, int timestamp)
	{
		String accountId = UUID.randomUUID().toString();
		accountDao.add(accountId, nickname, avatarUrl, desc, timestamp);

		return accountId;
	}

	@Override
	public void modifyAccountBasicInfo(AccountBasic account, int timestamp)
	{
		if (account.getNickname() != null && account.getNickname().length() > 0)
			accountDao.updateNickname(account.getAccountId(), account.getNickname(), timestamp);
		if (account.getDesc() != null && account.getDesc().length() > 0)
			accountDao.updateDesc(account.getAccountId(), account.getDesc(), timestamp);
		if (account.getAvatarUrl() != null && account.getAvatarUrl().length() > 0)
			accountDao.updateAvatarUrl(account.getAccountId(), account.getAvatarUrl(), timestamp);
	}

	@Override
	public AccountBasic queryAccount(String accountId)
	{
		return accountDao.queryBasic(accountId);
	}

	@Override
	public AccountBasic queryAccount(short loginType, String loginName)
	{
		if (loginType == GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL)
			loginName = loginName.toLowerCase();
		String accountId = loginDao.queryAccountId(loginType, loginName);
		return accountDao.queryBasic(accountId);
	}

	@Override
	public AccountMaster queryAccountMaster(String accountId)
			throws SupSocketException
	{
		try {
			AccountMaster account = accountDao.queryMaster(accountId);

			String email = loginDao.queryLoginName(accountId, GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL);
			account.setEmail(email);
			String phone = loginDao.queryLoginName(accountId, GlobalArgs.ACCOUNT_LOGIN_BY_PHONE);
			account.setPhone(phone);

			return account;
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public String queryLoginName(String accountId, short loginType)
			throws SupSocketException
	{
		try {
			return loginDao.queryLoginName(accountId, loginType);
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public void createLogin(String accountId, short loginType, String loginName, int timestamp)
	{
		if (loginType == GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL)
			loginName = loginName.toLowerCase();
		loginDao.add(accountId, loginType, loginName, timestamp);
	}

	@Override
	public void modifyAccountId4Login(String accountId, short loginType, String loginName, int timestamp)
			throws SupSocketException
	{
		try {
			loginDao.updateAccountId(loginType, loginName, accountId, timestamp);
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public String createEkey(short loginType, String loginName, int timestamp)
	{
		if (loginType == GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL)
			loginName = loginName.toLowerCase();
		String accountId = loginDao.queryAccountId(loginType, loginName);

		String ekey = UUID.randomUUID().toString();
		ekey = EcryptUtil.md5(ekey);

		/* ttl is 24 hours */
		ekeyDao.addEkey(ekey, accountId, loginType, loginName, timestamp + 86400);

		return ekey;
	}

	@Override
	public LostPwdEkey queryEkey(String ekey)
	{
		return ekeyDao.query(ekey);
	}

	@Override
	public String applyVerificationCode(short type, String deviceId, String phone, int timestamp)
			throws SupSocketException
	{
		try {
			int ttl = timestamp + 300; // 5 mins
			String ekey = StringUtil.random6num();

			if (ekeyDao.isExist(type, deviceId)) {
				VerificationCode code = ekeyDao.query(type, deviceId);
				int currentTimestamp = DatetimeUtil.currentTimestamp();

				if (code.getTtl() > currentTimestamp) {
					return null; // not need send it again
				} else {
					int count = code.getCount();

					if (count > 5) { // limit count
						if (code.getTtl() + 86400 < currentTimestamp) { // reactive
							ekeyDao.modifyVerificatonCode(type, deviceId, phone, ekey, ttl, timestamp, 1);
						} else {
							return null;
						}
					} else {
						ekeyDao.modifyVerificatonCode(type, deviceId, phone, ekey, ttl, timestamp, ++count);
					}
				}
			} else {
				ekeyDao.addVerificatonCode(type, deviceId, phone, ekey, ttl, timestamp);
			}

			return ekey;
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public VerificationCode queryVerificationCode(short verificationType, String deviceId)
			throws SupSocketException
	{
		return ekeyDao.query(verificationType, deviceId);
	}

	@Override
	public List<AccountBasic> queryAllAccountBasic()
			throws SupSocketException
	{
		try {
			return accountDao.queryAllBasic();
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public List<AccountDetail> queryAccountDetails(List<String> ids)
			throws SupSocketException
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// //////////////////////////////////////////////////////////

	private SupAccountDao accountDao;
	private SupLoginDao loginDao;
	private SupEkeyDao ekeyDao;

	public SupAccountDao getAccountDao()
	{
		return accountDao;
	}

	public void setAccountDao(SupAccountDao accountDao)
	{
		this.accountDao = accountDao;
	}

	public SupLoginDao getLoginDao()
	{
		return loginDao;
	}

	public void setLoginDao(SupLoginDao loginDao)
	{
		this.loginDao = loginDao;
	}

	public SupEkeyDao getEkeyDao()
	{
		return ekeyDao;
	}

	public void setEkeyDao(SupEkeyDao ekeyDao)
	{
		this.ekeyDao = ekeyDao;
	}



}
