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
import com.oct.ga.comm.cmd.auth.PhoneRegisterLoginReq;
import com.oct.ga.comm.cmd.auth.PhoneRegisterLoginResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaFollowingService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.domain.VerificationCode;
import com.redoct.ga.sup.device.SupDeviceService;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.StpSession;

public class PhoneRegisterLoginAdapter
		extends StpReqCommand
{
	public PhoneRegisterLoginAdapter()
	{
		super();
		this.setTag(Command.PHONE_REGISTER_LOGIN_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new PhoneRegisterLoginReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String accountId = "";
		short loginType = GlobalArgs.ACCOUNT_LOGIN_BY_PHONE;
		String osVersion = reqCmd.getOsVersion();
		String gateToken = reqCmd.getGateToken();
		String deviceId = reqCmd.getDeviceId();
		String apnsToken = reqCmd.getApnsToken();
		String phone = reqCmd.getPhone();
		String md5pwd = reqCmd.getMd5pwd();
		String verificationCode = reqCmd.getVerificationCode();
		String lang = reqCmd.getLang();

		try {
			SupAccountService supAccountService = (SupAccountService) context.getBean("supAccountService");
			SupDeviceService supDeviceService = (SupDeviceService) context.getBean("supDeviceService");
			SupSessionService supSessionService = (SupSessionService) context.getBean("supSessionService");

			// Logic: Record login action to log.
			logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|loginName=[" + phone
					+ "]|commandTag=[" + this.getTag() + "]|notifyToken=[" + apnsToken + "]|osVersion=[" + osVersion
					+ "]");

			if (supSessionService.verifyGateToken(gateToken, deviceId)) {
				VerificationCode code = supAccountService.queryVerificationCode(
						GlobalArgs.VERIFICATION_TYPE_PHONE_REGISTER, deviceId);
				if (code != null) {
					if (!verificationCode.equals(code.getEkey())) {
						logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId
								+ "]|accountId=[]|commandTag=[" + this.getTag() + "]|ErrorCode=["
								+ ErrorCode.NOT_MATCH_VERIFICATION_CODE + "]|This verificationCode(" + verificationCode
								+ ") not match to (" + code.getEkey() + ")");

						PhoneRegisterLoginResp respCmd = new PhoneRegisterLoginResp(
								ErrorCode.NOT_MATCH_VERIFICATION_CODE, null, null);
						respCmd.setSequence(sequence);
						return respCmd;
					} else {
						if (code.getTtl() < this.getCurrentTimestamp()) {
							logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId
									+ "]|accountId=[]|commandTag=[" + this.getTag() + "]|ErrorCode=["
									+ ErrorCode.VERIFICATON_CODE_TIMEOUT + "]|This verificationCode("
									+ verificationCode + ") is time out.");

							PhoneRegisterLoginResp respCmd = new PhoneRegisterLoginResp(
									ErrorCode.VERIFICATON_CODE_TIMEOUT, null, null);
							respCmd.setSequence(sequence);
							return respCmd;
						}

						if (!phone.equals(code.getPhone())) {
							logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId
									+ "]|accountId=[]|commandTag=[" + this.getTag() + "]|ErrorCode=["
									+ ErrorCode.NOT_MATCH_PHONE + "]|This phone(" + phone + "not match to ("
									+ code.getPhone() + ")");

							PhoneRegisterLoginResp respCmd = new PhoneRegisterLoginResp(ErrorCode.NOT_MATCH_PHONE,
									null, null);
							respCmd.setSequence(sequence);
							return respCmd;
						}
					}
				}

				String nickname = null;
				String avatarUrl = null;
				String desc = null;

				// LOGIC: account is exist?
				if (supAccountService.verifyExist(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone)) {
					logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId
							+ "]|accountId=[]|commandTag=[" + this.getTag() + "]|This loginName(" + phone
							+ ") already exist!");

					AccountBasic account = supAccountService.queryAccount(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone);
					accountId = account.getAccountId();
					supAccountService.resetPwd(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone, md5pwd, currentTimestamp);
				} else { // not exist
					accountId = supAccountService.createAccount(nickname, avatarUrl, desc, currentTimestamp);
					supAccountService
							.createLogin(accountId, GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone, currentTimestamp);
					supAccountService.resetPwd(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone, md5pwd, currentTimestamp);

					logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=["
							+ accountId + "]|commandTag=[" + this.getTag() + "]|nickname=[" + nickname
							+ "]|osVersion=[" + osVersion + "]| register success)");
					
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
				}

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
				stpSession.setLoginName(phone);
				String sessionTicket = supSessionService.applySessionTicket(stpSession);
				session.setAttribute("sessionTicket", sessionTicket);
				logger.debug("sessionTicket: " + sessionTicket);

				PhoneRegisterLoginResp respCmd = new PhoneRegisterLoginResp(ErrorCode.SUCCESS, accountId, sessionTicket);
				respCmd.setSequence(sequence);
				return respCmd;
			} else {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[]|commandTag=["
						+ this.getTag() + "]|ErrorCode=[" + ErrorCode.GATE_TOKEN_NOT_EXIST + "]|GateToken=["
						+ gateToken + "] not exist]");

				PhoneRegisterLoginResp respCmd = new PhoneRegisterLoginResp(ErrorCode.GATE_TOKEN_NOT_EXIST, accountId,
						null);
				respCmd.setSequence(sequence);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[" + accountId
					+ "]|commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			PhoneRegisterLoginResp respCmd = new PhoneRegisterLoginResp(ErrorCode.UNKNOWN_FAILURE, null, null);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private PhoneRegisterLoginReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(PhoneRegisterLoginAdapter.class);

}
