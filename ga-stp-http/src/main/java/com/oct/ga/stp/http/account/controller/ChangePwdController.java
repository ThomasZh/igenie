package com.oct.ga.stp.http.account.controller;

import org.restexpress.Request;
import org.restexpress.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oct.ga.stp.http.account.ChangePwdRequest;
import com.oct.ga.stp.http.framework.AbstractRestController;
import com.oct.ga.stp.http.framework.Controller;
import com.oct.ga.stp.http.framework.Utils;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.StpSession;

import io.netty.handler.codec.http.HttpResponseStatus;

@Component
@Controller
public class ChangePwdController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChangePwdController.class);
	private SupAccountService supAccountService;
	private SupSessionService supSessionService;

	public Object create(Request request, Response response) {
		String sessionId = Utils.getSessionId(request);
		ChangePwdRequest changePwdRequest = request.getBodyAs(ChangePwdRequest.class);
		try {
			StpSession stpSession = supSessionService.queryStpSessionByTicket(sessionId);
			if (stpSession == null) {
				response.setResponseStatus(HttpResponseStatus.UNAUTHORIZED);
				return null;
			}
			short loginType = stpSession.getLoginType();
			String accountId = supAccountService.verifyLogin(loginType, stpSession.getLoginName(),
					changePwdRequest.getOldPassword());
			if (accountId != null) {
				supAccountService.resetPwd(loginType, stpSession.getLoginName(), changePwdRequest.getNewPassword(),
						Utils.getCurrentTimeSeconds());
				response.setResponseStatus(HttpResponseStatus.OK);
			} else {
				response.setResponseStatus(HttpResponseStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			LOGGER.error("Change password failed, " + changePwdRequest, e);
			response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@Autowired
	public void setSupAccountService(SupAccountService supAccountService) {
		this.supAccountService = supAccountService;
	}

	@Autowired
	public void setSupSessionService(SupSessionService supSessionService) {
		this.supSessionService = supSessionService;
	}

}
