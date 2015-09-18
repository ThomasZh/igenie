package com.redoct.ga.sup.mail;

import com.oct.ga.comm.SupSocketException;

public interface SupMailService
{
	public void sendHtml(String fromEmail, String fromName, String toEmail, String toName, String subject, String html)
			throws SupSocketException;

	public void sendForgotPwd(String toEmail, String toName, String ekey)
			throws SupSocketException;

	public void sendFriendInvite(String fromEmail, String fromName, String toEmail, String toName, String ekey)
			throws SupSocketException;
}
