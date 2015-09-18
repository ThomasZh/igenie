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
import com.oct.ga.comm.cmd.auth.DeviceRegisterLoginReq;
import com.oct.ga.comm.cmd.auth.DeviceRegisterLoginResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.device.SupDeviceService;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.StpSession;

public class DeviceRegisterLoginAdapter
		extends StpReqCommand
{
	public DeviceRegisterLoginAdapter()
	{
		super();
		this.setTag(Command.DEVICE_REGISTER_LOGIN_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new DeviceRegisterLoginReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String accountId = "";
		String osVersion = reqCmd.getOsVersion();
		String gateToken = reqCmd.getGateToken();
		String deviceId = reqCmd.getDeviceId();
		String apnsToken = reqCmd.getApnsToken();
		String lang = reqCmd.getLang();

		try {
			SupAccountService supAccountService = (SupAccountService) context.getBean("supAccountService");
			SupDeviceService supDeviceService = (SupDeviceService) context.getBean("supDeviceService");
			SupSessionService supSessionService = (SupSessionService) context.getBean("supSessionService");

			// Logic: Record login action to log.
			logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|loginName=[]|commandTag=["
					+ this.getTag() + "]|notifyToken=[" + apnsToken + "]|osVersion=[" + osVersion + "]");

			if (supSessionService.verifyGateToken(gateToken, deviceId)) {
				String nickname = null;
				String avatarUrl = null;
				String desc = null;

				if (supAccountService.verifyExist(GlobalArgs.ACCOUNT_LOGIN_BY_DEVICE, deviceId)) {
					logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId
							+ "]|accountId=[]|commandTag=[" + this.getTag() + "]|ErrorCode=["
							+ ErrorCode.REGISTER_DEVICE_EXIST + "]|This loginName(" + deviceId + ") already exist!");

					AccountBasic account = supAccountService.queryAccount(GlobalArgs.ACCOUNT_LOGIN_BY_DEVICE, deviceId);
					accountId = account.getAccountId();
				} else { // not exist
					accountId = supAccountService.createAccount(nickname, avatarUrl, desc, currentTimestamp);
					supAccountService.createLogin(accountId, GlobalArgs.ACCOUNT_LOGIN_BY_DEVICE, deviceId,
							currentTimestamp);

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
				}

				if (osVersion.contains("android")) {
					apnsToken = EcryptUtil.md5ChatId(accountId, deviceId);
				}
				supDeviceService.modifyOsVersion(deviceId, osVersion, apnsToken, currentTimestamp);

				session.setAttribute("deviceId", deviceId);
				session.setAttribute("accountId", accountId);
				session.setAttribute("accountName", nickname);

				StpSession stpSession = new StpSession();
				stpSession.setAccountId(accountId);
				stpSession.setActive(true);
				stpSession.setDeviceId(deviceId);
				stpSession.setGateToken(gateToken);
				stpSession.setNotifyToken(apnsToken);
				stpSession.setDeviceOsVersion(osVersion);
				stpSession.setIoSessionId(session.getId());
				String sessionTicket = supSessionService.applySessionTicket(stpSession);

				DeviceRegisterLoginResp respCmd = new DeviceRegisterLoginResp(ErrorCode.SUCCESS, accountId,
						sessionTicket);
				respCmd.setSequence(sequence);
				return respCmd;
			} else {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[]|commandTag=["
						+ this.getTag() + "]|ErrorCode=[" + ErrorCode.GATE_TOKEN_NOT_EXIST + "]|GateToken=["
						+ gateToken + "] not exist]");

				DeviceRegisterLoginResp respCmd = new DeviceRegisterLoginResp(ErrorCode.GATE_TOKEN_NOT_EXIST,
						accountId, null);
				respCmd.setSequence(sequence);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[" + accountId
					+ "]|commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			DeviceRegisterLoginResp respCmd = new DeviceRegisterLoginResp(ErrorCode.UNKNOWN_FAILURE, null, null);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private DeviceRegisterLoginReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(DeviceRegisterLoginAdapter.class);

}
