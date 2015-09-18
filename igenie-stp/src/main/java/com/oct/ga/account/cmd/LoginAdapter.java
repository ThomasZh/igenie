package com.oct.ga.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.EcryptUtil;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.auth.LoginReq;
import com.oct.ga.comm.cmd.auth.LoginResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.device.SupDeviceService;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.StpSession;

public class LoginAdapter
		extends StpReqCommand
{
	public LoginAdapter()
	{
		super();

		this.setTag(Command.LOGIN_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new LoginReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		short loginType = GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL;
		String gateToken = reqCmd.getGateToken();
		String deviceId = reqCmd.getMyDeviceId();
		String loginName = reqCmd.getEmail();
		String md5pwd = reqCmd.getPassword();
		String apnsToken = reqCmd.getApnsToken();
		String osVersion = reqCmd.getOsVersion();

		String accountId = null;

		logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|loginName=[" + loginName
				+ "]|commandTag=[" + this.getTag() + "]|osVersion=[" + osVersion + "]|gateToken=[" + gateToken
				+ "]| try to login");

		if (loginName.contains("@")) {
			loginType = GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL;
			logger.debug("loginType: ACCOUNT_LOGIN_BY_EMAIL");
		} else {
			loginType = GlobalArgs.ACCOUNT_LOGIN_BY_PHONE;
			logger.debug("loginType: ACCOUNT_LOGIN_BY_PHONE");
		}

		try {
			SupAccountService supAccountService = (SupAccountService) context.getBean("supAccountService");
			SupDeviceService supDeviceService = (SupDeviceService) context.getBean("supDeviceService");
			SupSessionService supSessionService = (SupSessionService) context.getBean("supSessionService");

			if (supSessionService.verifyGateToken(gateToken, deviceId)) {
				// LOGIC: account is exist & loginName,password are correct
				accountId = supAccountService.verifyLogin(loginType, loginName, md5pwd);
				logger.debug("accountId: " + accountId);
				if (accountId != null) {
					if (osVersion.contains("android")) {
						apnsToken = EcryptUtil.md5ChatId(accountId, deviceId);
					}
					session.setAttribute("notifyToken", apnsToken);
					supDeviceService.modifyOsVersion(deviceId, osVersion, apnsToken, currentTimestamp);

					AccountBasic account = supAccountService.queryAccount(accountId);
					session.setAttribute("deviceId", deviceId);
					session.setAttribute("accountId", accountId);
					session.setAttribute("accountName", account.getNickname());
					session.setAttribute("avatarUrl", account.getAvatarUrl());

					StpSession stpSession = new StpSession();
					stpSession.setAccountId(accountId);
					stpSession.setActive(true);
					stpSession.setDeviceId(deviceId);
					stpSession.setGateToken(gateToken);
					stpSession.setNotifyToken(apnsToken);
					stpSession.setDeviceOsVersion(osVersion);
					stpSession.setIoSessionId(session.getId());
					stpSession.setLoginType(loginType);
					stpSession.setLoginName(loginName);
					String sessionTicket = supSessionService.applySessionTicket(stpSession);
					session.setAttribute("sessionTicket", sessionTicket);
					logger.debug("sessionTicket: " + sessionTicket);

					logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|loginName=["
							+ loginName + "]|commandTag=[" + this.getTag() + "]|accountId=[" + accountId
							+ "]|nickname=[" + account.getNickname() + "]|osVersion=[" + osVersion + "]|notifyToken=["
							+ apnsToken + "]|sessionTicket=[" + sessionTicket + "]| login success");

					LoginResp respCmd = new LoginResp(ErrorCode.SUCCESS, accountId, sessionTicket);
					respCmd.setSequence(sequence);
					return respCmd;
				} else {
					logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|loginName=["
							+ loginName + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
							+ ErrorCode.LOGIN_WRONG_PWD + "]|loginName and password pair are wrong");

					LoginResp respCmd = new LoginResp(ErrorCode.LOGIN_WRONG_PWD, null, null);
					respCmd.setSequence(sequence);
					return respCmd;
				}
			} else {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|loginName=[" + loginName
						+ "]|commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.GATE_TOKEN_NOT_EXIST
						+ "]|gateToken=[" + gateToken + "] not exist");

				LoginResp respCmd = new LoginResp(ErrorCode.GATE_TOKEN_NOT_EXIST, null, null);
				respCmd.setSequence(sequence);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|loginName=[" + loginName
					+ "]|commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			LoginResp respCmd = new LoginResp(ErrorCode.UNKNOWN_FAILURE, accountId, null);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private LoginReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(LoginAdapter.class);

}
