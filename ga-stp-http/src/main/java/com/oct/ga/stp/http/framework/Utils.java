package com.oct.ga.stp.http.framework;

import java.util.Collection;

import org.restexpress.Request;
import org.restexpress.Response;
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
