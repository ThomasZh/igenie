package com.oct.ga.stp.http.account.controller;

import java.util.UUID;

import org.restexpress.Request;
import org.restexpress.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.service.GaFollowingService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.http.account.MyAccountResponse;
import com.oct.ga.stp.http.framework.AbstractRestController;
import com.oct.ga.stp.http.framework.Controller;
import com.oct.ga.stp.http.framework.Utils;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.StpSession;

import io.netty.handler.codec.http.HttpResponseStatus;

@Component
@Controller
public class MyAccountController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MyAccountController.class);
	private SupAccountService supAccountService;
	private SupSessionService supSessionService;
	private GaFollowingService gaFollowingService;
	private GaTaskService gaTaskService;

	public Object read(Request request, Response response) {
		String sessionId = Utils.getSessionId(request);
		try {
			StpSession stpSession = supSessionService.queryStpSessionByTicket(sessionId);
			if (stpSession == null) {
				response.setResponseStatus(HttpResponseStatus.UNAUTHORIZED);
				return null;
			}
			// return account info: accountname, phone, photo
			AccountMaster account = supAccountService.queryAccountMaster(stpSession.getAccountId());
			// resp the current timestamp of server
			MyAccountResponse myAccountResponse = new MyAccountResponse();
			myAccountResponse.setAccountMaster(account);
			myAccountResponse.setCurrentTimestamp(Utils.getCurrentTimeSeconds());
			response.setResponseStatus(HttpResponseStatus.OK);
			return myAccountResponse;
		} catch (Exception e) {
			response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
			LOGGER.error("Get my account failed", e);
		}
		// Warning: OldStpEventHandler do not response anything.
		return null;
	}

	public Object update(Request request, Response response) {
		String sessionId = Utils.getSessionId(request);
		AccountMaster accountDetail = request.getBodyAs(AccountMaster.class);
		try {
			StpSession stpSession = supSessionService.queryStpSessionByTicket(sessionId);
			if (stpSession == null) {
				response.setResponseStatus(HttpResponseStatus.UNAUTHORIZED);
				return null;
			}
			AccountBasic account = new AccountBasic();
			// ? accountId is required
			account.setAccountId(stpSession.getAccountId());
			account.setNickname(accountDetail.getNickname());
			account.setAvatarUrl(accountDetail.getAvatarUrl());
			account.setDesc(accountDetail.getDesc());
			int currentTimestamp = Utils.getCurrentTimeSeconds();
			supAccountService.modifyAccountBasicInfo(account, currentTimestamp);
			gaFollowingService.updateMyLastUpdateTimeInFollowed(account.getAccountId(), currentTimestamp);
			try {
				String activityId = gaTaskService.modifyExerciseProject2Completed(account.getAccountId(),
						GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_EXERCISE_1, currentTimestamp);
				if (activityId != null) {
					GaTaskLog log = new GaTaskLog();
					log.setLogId(UUID.randomUUID().toString());
					log.setChannelId(activityId);
					log.setFromAccountId(account.getAccountId());
					log.setActionTag(GlobalArgs.TASK_ACTION_COMPLETED);
					log.setToActionId(activityId);
					gaTaskService.addLog(log, currentTimestamp);
					gaTaskService.addLogExtend(log.getLogId(), account.getAccountId(), activityId,
							GlobalArgs.TASK_ACTION_COMPLETED, GlobalArgs.SYNC_STATE_READ, currentTimestamp);
				}
			} catch (Exception e) {
				LOGGER.error("Add log failed", e);
			}
			response.setResponseStatus(HttpResponseStatus.OK);
			return null;
		} catch (Exception e) {
			LOGGER.error("Update my account failed", e);
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

	@Autowired
	public void setGaFollowingService(GaFollowingService gaFollowingService) {
		this.gaFollowingService = gaFollowingService;
	}

	@Autowired
	public void setGaTaskService(GaTaskService gaTaskService) {
		this.gaTaskService = gaTaskService;
	}

}
