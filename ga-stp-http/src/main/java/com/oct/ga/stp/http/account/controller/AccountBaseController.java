package com.oct.ga.stp.http.account.controller;

import org.restexpress.Request;
import org.restexpress.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.stp.http.account.AccountBaseResponse;
import com.oct.ga.stp.http.framework.Controller;
import com.oct.ga.stp.http.framework.AbstractRestController;
import com.redoct.ga.sup.account.SupAccountService;

import io.netty.handler.codec.http.HttpResponseStatus;

@Component
@Controller
public class AccountBaseController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountBaseController.class);
	private SupAccountService supAccountService;

	public Object read(Request request, Response response) {
		String accountId = request.getHeader("accountId");
		try {
			AccountBasic account = supAccountService.queryAccount(accountId);// FIXME
			AccountBaseResponse accountBaseResponse = new AccountBaseResponse();
			accountBaseResponse.setAccountId(accountId);
			accountBaseResponse.setAvatarUrl(account.getAvatarUrl());
			accountBaseResponse.setName(account.getNickname());
			response.setResponseStatus(HttpResponseStatus.OK);
			return accountBaseResponse;
		} catch (Exception e) {
			response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
			LOGGER.error("Get account base failed", e);
		}
		return null;
	}

	@Autowired
	public void setSupAccountService(SupAccountService supAccountService) {
		this.supAccountService = supAccountService;
	}

}
