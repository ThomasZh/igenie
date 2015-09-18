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
import com.redoct.ga.sup.session.SupSessionService;

import io.netty.handler.codec.http.HttpResponseStatus;

@Component
@Controller
public class RebindPhoneController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RebindPhoneController.class);
	@Autowired
	private SupAccountService supAccountService;
	@Autowired
	private SupSessionService supSessionService;

	@Override
	public Object create(Request request, Response response) {
		BindingPhoneRequest bindingPhoneRequest = request.getBodyAs(BindingPhoneRequest.class);
		short verificationType = GlobalArgs.VERIFICATION_TYPE_BIND_PHONE;
		String phone = bindingPhoneRequest.getPhone();
		String md5pwd = bindingPhoneRequest.getMd5pwd();
		String verificationCode = bindingPhoneRequest.getVerificationCode();
		String accountId = bindingPhoneRequest.getAccountId();
		String deviceId = bindingPhoneRequest.getDeviceId();
		try {
			VerificationCode code = supAccountService.queryVerificationCode(verificationType, deviceId);
			if (code != null) {
				if (verificationCode.equals(code.getEkey())) {
					if (code.getTtl() < Utils.getCurrentTimeSeconds()) {
						response.setResponseStatus(HttpResponseStatus.FORBIDDEN);
						Utils.addErrorCode(ErrorCode.VERIFICATON_CODE_TIMEOUT, response);
						return null;
					} else {
						if (supAccountService.verifyExist(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone)) {
							// clean old account session
							AccountBasic oldAccount = supAccountService.queryAccount(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE,
									phone);
							String oldAccountId = oldAccount.getAccountId();
							supSessionService.removeStpSession(oldAccountId);
							supAccountService.modifyAccountId4Login(accountId, GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone,
									Utils.getCurrentTimeSeconds());
							response.setResponseStatus(HttpResponseStatus.OK);
							return null;
						} else { // this login(phone) not exist
							int currentTimestamp = Utils.getCurrentTimeSeconds();
							supAccountService.createLogin(accountId, GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone,
									currentTimestamp);
							supAccountService.resetPwd(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, phone, md5pwd,
									currentTimestamp);
							response.setResponseStatus(HttpResponseStatus.OK);
							return null;
						}
					}
				} else {
					response.setResponseStatus(HttpResponseStatus.FORBIDDEN);
					Utils.addErrorCode(ErrorCode.NOT_MATCH_VERIFICATION_CODE, response);
					return null;
				}
			} else {
				response.setResponseStatus(HttpResponseStatus.FORBIDDEN);
				Utils.addErrorCode(ErrorCode.NOT_MATCH_VERIFICATION_CODE, response);
				return null;
			}
		} catch (Exception e) {
			LOGGER.error("Rebind phone failed", e);
			response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

}
