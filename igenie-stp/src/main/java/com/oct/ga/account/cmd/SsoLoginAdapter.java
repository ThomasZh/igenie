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
import com.oct.ga.comm.cmd.auth.SsoLoginReq;
import com.oct.ga.comm.cmd.auth.SsoLoginResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.club.ClubMasterInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaClubService;
import com.oct.ga.service.GaFollowingService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.device.SupDeviceService;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.StpSession;

public class SsoLoginAdapter
		extends StpReqCommand
{
	public SsoLoginAdapter()
	{
		super();
		this.setTag(Command.SSO_LOGIN_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SsoLoginReq().decode(tlv);
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
		String nickname = reqCmd.getNickname();
		String desc = reqCmd.getDesc();
		short loginType = reqCmd.getLoginType();
		String loginName = reqCmd.getLoginName();
		String avatarUrl = reqCmd.getImageUrl();
		String apnsToken = reqCmd.getApnsToken();

		try {
			SupAccountService supAccountService = (SupAccountService) context.getBean("supAccountService");
			SupDeviceService supDeviceService = (SupDeviceService) context.getBean("supDeviceService");
			SupSessionService supSessionService = (SupSessionService) context.getBean("supSessionService");

			// Logic: Record login action to log.
			logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|loginName=[" + loginName
					+ "]|commandTag=[" + this.getTag() + "]|notifyToken=[" + apnsToken + "]|osVersion=[" + osVersion
					+ "]");

			if (supSessionService.verifyGateToken(gateToken, deviceId)) {
				// LOGIC: account is exist?
				if (supAccountService.verifyExist(loginType, loginName)) {
					logger.info("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId
							+ "]|accountId=[]|commandTag=[" + this.getTag() + "]|ErrorCode=["
							+ ErrorCode.REGISTER_EMAIL_EXIST + "]|This loginName(" + loginName + ") already exist!");

					AccountBasic account = supAccountService.queryAccount(loginType, loginName);
					accountId = account.getAccountId();

					account.setNickname(nickname);
					account.setAvatarUrl(avatarUrl);
					account.setDesc(desc);
					supAccountService.modifyAccountBasicInfo(account, currentTimestamp);
				} else { // not exist
					accountId = supAccountService.createAccount(nickname, avatarUrl, desc, currentTimestamp);
					supAccountService.createLogin(accountId, loginType, loginName,
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
				stpSession.setLoginName(loginName);
				String sessionTicket = supSessionService.applySessionTicket(stpSession);
				session.setAttribute("sessionTicket", sessionTicket);
				logger.debug("sessionTicket: " + sessionTicket);

				SsoLoginResp respCmd = new SsoLoginResp(ErrorCode.SUCCESS, accountId, sessionTicket);
				respCmd.setSequence(sequence);
				return respCmd;
			} else {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[]|commandTag=["
						+ this.getTag() + "]|ErrorCode=[" + ErrorCode.GATE_TOKEN_NOT_EXIST + "]|GateToken=["
						+ gateToken + "] not exist]");

				SsoLoginResp respCmd = new SsoLoginResp(ErrorCode.GATE_TOKEN_NOT_EXIST, null, null);
				respCmd.setSequence(sequence);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[" + accountId
					+ "]|commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			SsoLoginResp respCmd = new SsoLoginResp(ErrorCode.UNKNOWN_FAILURE, null, null);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private String createDefalutClub(ApplicationContext context, String clubName, String titleBkImageUrl)
	{
		GaClubService clubService = (GaClubService) context.getBean("clubClubService");
		GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
		GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");

		ClubMasterInfo club = new ClubMasterInfo();
		club.setName(clubName);
		club.setCreatorId(getMyAccountId());
		club.setTitleBkImage(titleBkImageUrl);

		String clubId = clubService.create(club, currentTimestamp);
		groupService.createGroup(clubId, club.getName(), GlobalArgs.CHANNEL_TYPE_CLUB, currentTimestamp,
				this.getMyAccountId());
		syncVerService.increase(clubId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
				this.getMyAccountId(), this.getTag());

		groupService.joinAsLeader(clubId, this.getMyAccountId(), currentTimestamp);
		syncVerService.increase(clubId, GlobalArgs.SYNC_VERSION_TYPE_TASK_MEMBER, currentTimestamp,
				this.getMyAccountId(), this.getTag());

		// Add myself as a member
		// -1 as owner create time is at first list
		clubService.addSubscriber(clubId, this.getMyAccountId(), GlobalArgs.INVITE_STATE_ACCPET, currentTimestamp - 1);

		int num = clubService.querySubscriberNum(clubId);
		clubService.updateSubscriberNum(clubId, num, currentTimestamp);

		return clubId;
	}

	private SsoLoginReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SsoLoginAdapter.class);

}
