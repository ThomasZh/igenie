package com.oct.ga.stp.http.framework;

import org.restexpress.Request;
import org.restexpress.Response;

import io.netty.handler.codec.http.HttpResponseStatus;

public abstract class AbstractRestController {
	public Object create(Request request, Response response) {
		response.setResponseStatus(HttpResponseStatus.NOT_FOUND);
		return null;
	}

	public Object read(Request request, Response response) {
		response.setResponseStatus(HttpResponseStatus.NOT_FOUND);
		return null;
	}

	public Object update(Request request, Response response) {
		response.setResponseStatus(HttpResponseStatus.NOT_FOUND);
		return null;
	}

	public Object delete(Request request, Response response) {
		response.setResponseStatus(HttpResponseStatus.NOT_FOUND);
		return null;
	}
}
