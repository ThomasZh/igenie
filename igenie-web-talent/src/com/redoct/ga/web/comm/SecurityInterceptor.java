package com.redoct.ga.web.comm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SecurityInterceptor
		implements HandlerInterceptor
{
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception
	{
		// excluded URLs:
		// see
		// http://stackoverflow.com/questions/9908124/spring-mvc-3-interceptor-on-all-excluding-some-defined-paths
		String requestUri = request.getRequestURI();
		//System.out.println("requestUri: " + requestUri);
		for (String url : excludedUrls) {
			if (requestUri.endsWith(url)) {
				//System.out.println("excludedUrl: " + requestUri);
				return true;
			}
		}

		// intercept
		HttpSession session = request.getSession();
		if (session.getAttribute("user") == null) {
			// see
			// http://stackoverflow.com/questions/12713873/spring-3-1-how-do-you-send-all-exception-to-one-page
			throw new AuthorizationException();
		} else {
			return true;
		}
	}

	private List<String> excludedUrls;

	public void setExcludedUrls(List<String> excludedUrls)
	{
		this.excludedUrls = excludedUrls;
	}

}
