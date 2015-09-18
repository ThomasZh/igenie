package com.oct.ga.stp.http.account.controller;

import org.restexpress.Request;
import org.restexpress.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.stp.http.account.ResetPwdRequest;
import com.oct.ga.stp.http.framework.AbstractRestController;
import com.oct.ga.stp.http.framework.Controller;
import com.oct.ga.stp.http.framework.Utils;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.domain.LostPwdEkey;

import io.netty.handler.codec.http.HttpResponseStatus;

@Component
@Controller
public class ResetPwdController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ResetPwdController.class);
	private SupAccountService supAccountService;

	public Object create(Request request, Response response) {
		ResetPwdRequest resetPwdRequest = request.getBodyAs(ResetPwdRequest.class);
		// String email = resetPwdRequest.getEmail().toLowerCase();
		String ekey = resetPwdRequest.getEkey();
		String md5pwd = resetPwdRequest.getNewPassword();
		// String accountId = null;
		int currentTimestamp = Utils.getCurrentTimeSeconds();
		try {
			LostPwdEkey ekeyInfo = supAccountService.queryEkey(ekey);
			if (ekeyInfo.getTtl() < currentTimestamp) {
				response.setResponseStatus(HttpResponseStatus.FORBIDDEN);
				Utils.addErrorCode(ErrorCode.RESET_PWD_EKEY_EXPIRY_TIME, response);
			} else {
				supAccountService.resetPwd(ekeyInfo.getLoginType(), ekeyInfo.getLoginName(), md5pwd, currentTimestamp);
				response.setResponseStatus(HttpResponseStatus.OK);
			}
		} catch (Exception e) {
			LOGGER.error("Reset password failed", e);
			response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@Autowired
	public void setSupAccountService(SupAccountService supAccountService) {
		this.supAccountService = supAccountService;
	}

}
