package com.oct.ga.stp.http.activity.controller;

import java.util.ArrayList;
import java.util.List;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.exception.BadRequestException;
import org.restexpress.exception.ServiceException;
import org.restexpress.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oct.ga.activity.domain.ActivityConstants;
import com.oct.ga.activity.domain.DayItinerary;
import com.oct.ga.activity.domain.EquipmentItinerary;
import com.oct.ga.activity.domain.GeoItinerary;
import com.oct.ga.activity.domain.Itinerary;
import com.oct.ga.activity.domain.RouteItinerary;
import com.oct.ga.activity.service.ItineraryService;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.stp.http.activity.ItineraryRequest;
import com.oct.ga.stp.http.activity.ItineraryResponse;
import com.oct.ga.stp.http.framework.AbstractRestController;
import com.oct.ga.stp.http.framework.Controller;
import com.oct.ga.stp.http.framework.Utils;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.domain.StpSession;

@Component
@Controller
public class ItineraryController extends AbstractRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItineraryController.class);
	@Autowired
	private SupSessionService supSessionService;
	@Autowired
	private ItineraryService itineraryService;

	@Override
	public Object read(Request request, Response response) {
		String activityId = request.getHeader("activityId");
		int idx = Utils.requireHeaderAsInt(request, "idx");
		int pageSize = Utils.requireHeaderAsInt(request, "pageSize");

		try {
			List<Itinerary> itineraries = itineraryService.findByActivityId(activityId, idx, pageSize);
			List<ItineraryResponse> itineraryResponses = new ArrayList<>(itineraries.size());
			for (Itinerary itinerary : itineraries) {
				ItineraryResponse itineraryResponse = new ItineraryResponse();
				Utils.copyProperties(itinerary, itineraryResponse);
				itineraryResponses.add(itineraryResponse);
			}
			return itineraryResponses;
		} catch (RuntimeException e) {
			LOGGER.error("Read itinerary failed", e);
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
		ItineraryRequest[] itineraryRequests = request.getBodyAs(ItineraryRequest[].class);
		List<Itinerary> itineraries = new ArrayList<>(itineraryRequests.length);
		for (int i = 0; i < itineraryRequests.length; i++) {
			ItineraryRequest itineraryRequest = itineraryRequests[i];
			Itinerary itinerary;
			int type = itineraryRequest.getType();
			switch (type) {
			case ActivityConstants.ITINERARY_TYPE_GEO:
				itinerary = new GeoItinerary();
				break;
			case ActivityConstants.ITINERARY_TYPE_ROUTE:
				itinerary = new RouteItinerary();
				break;
			case ActivityConstants.ITINERARY_TYPE_DAY:
				itinerary = new DayItinerary();
				break;
			case ActivityConstants.ITINERARY_TYPE_EQUIPMENT:
				itinerary = new EquipmentItinerary();
				break;
			default:
				LOGGER.error("Illegal type: {}, [activityId = {}, itinerary = {}]", type, activityId, itineraryRequest);
				throw new BadRequestException();
			}
			Utils.copyProperties(itineraryRequest, itinerary);
			itineraries.add(itinerary);
		}
		try {
			itineraryService.overwriteByActivityId(activityId, itineraries);
		} catch (RuntimeException e) {
			LOGGER.error("update itinerary failed", e);
			throw new ServiceException(e);
		}
		return null;
	}

}
