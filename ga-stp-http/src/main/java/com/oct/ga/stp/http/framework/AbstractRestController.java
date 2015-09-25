package com.oct.ga.stp.http.framework;

import java.util.Collections;
import java.util.List;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.exception.MethodNotAllowedException;

import io.netty.handler.codec.http.HttpMethod;

public abstract class AbstractRestController {
	protected List<HttpMethod> allowMethods = Collections.emptyList();

	public Object create(Request request, Response response) {
		throw new MethodNotAllowedException(allowMethods);
	}

	public Object read(Request request, Response response) {
		throw new MethodNotAllowedException(allowMethods);
	}

	public Object update(Request request, Response response) {
		throw new MethodNotAllowedException(allowMethods);
	}

	public Object delete(Request request, Response response) {
		throw new MethodNotAllowedException(allowMethods);
	}
}
