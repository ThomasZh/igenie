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
import com.oct.ga.comm.cmd.auth.RegisterLoginReq;
import com.oct.ga.comm.cmd.auth.RegisterLoginResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaFollowingService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.device.SupDeviceService;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.StpSession;

public class RegisterLoginAdapter
		extends StpReqCommand
{
	public RegisterLoginAdapter()
	{
		super();
		this.setTag(Command.REGISTER_LOGIN_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new RegisterLoginReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String accountId = "";
		short loginType = GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL;
		String osVersion = reqCmd.getOsVersion();
		String gateToken = reqCmd.getGateToken();
		String deviceId = reqCmd.getDeviceId();
		String nickname = reqCmd.getFirstName();
		String loginName = reqCmd.getEmail();
		String md5pwd = reqCmd.getMd5pwd();
		String avatarUrl = reqCmd.getFacePhoto();
		String apnsToken = reqCmd.getApnsToken();
		String desc = null;

		if (loginName.contains("@")) {
			loginType = GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL;
			logger.debug("loginType: ACCOUNT_LOGIN_BY_EMAIL");

			if (nickname == null || nickname.length() == 0) {
				nickname = loginName.substring(0, loginName.indexOf("@"));
			}
		} else {
			loginType = GlobalArgs.ACCOUNT_LOGIN_BY_PHONE;
			logger.debug("loginType: ACCOUNT_LOGIN_BY_PHONE");
			
			if (nickname == null || nickname.length() == 0) {
				nickname = loginName;
			}
		}
		logger.debug("nickname: " + nickname);

		try {
			SupAccountService supAccountService = (SupAccountService) context.getBean("supAccountService");
			SupDeviceService supDeviceService = (SupDeviceService) context.getBean("supDeviceService");
			SupSessionService supSessionService = (SupSessionService) context.getBean("supSessionService");

			// Logic: Record login action to log.
			logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|loginName=[" + loginName
					+ "]|commandTag=[" + this.getTag() + "]|notifyToken=[" + apnsToken + "]|osVersion=[" + osVersion
					+ "]");

			if (supSessionService.verifyGateToken(gateToken, deviceId)) {
				if (supAccountService.verifyExist(loginType, loginName)) {
					logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId
							+ "]|accountId=[]|commandTag=[" + this.getTag() + "]|ErrorCode=["
							+ ErrorCode.REGISTER_EMAIL_EXIST + "]|This loginName(" + loginName + ") already exist!");

					RegisterLoginResp respCmd = new RegisterLoginResp(ErrorCode.REGISTER_EMAIL_EXIST, null, null);
					respCmd.setSequence(sequence);
					return respCmd;
				} else { // not exist
					accountId = supAccountService.createAccount(nickname, avatarUrl, desc, currentTimestamp);
					supAccountService.createLogin(accountId, loginType, loginName, currentTimestamp);
					supAccountService.resetPwd(loginType, loginName, md5pwd, currentTimestamp);

					logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=["
							+ accountId + "]|commandTag=[" + this.getTag() + "]|nickname=[" + nickname
							+ "]|osVersion=[" + osVersion + "]| register success)");

					if (osVersion.contains("android")) {
						apnsToken = EcryptUtil.md5ChatId(accountId, deviceId);
					}
					supDeviceService.modifyOsVersion(deviceId, osVersion, apnsToken, currentTimestamp);

					session.setAttribute("deviceId", deviceId);
					session.setAttribute("accountId", accountId);
					session.setAttribute("accountName", nickname);
					session.setAttribute("avatarUrl", avatarUrl);

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

					try {
						GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
						activityService.createExerciseTask1(context, accountId, currentTimestamp);
						activityService.createExerciseTask2(context, accountId, currentTimestamp);
						activityService.createExerciseTask3(context, accountId, currentTimestamp);
					} catch (Exception e) {
						logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=["
								+ accountId + "]|create exercise project error: " + LogErrorMessage.getFullInfo(e));
					}

					try {
						GaFollowingService followingService = (GaFollowingService) context
								.getBean("gaFollowingService");

						// Logic: follow support service.
						followingService.follow(GlobalArgs.SUPPORT_SERVICE_ACCOUNT_ID, accountId, currentTimestamp);
						followingService.follow(accountId, GlobalArgs.SUPPORT_SERVICE_ACCOUNT_ID, currentTimestamp);
					} catch (Exception e) {
						logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=["
								+ accountId + "]|follow support account error: " + LogErrorMessage.getFullInfo(e));
					}

					RegisterLoginResp respCmd = new RegisterLoginResp(ErrorCode.SUCCESS, accountId, sessionTicket);
					respCmd.setSequence(sequence);
					return respCmd;
				}
			} else {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[]|commandTag=["
						+ this.getTag() + "]|ErrorCode=[" + ErrorCode.GATE_TOKEN_NOT_EXIST + "]|GateToken=["
						+ gateToken + "] not exist]");

				RegisterLoginResp respCmd = new RegisterLoginResp(ErrorCode.GATE_TOKEN_NOT_EXIST, accountId, null);
				respCmd.setSequence(sequence);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[" + accountId
					+ "]|commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			RegisterLoginResp respCmd = new RegisterLoginResp(ErrorCode.UNKNOWN_FAILURE, null, null);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private RegisterLoginReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(RegisterLoginAdapter.class);

}
