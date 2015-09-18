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
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaFollowingService;
import com.oct.ga.stp.http.account.LoginResponse;
import com.oct.ga.stp.http.account.SsoLoginRequest;
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
public class SsoLoginController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SsoLoginController.class);
	private SupAccountService supAccountService;
	private SupDeviceService supDeviceService;
	private SupSessionService supSessionService;
	@Autowired
	private ApplicationContext context;

	public Object create(Request request, Response response) {
		SsoLoginRequest loginRequest = request.getBodyAs(SsoLoginRequest.class);

		String accountId = null;
		String osVersion = loginRequest.getOsVersion();
		String gateToken = loginRequest.getGateToken();
		String deviceId = loginRequest.getDeviceId();
		String nickname = loginRequest.getNickname();
		String desc = loginRequest.getDesc();
		short loginType = loginRequest.getLoginType();
		String loginName = loginRequest.getLoginName();
		String avatarUrl = loginRequest.getImageUrl();
		String apnsToken = loginRequest.getApnsToken();

		try {
			int currentTimestamp = Utils.getCurrentTimeSeconds();
			if (supSessionService.verifyGateToken(gateToken, deviceId)) {
				// LOGIC: account is exist?
				if (supAccountService.verifyExist(loginType, loginName)) {

					AccountBasic account = supAccountService.queryAccount(loginType, loginName);
					accountId = account.getAccountId();

					account.setNickname(nickname);
					account.setAvatarUrl(avatarUrl);
					account.setDesc(desc);
					supAccountService.modifyAccountBasicInfo(account, currentTimestamp);
				} else { // not exist
					accountId = supAccountService.createAccount(nickname, avatarUrl, desc, currentTimestamp);
					supAccountService.createLogin(accountId, loginType, loginName, currentTimestamp);

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
				}

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

				response.setResponseStatus(HttpResponseStatus.OK);
				LoginResponse loginResponse = new LoginResponse();
				loginResponse.setAccountId(accountId);
				loginResponse.setSessionToken(sessionTicket);
				return loginResponse;
			} else {
				response.setResponseStatus(HttpResponseStatus.BAD_REQUEST);
				Utils.addErrorCode(ErrorCode.GATE_TOKEN_NOT_EXIST, response);
				return null;
			}
		} catch (Exception e) {
			LOGGER.error("Login failed", e);
			response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
			return null;
		}
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
