package com.oct.ga.stp.http.club.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.restexpress.Request;
import org.restexpress.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oct.ga.comm.domain.club.ItineraryInfo;
import com.oct.ga.service.GaItineraryService;
import com.oct.ga.stp.http.club.ItineraryRequest;
import com.oct.ga.stp.http.club.ItineraryResponse;
import com.oct.ga.stp.http.framework.AbstractRestController;
import com.oct.ga.stp.http.framework.Controller;
import com.oct.ga.stp.http.framework.Utils;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.StpSession;

import io.netty.handler.codec.http.HttpResponseStatus;

@Component
@Controller
public class ItineraryController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItineraryController.class);
	@Autowired
	private GaItineraryService gaItineraryService;
	@Autowired
	private SupSessionService supSessionService;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Object create(Request request, Response response) {
		try {
			String activityId = request.getHeader("activityId");
			ItineraryRequest[] itineraryRequests = request.getBodyAs(ItineraryRequest[].class);
			List<ItineraryInfo> itineraryInfos = new ArrayList<>(itineraryRequests.length);
			try {
				for (int i = 0; i < itineraryRequests.length; i++) {
					ItineraryRequest itineraryRequest = itineraryRequests[i];
					ItineraryInfo itineraryInfo = new ItineraryInfo();
					Utils.copyProperties(itineraryRequest, itineraryInfo, "imageUrls");
					itineraryInfo.setActivityId(activityId);
					itineraryInfo.setIdx(i);
					if (itineraryRequest.getImageUrls() != null) {
						itineraryInfo.setImageUrls(objectMapper.writeValueAsString(itineraryRequest.getImageUrls()));
					}
					itineraryInfos.add(itineraryInfo);
				}
			} catch (JsonProcessingException e) {
				LOGGER.error("Serialize field 'imageUrls' failed", e);
				response.setResponseStatus(HttpResponseStatus.BAD_REQUEST);
				return null;
			}
			String sessionId = Utils.getSessionId(request);
			StpSession stpSession = supSessionService.queryStpSessionByTicket(sessionId);
			if (stpSession == null) {
				response.setResponseStatus(HttpResponseStatus.UNAUTHORIZED);
				return null;
			}
			// TODO need check permission
			if (itineraryInfos.size() > 0) {
				gaItineraryService.overWrite(itineraryInfos);
			} else {
				gaItineraryService.delete(activityId);
			}
			response.setResponseStatus(HttpResponseStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Create itineraries failed", e);
			response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object read(Request request, Response response) {
		String activityId = request.getHeader("activityId");
		String sessionId = Utils.getSessionId(request);
		try {
			StpSession stpSession = supSessionService.queryStpSessionByTicket(sessionId);
			if (stpSession == null) {
				response.setResponseStatus(HttpResponseStatus.UNAUTHORIZED);
				return null;
			}
			List<ItineraryInfo> itineraryInfos = gaItineraryService.find(activityId);
			List<ItineraryResponse> itineraryResponses = new ArrayList<>(itineraryInfos.size());
			Utils.copyProperties(itineraryInfos, ItineraryResponse.class, itineraryResponses, "imageUrls");
			Iterator<ItineraryInfo> itineraryIterator = itineraryInfos.iterator();
			for (ItineraryResponse itineraryResponse : itineraryResponses) {
				ItineraryInfo itineraryInfo = itineraryIterator.next();
				itineraryResponse.setImageUrls(objectMapper.readValue(itineraryInfo.getImageUrls(), List.class));
			}
			response.setResponseStatus(HttpResponseStatus.OK);
			return itineraryResponses;
		} catch (Exception e) {
			LOGGER.error("Read itineraries failed", e);
			response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@Override
	public Object delete(Request request, Response response) {
		String activityId = request.getHeader("activityId");
		String sessionId = Utils.getSessionId(request);
		try {
			StpSession stpSession = supSessionService.queryStpSessionByTicket(sessionId);
			if (stpSession == null) {
				response.setResponseStatus(HttpResponseStatus.UNAUTHORIZED);
				return null;
			}
			gaItineraryService.delete(activityId);
			response.setResponseStatus(HttpResponseStatus.OK);
			return null;
		} catch (Exception e) {
			LOGGER.error("Read itineraries failed", e);
			response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

}
