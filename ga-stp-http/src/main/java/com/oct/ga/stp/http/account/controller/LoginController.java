package com.oct.ga.stp.http.account.controller;

import org.restexpress.Request;
import org.restexpress.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oct.ga.comm.EcryptUtil;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.stp.http.account.LoginRequest;
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
public class LoginController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	private SupAccountService supAccountService;
	private SupDeviceService supDeviceService;
	private SupSessionService supSessionService;

	public Object create(Request request, Response response) {
		LoginRequest loginRequest = request.getBodyAs(LoginRequest.class);
		short loginType = GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL;
		String gateToken = loginRequest.getGateToken();
		String deviceId = loginRequest.getMyDeviceId();
		String loginName = loginRequest.getEmail();
		String md5pwd = loginRequest.getPassword();
		String apnsToken = loginRequest.getApnsToken();
		String osVersion = loginRequest.getOsVersion();
		if (loginName.contains("@")) {
			loginType = GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL;
		} else {
			loginType = GlobalArgs.ACCOUNT_LOGIN_BY_PHONE;
		}

		try {
			if (supSessionService.verifyGateToken(gateToken, deviceId)) {
				String accountId = supAccountService.verifyLogin(loginType, loginName, md5pwd);
				if (accountId != null) {
					if (osVersion.contains("android")) {
						apnsToken = EcryptUtil.md5ChatId(accountId, deviceId);
					}
					supDeviceService.modifyOsVersion(deviceId, osVersion, apnsToken, Utils.getCurrentTimeSeconds());

					// AccountBasic account =
					// supAccountService.queryAccount(accountId);

					StpSession stpSession = new StpSession();
					stpSession.setAccountId(accountId);
					stpSession.setActive(true);
					stpSession.setDeviceId(deviceId);
					stpSession.setGateToken(gateToken);
					stpSession.setNotifyToken(apnsToken);
					stpSession.setDeviceOsVersion(osVersion);
					// stpSession.setIoSessionId(session.getId()); XXX no io
					// session id
					stpSession.setLoginType(loginType);
					stpSession.setLoginName(loginName);
					String sessionTicket = supSessionService.applySessionTicket(stpSession);
					// session.setAttribute("deviceId", deviceId);
					// session.setAttribute("accountId", accountId);
					// session.setAttribute("accountName",
					// account.getNickname());
					// session.setAttribute("avatarUrl",
					// account.getAvatarUrl());
					// session.setAttribute("sessionTicket", sessionTicket);
					response.setResponseStatus(HttpResponseStatus.OK);
					LoginResponse loginResponse = new LoginResponse();
					loginResponse.setAccountId(accountId);
					loginResponse.setSessionToken(sessionTicket);
					LOGGER.debug("Login success, {}", loginResponse);
					return loginResponse;
				} else {
					response.setResponseStatus(HttpResponseStatus.BAD_REQUEST);
					Utils.addErrorCode(ErrorCode.LOGIN_WRONG_PWD, response);
					return null;
				}
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
