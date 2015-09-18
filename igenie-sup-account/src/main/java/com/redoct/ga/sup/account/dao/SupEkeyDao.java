package com.redoct.ga.sup.account.dao;

import com.redoct.ga.sup.account.domain.LostPwdEkey;
import com.redoct.ga.sup.account.domain.VerificationCode;

public interface SupEkeyDao
{
	public void addEkey(String ekey, String accountId, short loginType, String loginName, int ttl);

	public LostPwdEkey query(String ekey);

	public void remove(String ekey);

	public void addVerificatonCode(short type, String deviceId, String phone, String ekey, int ttl, int timestamp);

	public void modifyVerificatonCode(short type, String deviceId, String phone, String ekey, int ttl, int timestamp,
			int count);

	public boolean isExist(short type, String deviceId);

	public VerificationCode query(short type, String deviceId);

	public void remove(short type, String deviceId);
}
