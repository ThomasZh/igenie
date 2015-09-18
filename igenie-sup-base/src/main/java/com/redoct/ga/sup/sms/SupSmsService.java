package com.redoct.ga.sup.sms;

import com.oct.ga.comm.SupSocketException;

public interface SupSmsService
{
	public void sendVerificationCode(String phone, String ekey, String lang)
			throws SupSocketException;
}
