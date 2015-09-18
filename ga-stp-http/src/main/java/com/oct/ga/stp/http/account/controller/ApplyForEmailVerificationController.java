package com.oct.ga.stp.http.account.controller;

import org.restexpress.Request;
import org.restexpress.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.stp.http.account.ApplyForEmailVerificationRequest;
import com.oct.ga.stp.http.framework.AbstractRestController;
import com.oct.ga.stp.http.framework.Controller;
import com.oct.ga.stp.http.framework.Utils;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.mail.SupMailService;

import io.netty.handler.codec.http.HttpResponseStatus;

@Component
@Controller
public class ApplyForEmailVerificationController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplyForEmailVerificationController.class);
	private SupAccountService supAccountService;
	private SupMailService supMailService;

	public Object create(Request request, Response response) {
		ApplyForEmailVerificationRequest retrievePwdRequest = request.getBodyAs(ApplyForEmailVerificationRequest.class);
		String loginName = retrievePwdRequest.getEmail();

		try {
			if (supAccountService.verifyExist(GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, loginName)) {

				String ekey = supAccountService.createEkey(GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, loginName,
						Utils.getCurrentTimeSeconds());

				AccountBasic account = supAccountService.queryAccount(GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, loginName);

				// send email to myself.
				supMailService.sendForgotPwd(loginName, account.getNickname(), ekey);
				response.setResponseStatus(HttpResponseStatus.OK);
				return null;
			} else {
				response.setResponseStatus(HttpResponseStatus.BAD_REQUEST);
				Utils.addErrorCode(ErrorCode.MEMBER_NOT_GA_ACCOUNT, response);
				return null;
			}
		} catch (Exception e) {
			response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
			LOGGER.error("Retrieve password failed", e);
			return null;
		}
	}

	@Autowired
	public void setSupAccountService(SupAccountService supAccountService) {
		this.supAccountService = supAccountService;
	}

	@Autowired
	public void setSupMailService(SupMailService supMailService) {
		this.supMailService = supMailService;
	}

}
