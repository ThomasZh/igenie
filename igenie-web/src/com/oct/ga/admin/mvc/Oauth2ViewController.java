package com.oct.ga.admin.mvc;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
public class Oauth2ViewController
{
	public static final String APP_ID = "wxaa328c83d3132bfb";
	public static final String APP_SECRET = "32bbf99a46d80b24bae81e8c8558c42f";
	public static final String DOMAIN = "planc2c.com";

	@RequestMapping("/invite/oauth2")
	public ModelAndView getPages(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		String code = request.getParameter("code");
		logger.debug("code: " + code);
		HttpSession session = request.getSession();
		boolean isValidCode = true;
		String serviceUrl = URLEncoder.encode("http://" + DOMAIN + request.getRequestURI(), "utf-8");

		ModelAndView model = new ModelAndView();
		model.setViewName("/invite/oauth2");
		model.addObject("code", code);

		// 检查是否已验证或者验证是否通过
		if (code == null || code.equals("authdeny")) {
			isValidCode = false;
		}

		// 用户session不存在, 并且取消授权，重定向到授权页面
		if ((!isValidCode) && session.getAttribute("user") == null) {
			StringBuilder oauth_url = new StringBuilder();
			oauth_url.append("https://open.weixin.qq.com/connect/oauth2/authorize?");
			oauth_url.append("appid=").append(APP_ID);
			oauth_url.append("&redirect_uri=").append(serviceUrl);
			oauth_url.append("&response_type=code");
			oauth_url.append("&scope=snsapi_userinfo");
			oauth_url.append("&state=1#wechat_redirect");
			response.sendRedirect(oauth_url.toString());

			return null;
		}

		// 如果用户同意授权并且，用户session不存在，通过OAUTH接口调用获取用户信息
		if (isValidCode && session.getAttribute("user") == null) {
			String accessToken = getAccessToken(APP_ID, APP_SECRET, code);
			model.addObject("accessToken", accessToken);

			try {
				Gson gson = new Gson();
				JSONObject obj = gson.fromJson(accessToken, JSONObject.class);
				String unionid = obj.getString("unionid");
				model.addObject("unionid", unionid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return model;
	}

	/**
	 * 获取授权令牌
	 */
	public String getAccessToken(String appid, String secret, String code)
			throws IOException
	{
		StringBuilder url = new StringBuilder();
		url.append("https://api.weixin.qq.com/sns/oauth2/access_token?");
		url.append("appid=" + appid);
		url.append("&secret=").append(secret);
		url.append("&code=").append(code);
		url.append("&grant_type=authorization_code");

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url.toString());
		// Create a custom response handler
		ResponseHandler<String> responseHandler = new ResponseHandler<String>()
		{
			public String handleResponse(final HttpResponse response)
					throws ClientProtocolException, IOException
			{
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}

		};
		String responseBody = httpclient.execute(httpget, responseHandler);

		return responseBody;
	}

	/**
	 * 获取用户信息
	 */
	public String getUserInfo(String token, String openid)
			throws IOException
	{
		StringBuilder url = new StringBuilder();
		url.append("https://api.weixin.qq.com/sns/userinfo?");
		url.append("access_token=" + token);
		url.append("&openid=").append(openid);
		url.append("&lang=zh_CN");

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url.toString());
		// Create a custom response handler
		ResponseHandler<String> responseHandler = new ResponseHandler<String>()
		{
			public String handleResponse(final HttpResponse response)
					throws ClientProtocolException, IOException
			{
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}

		};
		String responseBody = httpclient.execute(httpget, responseHandler);

		return responseBody;
	}

	private final static Logger logger = LoggerFactory.getLogger(Oauth2ViewController.class);
}
