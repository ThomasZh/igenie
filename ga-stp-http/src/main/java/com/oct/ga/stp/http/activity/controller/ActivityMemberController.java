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

import com.oct.ga.activity.domain.ActivityMember;
import com.oct.ga.activity.service.ActivityMemberService;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.stp.http.activity.ActivityMemberResponse;
import com.oct.ga.stp.http.framework.AbstractRestController;
import com.oct.ga.stp.http.framework.Controller;
import com.oct.ga.stp.http.framework.Utils;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.StpSession;

@Component
@Controller
public class ActivityMemberController extends AbstractRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityMemberController.class);
	@Autowired
	private SupSessionService supSessionService;
	@Autowired
	private ActivityMemberService activityMemberService;

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
		String activityId = request.getHeader("activityId");
		long createTime = Utils.getHeaderAsLong(request, "createTime", Long.MAX_VALUE);
		int pageSize = Utils.requireHeaderAsInt(request, "pageSize");
		try {
			List<ActivityMember> activityMembers = activityMemberService.findByActivityId(activityId, createTime,
					pageSize);
			List<ActivityMemberResponse> activityMemberResponses = new ArrayList<>(activityMembers.size());
			for (ActivityMember activityMember : activityMembers) {
				ActivityMemberResponse activityMemberResponse = new ActivityMemberResponse();
				// TODO set other properties
				activityMemberResponse.setRank(activityMember.getRank());
			}
			return activityMemberResponses;
		} catch (RuntimeException e) {
			LOGGER.error("Read activity members failed", e);
			throw new ServiceException(e);
		}
	}

}
