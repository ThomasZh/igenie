package com.oct.ga.stp.http.activity.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.oct.ga.stp.http.activity.ActivityResponse;
import com.oct.ga.stp.http.framework.AbstractRestController;
import com.oct.ga.stp.http.framework.Controller;
import com.oct.ga.stp.http.framework.Utils;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.StpSession;

import io.netty.handler.codec.http.HttpResponseStatus;

@Component
@Controller
public class FriendActivityController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FriendActivityController.class);
	@Autowired
	private SupSessionService supSessionService;
	@Autowired
	private ActivityService activityService;

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
		try {
			String accountId = stpSession.getAccountId();
			long beginTime = Utils.getHeaderAsLong(request, "beginTime", Long.MAX_VALUE);
			boolean prev = Utils.getHeaderAsBoolean(request, "prev", false);
			int pageSize = Utils.requireHeaderAsInt(request, "pageSize");
			List<Activity> activities = activityService.findFriendActivities(accountId, beginTime, prev, pageSize);
			List<ActivityResponse> activityResponses = new ArrayList<>(activities.size());
			for (Activity activity : activities) {
				ActivityResponse activityResponse = new ActivityResponse();
				activityResponse.setActivityId(activity.getId());
				activityResponse.setActivityName(activity.getName());
				activityResponse.setBeginTime(activity.getBeginTime());
				activityResponse.setEndTime(activity.getEndTime());
				activityResponse.setBgImageUrl(activity.getBgImageUrl());
				ActivityMember leader = activity.getLeader();
				if (leader != null) {
					int myRank = accountId.equals(leader.getAccountId()) ? ActivityConstants.ACTIVITY_MEMBER_RANK_LEADER
							: ActivityConstants.ACTIVITY_MEMBER_RANK_MEMBER;
					activityResponse.setMyRank(myRank);
					// TODO
				}
			}
			response.setResponseStatus(HttpResponseStatus.OK);
			return activityResponses;
		} catch (RuntimeException e) {
			LOGGER.error("Read friend activities failed", e);
			throw new ServiceException(e);
		}
	}

}
