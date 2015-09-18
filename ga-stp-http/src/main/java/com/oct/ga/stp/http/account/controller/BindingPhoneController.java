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
import com.oct.ga.stp.http.account.BindingPhoneRequest;
import com.oct.ga.stp.http.framework.AbstractRestController;
import com.oct.ga.stp.http.framework.Controller;
import com.oct.ga.stp.http.framework.Utils;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.domain.VerificationCode;

import io.netty.handler.codec.http.HttpResponseStatus;

@Component
@Controller
public class BindingPhoneController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BindingPhoneController.class);
	private SupAccountService supAccountService;

	public Object create(Request request, Response response) {
		short verificationType = GlobalArgs.VERIFICATION_TYPE_BIND_PHONE;
		BindingPhoneRequest bindingPhoneRequest = request.getBodyAs(BindingPhoneRequest.class);
		String phone = bindingPhoneRequest.getPhone();
		String md5pwd = bindingPhoneRequest.getMd5pwd();
		String verificationCode = bindingPhoneRequest.getVerificationCode();
		try {

			String deviceId = bindingPhoneRequest.getDeviceId();
			VerificationCode code = supAccountService.queryVerificationCode(verificationType, deviceId);
			if (code == null || !verificationCode.equals(code.getEkey())) {
				response.setResponseStatus(HttpResponseStatus.FORBIDDEN);
				Utils.addErrorCode(ErrorCode.NOT_MATCH_VERIFICATION_CODE, response);
				return null;
			}
			if (code.getTtl() < Utils.getCurrentTimeSeconds()) {
				response.setResponseStatus(HttpResponseStatus.FORBIDDEN);
				Utils.addErrorCode(ErrorCode.VERIFICATON_CODE_TIMEOUT, response);
				return null;
			}
			if (supAccountService.verifyExist(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone)) {
				AccountBasic oldAccount = supAccountService.queryAccount(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone);
				String oldAccountId = oldAccount.getAccountId();

				String email = supAccountService.queryLoginName(oldAccountId, GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL);
				if (email != null && email.length() > 0) {
					response.setResponseStatus(HttpResponseStatus.CONFLICT);
					Utils.addErrorCode(ErrorCode.PHONE_ALREADY_BIND, response);
					return null;
				}

				String wechat = supAccountService.queryLoginName(oldAccountId, GlobalArgs.ACCOUNT_LOGIN_BY_WECHAT);
				if (wechat != null && wechat.length() > 0) {
					response.setResponseStatus(HttpResponseStatus.CONFLICT);
					Utils.addErrorCode(ErrorCode.PHONE_ALREADY_BIND, response);
					return null;
				}

				response.setResponseStatus(HttpResponseStatus.CONFLICT);
				Utils.addErrorCode(ErrorCode.REGISTER_PHONE_EXIST, response);
			} else { // this login(phone) not exist
				String accountId = bindingPhoneRequest.getAccountId();
				int currentTimestamp = Utils.getCurrentTimeSeconds();
				supAccountService.createLogin(accountId, GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone, currentTimestamp);
				supAccountService.resetPwd(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone, md5pwd, currentTimestamp);
				response.setResponseStatus(HttpResponseStatus.OK);
			}
		} catch (Exception e) {
			LOGGER.error("Binding phone failed", e);
			response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@Autowired
	public void setSupAccountService(SupAccountService supAccountService) {
		this.supAccountService = supAccountService;
	}

}
