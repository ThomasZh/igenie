package com.oct.ga.integration;

import java.io.IOException;
import java.lang.reflect.Member;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.http.client.utils.HttpClientUtils;

import com.google.gson.Gson;

public class WechatOAuthAPI
{
	public static final String APP_ID = "wxaa328c83d3132bfb";
	public static final String APP_SECRET = "32bbf99a46d80b24bae81e8c8558c42f";
	public static final String DOMAIN = "planc2c.com";

	public static void OAuthIfNesscary(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		String code = request.getParameter("code");
		HttpSession session = request.getSession();
		boolean isValidCode = true;
		String serviceUrl = URLEncoder.encode("http://" + DOMAIN + request.getRequestURI(), "utf-8");

		// 检查是否已验证或者验证是否通过
		if (code == null || code.equals("authdeny")) {
			isValidCode = false;
		}

		// 如果session未空或者取消授权，重定向到授权页面
		if ((!isValidCode) && session.getAttribute("user") == null) {
			StringBuilder oauth_url = new StringBuilder();
			oauth_url.append("https://open.weixin.qq.com/connect/oauth2/authorize?");
			oauth_url.append("appid=").append(APP_ID);
			oauth_url.append("&redirect_uri=").append(serviceUrl);
			oauth_url.append("&response_type=code");
			oauth_url.append("&scope=snsapi_userinfo");
			oauth_url.append("&state=1#wechat_redirect");
			response.sendRedirect(oauth_url.toString());
			return;
		}

		// 如果用户同意授权并且，用户session不存在，通过OAUTH接口调用获取用户信息
		if (isValidCode && session.getAttribute("user") == null) {
			//Member member = null;
			JSONObject obj = WechatOAuthAPI.getAccessToken(WechatOAuthAPI.APP_ID, WechatOAuthAPI.APP_SECRET, code);
			String token = obj.getString("access_token");
			String openid = obj.getString("openid");
			JSONObject user = WechatOAuthAPI.getUserInfo(token, openid);
			//MemberService memberService = (MemberService) WebAppContext.getObject("memberService");
			//member = memberService.saveOrUpdateIfNesscary(user);
			session.setAttribute("user", user);
		}
	}

	/**
	 * 获取授权令牌
	 * */
	public static JSONObject getAccessToken(String appid, String secret, String code)
	{
		StringBuilder url = new StringBuilder();
		url.append("https://api.weixin.qq.com/sns/oauth2/access_token?");
		url.append("appid=" + appid);
		url.append("&secret=").append(secret);
		url.append("&code=").append(code);
		url.append("&grant_type=authorization_code");
		
		Gson gson = new Gson();
		return gson.fromJson(url.toString(), JSONObject.class);
		// return HttpClientUtils.getJson(url.toString());
	}

	// 获取用户信息
	public static JSONObject getUserInfo(String token, String openid)
	{
		StringBuilder url = new StringBuilder();
		url.append("https://api.weixin.qq.com/sns/userinfo?");
		url.append("access_token=" + token);
		url.append("&openid=").append(openid);
		url.append("&lang=zh_CN");
		
		Gson gson = new Gson();
		return gson.fromJson(url.toString(), JSONObject.class);
		//return HttpClientUtils.getJson(url.toString());
	}
}
