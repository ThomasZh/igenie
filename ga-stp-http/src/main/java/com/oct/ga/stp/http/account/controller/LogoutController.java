package com.oct.ga.stp.http.account.controller;

import org.restexpress.Request;
import org.restexpress.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oct.ga.stp.http.framework.AbstractRestController;
import com.oct.ga.stp.http.framework.Controller;
import com.oct.ga.stp.http.framework.Utils;
import com.redoct.ga.sup.session.SupSessionService;

import io.netty.handler.codec.http.HttpResponseStatus;

@Component
@Controller
public class LogoutController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogoutController.class);
	private SupSessionService supSessionService;

	public Object create(Request request, Response response) {
		String sessionId = Utils.getSessionId(request);
		try {
			boolean removed = supSessionService.removeStpSessionByTicket(sessionId);
			response.setResponseStatus(HttpResponseStatus.OK);
			LOGGER.info("Logout success, [sessionId = {}, removed = {}]", sessionId, removed);
		} catch (Exception e) {
			LOGGER.error("Logout failed", e);
			response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@Autowired
	public void setSupSessionService(SupSessionService supSessionService) {
		this.supSessionService = supSessionService;
	}

}
