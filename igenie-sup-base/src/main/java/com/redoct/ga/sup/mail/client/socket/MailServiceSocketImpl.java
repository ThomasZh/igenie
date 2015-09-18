package com.redoct.ga.sup.mail.client.socket;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.domain.gatekeeper.SupServerInfo;
import com.redoct.ga.sup.client.socket.SupSocketClient;
import com.redoct.ga.sup.client.socket.SupSocketConnectionManager;
import com.redoct.ga.sup.mail.SupMailService;
import com.redoct.ga.sup.mail.cmd.SendForgotPwdEmailReq;
import com.redoct.ga.sup.mail.cmd.SendForgotPwdEmailResp;
import com.redoct.ga.sup.mail.cmd.SendFriendInviteEmailReq;
import com.redoct.ga.sup.mail.cmd.SendFriendInviteEmailResp;
import com.redoct.ga.sup.mail.cmd.SendHtmlEmailReq;
import com.redoct.ga.sup.mail.cmd.SendHtmlEmailResp;

public class MailServiceSocketImpl
		implements SupMailService
{
	@Override
	public void sendHtml(String fromEmail, String fromName, String toEmail, String toName, String subject, String html)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_MAIL_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				SendHtmlEmailReq reqCmd = new SendHtmlEmailReq(fromEmail, fromName, toEmail, toName, subject, html);

				SendHtmlEmailResp respCmd = (SendHtmlEmailResp) socketClient.send(addr, reqCmd);
				if (respCmd != null)
					logger.debug("response state: " + respCmd.getRespState());
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public void sendForgotPwd(String toEmail, String toName, String ekey)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_MAIL_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				SendForgotPwdEmailReq reqCmd = new SendForgotPwdEmailReq(toEmail, toName, ekey);

				SendForgotPwdEmailResp respCmd = (SendForgotPwdEmailResp) socketClient.send(addr, reqCmd);
				if (respCmd != null)
					logger.debug("response state: " + respCmd.getRespState());

				return;
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public void sendFriendInvite(String fromEmail, String fromName, String toEmail, String toName, String ekey)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_MAIL_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				SendFriendInviteEmailReq reqCmd = new SendFriendInviteEmailReq(fromEmail, fromName, toEmail, toName,
						ekey);

				SendFriendInviteEmailResp respCmd = (SendFriendInviteEmailResp) socketClient.send(addr, reqCmd);
				if (respCmd != null)
					logger.debug("response state: " + respCmd.getRespState());
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	// /////////////////////////////////////////////////////

	private SupSocketClient socketClient;

	public SupSocketClient getSocketClient()
	{
		return socketClient;
	}

	public void setSocketClient(SupSocketClient socketClient)
	{
		this.socketClient = socketClient;
	}

	private final static Logger logger = LoggerFactory.getLogger(MailServiceSocketImpl.class);

}
