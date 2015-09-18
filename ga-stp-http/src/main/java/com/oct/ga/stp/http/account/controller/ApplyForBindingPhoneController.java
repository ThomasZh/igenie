package com.oct.ga.stp.http.account.controller;

import org.restexpress.Request;
import org.restexpress.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.stp.http.account.ApplyForBindingPhoneRequest;
import com.oct.ga.stp.http.framework.AbstractRestController;
import com.oct.ga.stp.http.framework.Controller;
import com.oct.ga.stp.http.framework.Utils;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.sms.SupSmsService;

import io.netty.handler.codec.http.HttpResponseStatus;

@Component
@Controller
public class ApplyForBindingPhoneController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplyForBindingPhoneController.class);
	private SupAccountService accountService;
	private SupSmsService smsbox;

	public Object create(Request request, Response response) {
		ApplyForBindingPhoneRequest applyForBindingPhoneRequest = request.getBodyAs(ApplyForBindingPhoneRequest.class);
		short verificationType = applyForBindingPhoneRequest.getVerificationType();
		try {
			String ekey = accountService.applyVerificationCode(verificationType,
					applyForBindingPhoneRequest.getDeviceId(), applyForBindingPhoneRequest.getPhone(),
					Utils.getCurrentTimeSeconds());
			if (ekey == null || ekey.length() == 0) {
				response.setResponseStatus(HttpResponseStatus.FORBIDDEN);
				Utils.addErrorCode(ErrorCode.APPLY_VERIFICATON_CODE_TOO_MOUCH_TIMES, response);
			} else {
				// TODO cn/en
				String lang = "cn";
				smsbox.sendVerificationCode(applyForBindingPhoneRequest.getPhone(), ekey, lang);
				response.setResponseStatus(HttpResponseStatus.OK);
			}
		} catch (Exception e) {
			LOGGER.error("Apply for binding phone failed", e);
			response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@Autowired
	public void setAccountService(SupAccountService accountService) {
		this.accountService = accountService;
	}

	@Autowired
	public void setSmsbox(SupSmsService smsbox) {
		this.smsbox = smsbox;
	}

}
