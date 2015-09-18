package com.oct.ga.stp.http.account.controller;

import org.restexpress.Request;
import org.restexpress.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.stp.http.account.VerifyEmailRequest;
import com.oct.ga.stp.http.account.VerifyEmailResponse;
import com.oct.ga.stp.http.framework.AbstractRestController;
import com.oct.ga.stp.http.framework.Controller;
import com.oct.ga.stp.http.framework.Utils;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.domain.LostPwdEkey;

import io.netty.handler.codec.http.HttpResponseStatus;

@Component
@Controller
public class VerifyEmailController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(VerifyEmailController.class);
	private SupAccountService supAccountService;

	public Object create(Request request, Response response) {
		VerifyEmailRequest verifyEmailRequest = request.getBodyAs(VerifyEmailRequest.class);
		String ekey = verifyEmailRequest.getEkey();
		String email = null;

		try {
			LostPwdEkey ekeyInfo = supAccountService.queryEkey(ekey);
			String accountId = ekeyInfo.getAccountId();
			// exist
			if (accountId != null && accountId.length() > 0) {
				if (ekeyInfo.getTtl() < Utils.getCurrentTimeSeconds()) {
					response.setResponseStatus(HttpResponseStatus.FORBIDDEN);
					Utils.addErrorCode(ErrorCode.RESET_PWD_EKEY_EXPIRY_TIME, response);
					VerifyEmailResponse verifyEmailResponse = new VerifyEmailResponse();
					verifyEmailResponse.setEmail(email);
					return verifyEmailResponse;
				} else {
					email = ekeyInfo.getLoginName();
					response.setResponseStatus(HttpResponseStatus.OK);
					VerifyEmailResponse verifyEmailResponse = new VerifyEmailResponse();
					verifyEmailResponse.setEmail(email);
					return verifyEmailResponse;
				}
			} else { // not exist
				Utils.addErrorCode(ErrorCode.RESET_PWD_HAS_NO_EKEY, response);
				response.setResponseStatus(HttpResponseStatus.BAD_REQUEST);
				VerifyEmailResponse verifyEmailResponse = new VerifyEmailResponse();
				verifyEmailResponse.setEmail(email);
				return verifyEmailResponse;
			}
		} catch (Exception e) {
			response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
			LOGGER.error("Verify email failed", e);
		}
		return null;
	}

	@Autowired
	public void setSupAccountService(SupAccountService supAccountService) {
		this.supAccountService = supAccountService;
	}

}
