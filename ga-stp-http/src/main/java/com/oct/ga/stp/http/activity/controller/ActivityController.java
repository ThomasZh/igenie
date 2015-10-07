package com.oct.ga.stp.http.activity.controller;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.exception.ServiceException;
import org.restexpress.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oct.ga.activity.domain.Activity;
import com.oct.ga.activity.domain.ActivityConstants;
import com.oct.ga.activity.domain.ActivityMember;
import com.oct.ga.activity.service.ActivityService;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.stp.http.activity.ActivityDetailResponse;
import com.oct.ga.stp.http.activity.ActivityRequest;
import com.oct.ga.stp.http.framework.AbstractRestController;
import com.oct.ga.stp.http.framework.Controller;
import com.oct.ga.stp.http.framework.Utils;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.StpSession;

import io.netty.handler.codec.http.HttpResponseStatus;

@Component
@Controller
public class ActivityController extends AbstractRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityController.class);
	@Autowired
	private SupSessionService supSessionService;
	@Autowired
	private ActivityService activityService;

	@Override
	public Object create(Request request, Response response) {
		String sessionId = Utils.requireSessionId(request);
		ActivityRequest activityRequest = request.getBodyAs(ActivityRequest.class);
		Activity activity = new Activity();
		Utils.copyProperties(activityRequest, activity);
		activity.setStatus(ActivityConstants.ACTIVITY_STATUS_UNPUBLISHED);// TODO
		try {
			activityService.create(activity);
			response.setResponseStatus(HttpResponseStatus.OK);
		} catch (RuntimeException e) {
			LOGGER.error("Create activity failed", e);
			throw new ServiceException(e);
		}
		return null;
	}

	@Override
	public Object read(Request request, Response response) {
		String sessionId = Utils.requireSessionId(request);
		StpSession stpSession = null;
		try {
			stpSession = supSessionService.queryStpSessionByTicket(sessionId);
		} catch (SupSocketException e) {
			LOGGER.error("Read session failed", e);
			throw new ServiceException(e);
		}
		if (stpSession == null) {
			LOGGER.error("No session found for session id:{}", sessionId);
			throw new UnauthorizedException();
		}
		String accountId = stpSession.getAccountId();
		String activityId = request.getHeader("activityId");
		try {
			Activity activity = activityService.read(activityId);
			ActivityDetailResponse activityDetailResponse = new ActivityDetailResponse();
			Utils.copyProperties(activity, activityDetailResponse);
			activityDetailResponse.setActivityId(activity.getId());
			activityDetailResponse.setActivityName(activity.getName());
			ActivityMember leader = activity.getLeader();
			if (leader != null) {
				int myRank = accountId.equals(leader.getAccountId()) ? ActivityConstants.ACTIVITY_MEMBER_RANK_LEADER
						: ActivityConstants.ACTIVITY_MEMBER_RANK_MEMBER;
				activityDetailResponse.setMyRank(myRank);
				// TODO
			}
			// TODO setFaverite
			return activityDetailResponse;
		} catch (RuntimeException e) {
			LOGGER.error("Read activity failed", e);
			throw new ServiceException(e);
		}
	}

	@Override
	public Object update(Request request, Response response) {
		String sessionId = Utils.requireSessionId(request);
		StpSession stpSession = null;
		try {
			stpSession = supSessionService.queryStpSessionByTicket(sessionId);
		} catch (SupSocketException e) {
			LOGGER.error("Read session failed", e);
			throw new ServiceException(e);
		}
		if (stpSession == null) {
			LOGGER.error("No session found for session id:{}", sessionId);
			throw new UnauthorizedException();
		}
		String activityId = request.getHeader("activityId");
		ActivityRequest activityRequest = request.getBodyAs(ActivityRequest.class);
		Activity activity = new Activity();
		Utils.copyProperties(activityRequest, activity);
		activity.setId(activityId);
		try {
			activityService.update(activity);
			response.setResponseStatus(HttpResponseStatus.OK);
		} catch (RuntimeException e) {
			LOGGER.error("Read activity failed", e);
			throw new ServiceException(e);
		}
		return null;
	}

}
