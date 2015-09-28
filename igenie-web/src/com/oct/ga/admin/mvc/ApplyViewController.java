package com.oct.ga.admin.mvc;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

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
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.apply.GaApplicantTemplate;
import com.oct.ga.comm.domain.apply.GaApplicantTemplateCell;
import com.oct.ga.comm.domain.club.ActivityDetailInfo;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaApplyService;

@Controller
public class ApplyViewController
{
	public static final String APP_ID = "wxaa328c83d3132bfb";
	public static final String APP_SECRET = "32bbf99a46d80b24bae81e8c8558c42f";
	public static final String DOMAIN = "planc2c.com";

	@RequestMapping("/invite/apply")
	public ModelAndView getPages(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		request.setCharacterEncoding("UTF-8");// ���ÿͻ�������������������"UTF-8"�ַ����
		response.setCharacterEncoding("UTF-8");// ���ý��ַ���"UTF-8"����������ͻ��������

		String code = request.getParameter("code");
		logger.debug("code: " + code);
		HttpSession session = request.getSession();
		boolean isValidCode = true;
		String serviceUrl = URLEncoder.encode("http://" + DOMAIN + request.getRequestURI(), "utf-8");

		String activityId = request.getParameter("id");
		logger.debug("activityId: " + activityId);
		ModelAndView model = new ModelAndView();
		model.setViewName("/invite/apply");
		model.addObject("id", activityId);

		// ����Ƿ�����֤������֤�Ƿ�ͨ��
		if (code == null || code.equals("authdeny")) {
			isValidCode = false;
		}

		// �û�session������, ����ȡ����Ȩ���ض�����Ȩҳ��
		if ((!isValidCode) && session.getAttribute("unionid") == null) {
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

		// ����û�ͬ����Ȩ���ң��û�session�����ڣ�ͨ��OAUTH�ӿڵ��û�ȡ�û���Ϣ
		if (isValidCode && session.getAttribute("user") == null) {
			String str = getAccessToken(APP_ID, APP_SECRET, code);
			String utf8str = new String(str.getBytes("iso8859-1"), "utf-8");

			try {
				Gson gson = new Gson();
				JSONObject obj = gson.fromJson(utf8str, JSONObject.class);
				String token = obj.getString("access_token");
				String openid = obj.getString("openid");
				String unionid = obj.getString("unionid");
				logger.debug("unionid: " + unionid);

				String str2 = this.getUserInfo(token, openid);
				String utf8str2 = new String(str2.getBytes("iso8859-1"), "utf-8");
				JSONObject obj2 = gson.fromJson(utf8str2, JSONObject.class);
				String nickname = obj2.getString("nickname");
				String headimgurl = obj2.getString("headimgurl");
				logger.debug("nickname: " + nickname);
				logger.debug("headimgurl: " + headimgurl);

				WechatUserSession user = new WechatUserSession();
				user.setUnionid(unionid);
				user.setNickname(nickname);
				user.setHeadimgurl(headimgurl);

				session.setAttribute("user", user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		GaActivityService activityService = (GaActivityService) ctx.getBean("clubActivityService");
		GaApplyService applyService = (GaApplyService) ctx.getBean("gaApplyService");

		ActivityDetailInfo activityDetail = activityService.query(activityId, null);
		if (activityDetail.getApplyFormType() == GlobalArgs.FALSE) {
			return model;
		}

		GaApplicantTemplate template = applyService.queryApplicantTemplate(activityId);
		String contactJson = template.getContactJson();
		logger.debug("contactJson: " + contactJson);
		String participationJson = template.getParticipationJson();
		logger.debug("participationJson: " + participationJson);
		List<GaApplicantTemplateCell> contactCells = null;
		List<GaApplicantTemplateCell> participationCells = null;

		Gson gson = new Gson();
		if (contactJson != null && contactJson.length() > 0) {
			contactCells = gson.fromJson(contactJson, new TypeToken<List<GaApplicantTemplateCell>>()
			{
			}.getType());

			model.addObject("contactCells", contactCells);
		}
		if (participationJson != null && participationJson.length() > 0) {
			participationCells = gson.fromJson(participationJson, new TypeToken<List<GaApplicantTemplateCell>>()
			{
			}.getType());

			model.addObject("participationCells", participationCells);
			model.addObject("participationColumnNum", participationCells.size());
		}

		return model;
	}

	/**
	 * ��ȡ��Ȩ����
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
	 * ��ȡ�û���Ϣ
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

	private final static Logger logger = LoggerFactory.getLogger(ApplyViewController.class);

}
