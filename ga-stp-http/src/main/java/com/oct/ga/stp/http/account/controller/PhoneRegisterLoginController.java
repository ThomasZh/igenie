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
import com.oct.ga.stp.http.account.PhoneRegisterLoginRequest;
import com.oct.ga.stp.http.framework.AbstractRestController;
import com.oct.ga.stp.http.framework.Controller;
import com.oct.ga.stp.http.framework.Utils;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.domain.VerificationCode;
import com.redoct.ga.sup.device.SupDeviceService;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.StpSession;

import io.netty.handler.codec.http.HttpResponseStatus;

@Component
@Controller
public class PhoneRegisterLoginController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PhoneRegisterLoginController.class);
	private SupAccountService supAccountService;
	private SupDeviceService supDeviceService;
	private SupSessionService supSessionService;
	@Autowired
	private ApplicationContext context;

	public Object create(Request request, Response response) {
		PhoneRegisterLoginRequest phoneRegisterLoginRequest = request.getBodyAs(PhoneRegisterLoginRequest.class);

		String accountId;
		short loginType = GlobalArgs.ACCOUNT_LOGIN_BY_PHONE;
		String osVersion = phoneRegisterLoginRequest.getOsVersion();
		String gateToken = phoneRegisterLoginRequest.getGateToken();
		String deviceId = phoneRegisterLoginRequest.getDeviceId();
		String apnsToken = phoneRegisterLoginRequest.getApnsToken();
		String phone = phoneRegisterLoginRequest.getPhone();
		String md5pwd = phoneRegisterLoginRequest.getMd5pwd();
		String verificationCode = phoneRegisterLoginRequest.getVerificationCode();
		// String lang = phoneRegisterLoginRequest.getLang();

		try {

			if (supSessionService.verifyGateToken(gateToken, deviceId)) {
				VerificationCode code = supAccountService
						.queryVerificationCode(GlobalArgs.VERIFICATION_TYPE_PHONE_REGISTER, deviceId);
				if (code != null) {
					if (!verificationCode.equals(code.getEkey())) {
						Utils.addErrorCode(ErrorCode.NOT_MATCH_VERIFICATION_CODE, response);
						response.setResponseStatus(HttpResponseStatus.FORBIDDEN);
						return null;
					} else {
						if (code.getTtl() < Utils.getCurrentTimeSeconds()) {
							Utils.addErrorCode(ErrorCode.VERIFICATON_CODE_TIMEOUT, response);
							response.setResponseStatus(HttpResponseStatus.FORBIDDEN);
							return null;
						}
						if (!phone.equals(code.getPhone())) {
							Utils.addErrorCode(ErrorCode.NOT_MATCH_PHONE, response);
							return null;
						}
					}
				}

				String nickname = null;
				String avatarUrl = null;
				String desc = null;

				int currentTimestamp = Utils.getCurrentTimeSeconds();
				// LOGIC: account is exist?
				if (supAccountService.verifyExist(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone)) {
					AccountBasic account = supAccountService.queryAccount(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone);
					accountId = account.getAccountId();
					supAccountService.resetPwd(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone, md5pwd, currentTimestamp);
				} else { // not exist
					accountId = supAccountService.createAccount(nickname, avatarUrl, desc, currentTimestamp);
					supAccountService.createLogin(accountId, GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone,
							currentTimestamp);
					supAccountService.resetPwd(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone, md5pwd, currentTimestamp);

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
				stpSession.setLoginName(phone);
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
			}
		} catch (Exception e) {
			LOGGER.error("Phone register login failed", e);
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
