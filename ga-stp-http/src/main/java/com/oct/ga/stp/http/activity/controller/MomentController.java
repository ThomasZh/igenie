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
import com.oct.ga.activity.domain.Moment;
import com.oct.ga.activity.service.ActivityService;
import com.oct.ga.activity.service.MomentService;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.stp.http.activity.MomentComment;
import com.oct.ga.stp.http.activity.MomentRequest;
import com.oct.ga.stp.http.activity.MomentResponse;
import com.oct.ga.stp.http.framework.AbstractRestController;
import com.oct.ga.stp.http.framework.Controller;
import com.oct.ga.stp.http.framework.Utils;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.StpSession;

import io.netty.handler.codec.http.HttpResponseStatus;

@Component
@Controller
public class MomentController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MomentController.class);
	@Autowired
	private SupSessionService supSessionService;
	@Autowired
	private MomentService momentService;
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
		String activityId = request.getHeader("activityId");
		long createTime = Utils.getHeaderAsLong(request, "createTime", Long.MAX_VALUE);
		int pageSize = Utils.requireHeaderAsInt(request, "pageSize");
		try {
			List<Moment> moments = momentService.findByActivityId(activityId, createTime, pageSize);
			List<MomentResponse> momentResponses = new ArrayList<>(moments.size());
			for (Moment moment : moments) {
				MomentResponse momentResponse = new MomentResponse();
				momentResponse.setActivityId(moment.getActivityId());
				Activity activity = activityService.read(moment.getActivityId());
				if (activity != null) {
					momentResponse.setActivityName(activity.getName());
				}
				momentResponse.setDesc(moment.getDesc());
				momentResponse.setImageUrls(moment.getImageUrls());
				momentResponse.setCommentNum(moment.getCommentNum());
				momentResponse.setLikeNum(moment.getLikeNum());
				momentResponse.setCreateTime(moment.getCreateTime());
				// TODO
				List<MomentComment> comments = null;
				momentResponse.setComments(comments);
				List<String> likes = null;
				momentResponse.setLikes(likes);
				momentResponse.setLike(false);
				momentResponses.add(momentResponse);
			}
			response.setResponseStatus(HttpResponseStatus.OK);
			return momentResponses;
		} catch (RuntimeException e) {
			LOGGER.error("Read moments failed", e);
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
		String id = request.getHeader("id");
		String activityId = request.getHeader("activityId");
		MomentRequest momentRequest = request.getBodyAs(MomentRequest.class);
		Moment moment = new Moment();
		moment.setId(id);
		moment.setAccountId(stpSession.getAccountId());
		moment.setActivityId(activityId);
		moment.setDesc(momentRequest.getDesc());
		moment.setImageUrls(momentRequest.getImageUrls());
		try {
			momentService.create(moment);
		} catch (RuntimeException e) {
			LOGGER.error("Create moment failed", e);
			throw new ServiceException(e);
		}
		return null;
	}

	@Override
	public Object delete(Request request, Response response) {
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
		String id = request.getHeader("id");
		try {
			momentService.delete(id);
		} catch (RuntimeException e) {
			LOGGER.error("Delete moment failed", e);
			throw new ServiceException(e);
		}
		return null;
	}

}
