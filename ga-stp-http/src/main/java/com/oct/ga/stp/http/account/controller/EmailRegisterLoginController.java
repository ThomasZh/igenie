package com.oct.ga.stp.http.account.controller;

import org.restexpress.Request;
import org.restexpress.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.oct.ga.comm.EcryptUtil;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaFollowingService;
import com.oct.ga.stp.http.account.EmailRegisterLoginRequest;
import com.oct.ga.stp.http.account.LoginResponse;
import com.oct.ga.stp.http.framework.AbstractRestController;
import com.oct.ga.stp.http.framework.Controller;
import com.oct.ga.stp.http.framework.Utils;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.device.SupDeviceService;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.StpSession;

import io.netty.handler.codec.http.HttpResponseStatus;

@Component
@Controller
public class EmailRegisterLoginController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailRegisterLoginController.class);
	private SupAccountService supAccountService;
	private SupDeviceService supDeviceService;
	private SupSessionService supSessionService;
	@Autowired
	private ApplicationContext context;

	public Object create(Request request, Response response) {
		EmailRegisterLoginRequest reqCmd = request.getBodyAs(EmailRegisterLoginRequest.class);
		String accountId = null;
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

			if (nickname == null || nickname.length() == 0) {
				nickname = loginName.substring(0, loginName.indexOf("@"));
			}
		} else {
			loginType = GlobalArgs.ACCOUNT_LOGIN_BY_PHONE;

			if (nickname == null || nickname.length() == 0) {
				nickname = loginName;
			}
		}

		try {

			// Logic: Record login action to log.
			if (supSessionService.verifyGateToken(gateToken, deviceId)) {
				if (supAccountService.verifyExist(loginType, loginName)) {
					response.setResponseStatus(HttpResponseStatus.CONFLICT);
					Utils.addErrorCode(ErrorCode.REGISTER_EMAIL_EXIST, response);
					return null;
				} else { // not exist
					int currentTimestamp = Utils.getCurrentTimeSeconds();
					accountId = supAccountService.createAccount(nickname, avatarUrl, desc, currentTimestamp);
					supAccountService.createLogin(accountId, loginType, loginName, currentTimestamp);
					supAccountService.resetPwd(loginType, loginName, md5pwd, currentTimestamp);

					if (osVersion.contains("android")) {
						apnsToken = EcryptUtil.md5ChatId(accountId, deviceId);
					}
					supDeviceService.modifyOsVersion(deviceId, osVersion, apnsToken, currentTimestamp);

					// session.setAttribute("deviceId", deviceId);
					// session.setAttribute("accountId", accountId);
					// session.setAttribute("accountName", nickname);
					// session.setAttribute("avatarUrl", avatarUrl);

					StpSession stpSession = new StpSession();
					stpSession.setAccountId(accountId);
					stpSession.setActive(true);
					stpSession.setDeviceId(deviceId);
					stpSession.setGateToken(gateToken);
					stpSession.setNotifyToken(apnsToken);
					stpSession.setDeviceOsVersion(osVersion);
					// stpSession.setIoSessionId(session.getId());
					stpSession.setLoginType(loginType);
					stpSession.setLoginName(loginName);
					String sessionTicket = supSessionService.applySessionTicket(stpSession);
					// session.setAttribute("sessionTicket", sessionTicket);
					try {
						GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
						activityService.createExerciseTask1(context, accountId, currentTimestamp);
						activityService.createExerciseTask2(context, accountId, currentTimestamp);
						activityService.createExerciseTask3(context, accountId, currentTimestamp);
					} catch (Exception e) {
						LOGGER.error("Create tasks failed", e);
					}

					try {
						GaFollowingService followingService = (GaFollowingService) context
								.getBean("gaFollowingService");

						// Logic: follow support service.
						followingService.follow(GlobalArgs.SUPPORT_SERVICE_ACCOUNT_ID, accountId, currentTimestamp);
						followingService.follow(accountId, GlobalArgs.SUPPORT_SERVICE_ACCOUNT_ID, currentTimestamp);
					} catch (Exception e) {
						LOGGER.error("Follow failed", e);
					}
					response.setResponseStatus(HttpResponseStatus.OK);
					LoginResponse loginResponse = new LoginResponse();
					loginResponse.setAccountId(accountId);
					loginResponse.setSessionToken(sessionTicket);
					return loginResponse;
				}
			} else {
				response.setResponseStatus(HttpResponseStatus.FORBIDDEN);
				Utils.addErrorCode(ErrorCode.GATE_TOKEN_NOT_EXIST, response);
				return null;
			}
		} catch (Exception e) {
			LOGGER.error("Email register login failed", e);
			response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@Autowired
	public void setSupAccountService(SupAccountService supAccountService) {
		this.supAccountService = supAccountService;
	}

	@Autowired
	public void setSupDeviceService(SupDeviceService supDeviceService) {
		this.supDeviceService = supDeviceService;
	}

	@Autowired
	public void setSupSessionService(SupSessionService supSessionService) {
		this.supSessionService = supSessionService;
	}

}
