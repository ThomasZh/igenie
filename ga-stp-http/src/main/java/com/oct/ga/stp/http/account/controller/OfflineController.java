package com.oct.ga.stp.http.account.controller;

import org.restexpress.Request;
import org.restexpress.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oct.ga.stp.http.account.OfflineRequest;
import com.oct.ga.stp.http.framework.AbstractRestController;
import com.oct.ga.stp.http.framework.Controller;
import com.redoct.ga.sup.session.SupSessionService;

import io.netty.handler.codec.http.HttpResponseStatus;

@Component
@Controller
public class OfflineController extends AbstractRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OfflineController.class);
	private SupSessionService supSessionService;

	public Object create(Request request, Response response) {
		OfflineRequest offlineRequest = request.getBodyAs(OfflineRequest.class);
		String accountId = offlineRequest.getAccountId();
		try {
			supSessionService.inactiveStpSession(accountId);
			response.setResponseStatus(HttpResponseStatus.OK);
		} catch (Exception e) {
			response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
			LOGGER.error("Inactive session failed", e);
		}
		return null;
	}

	@Autowired
	public void setSupSessionService(SupSessionService supSessionService) {
		this.supSessionService = supSessionService;
	}

}
