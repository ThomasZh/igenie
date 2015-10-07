package com.oct.ga.stp.http.framework;

import java.util.Collection;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.exception.BadRequestException;
import org.restexpress.exception.UnauthorizedException;
import org.springframework.beans.BeanUtils;

import com.oct.ga.stp.http.common.Header;

public class Utils {
	public static int getCurrentTimeSeconds() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	public static void addErrorCode(short errorCode, Response response) {
		response.addHeader(Header.HTTP_HEADER_ERROR_CODE, Short.toString(errorCode));
	}

	public static String getSessionId(Request request) {
		return request.getHeader(Header.HTTP_HEADER_SESSION_ID);
	}

	public static String requireSessionId(Request request) {
		String sessionId = getSessionId(request);
		if (sessionId == null || sessionId.trim().length() == 0) {
			throw new UnauthorizedException();
		}
		return sessionId;
	}

	public static long getHeaderAsLong(Request request, String name, long defaultValue) {
		String value = request.getHeader(name);
		if (value == null || value.trim().length() == 0) {
			return defaultValue;
		} else {
			return requireHeaderAsLong(request, name);
		}
	}

	public static long requireHeaderAsLong(Request request, String name) {
		try {
			return Long.parseLong(request.getHeader(name));
		} catch (NumberFormatException e) {
			throw new BadRequestException(e);
		}
	}

	public static int getHeaderAsInt(Request request, String name, int defaultValue) {
		String value = request.getHeader(name);
		if (value == null || value.trim().length() == 0) {
			return defaultValue;
		} else {
			return requireHeaderAsInt(request, name);
		}
	}

	public static int requireHeaderAsInt(Request request, String name) {
		try {
			return Integer.parseInt(request.getHeader(name));
		} catch (NumberFormatException e) {
			throw new BadRequestException(e);
		}
	}

	public static boolean getHeaderAsBoolean(Request request, String name, boolean defaultValue) {
		String value = request.getHeader(name);
		if (value == null || value.trim().length() == 0) {
			return defaultValue;
		} else {
			return requireHeaderAsBoolean(request, name);
		}
	}

	public static boolean requireHeaderAsBoolean(Request request, String name) {
		String value = request.getHeader(name);
		if ("true".equalsIgnoreCase(value)) {
			return true;
		}
		if ("false".equalsIgnoreCase(value)) {
			return false;
		}
		throw new BadRequestException();
	}

	public static void copyProperties(Object source, Object target, String... ignoreProperties) {
		BeanUtils.copyProperties(source, target, ignoreProperties);
	}

	public static <T> void copyProperties(Collection<?> source, Class<T> targetType, Collection<T> target,
			String... ignoreProperties) {
		for (Object sourceEle : source) {
			try {
				T t = targetType.newInstance();
				BeanUtils.copyProperties(sourceEle, t, ignoreProperties);
				target.add(t);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
