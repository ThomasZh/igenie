package com.redoct.ga.web.talent.mvc.action;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.google.gson.Gson;
import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.talent.GaTalentService;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.web.talent.ApplicationContextProvider;
import com.redoct.ga.web.wechat.WechatUserSession;

@Controller
public class VoteActionController
{
	public static final String APP_ID = "wxaa328c83d3132bfb";
	public static final String APP_SECRET = "32bbf99a46d80b24bae81e8c8558c42f";
	public static final String DOMAIN = "planc2c.com";

	@RequestMapping(value = "/vote-action", method = RequestMethod.GET)
	public ModelAndView getPages(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		request.setCharacterEncoding("UTF-8");// ���ÿͻ�������������������"UTF-8"�ַ����
		response.setCharacterEncoding("UTF-8");// ���ý��ַ���"UTF-8"����������ͻ��������

		String ekey = request.getParameter("ekey");
		logger.debug("ekey: " + ekey);
		String code = request.getParameter("code");
		logger.debug("code: " + code);
		HttpSession session = request.getSession();
		boolean isValidCode = true;
		String serviceUrl = URLEncoder.encode("http://" + DOMAIN + request.getRequestURI(), "utf-8");
		String myAccountId = null;
		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();

		// ����Ƿ�����֤������֤�Ƿ�ͨ��
		if (code == null || code.equals("authdeny")) {
			isValidCode = false;
		}

		// �û�session������, ����ȡ����Ȩ���ض�����Ȩҳ��
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
				String headImgUrl = obj2.getString("headimgurl");
				logger.debug("nickname: " + nickname);
				logger.debug("headimgurl: " + headImgUrl);

				SupAccountService supAccountService = (SupAccountService) ctx.getBean("supAccountService");

				String desc = null;
				int currentTimestamp = DatetimeUtil.currentTimestamp();
				if (supAccountService.verifyExist(GlobalArgs.ACCOUNT_LOGIN_BY_WECHAT, unionid)) {
					logger.warn("This unionid(" + unionid + ") already exist!");

					AccountBasic account = supAccountService.queryAccount(GlobalArgs.ACCOUNT_LOGIN_BY_WECHAT, unionid);
					myAccountId = account.getAccountId();
					account.setNickname(nickname);
					account.setAvatarUrl(headImgUrl);
					account.setDesc(desc);
					supAccountService.modifyAccountBasicInfo(account, currentTimestamp);
				} else { // not exist
					myAccountId = supAccountService.createAccount(nickname, headImgUrl, desc, currentTimestamp);
					supAccountService.createLogin(myAccountId, GlobalArgs.ACCOUNT_LOGIN_BY_WECHAT, unionid,
							currentTimestamp);
					logger.info("accountId=[" + myAccountId + "]|nickname=[" + nickname + "]| register success)");
				}

				WechatUserSession user = new WechatUserSession();
				user.setAccountId(myAccountId);
				user.setUnionid(unionid);
				user.setNickname(nickname);
				user.setHeadimgurl(headImgUrl);
				session.setAttribute("user", user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (myAccountId == null) {
			WechatUserSession user = (WechatUserSession) session.getAttribute("user");
			if (user != null) {
				myAccountId = user.getAccountId();
			}
		}

		if (ekey != null && myAccountId != null) {
			int timestamp = DatetimeUtil.currentTimestamp();
			GaTalentService talentService = (GaTalentService) ctx.getBean("gaTalentService");
			if (!talentService.isVote(ekey, myAccountId)) {
				talentService.vote(ekey, myAccountId, timestamp);
			}
		} else {
			logger.warn("accountId(" + ekey + ") or voteAccountId(" + myAccountId + ") can't be null");
		}

		Map map = new HashMap();
		map.put("ekey", ekey);
		return new ModelAndView(new RedirectView("talent-profile.htm"), map);
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

	private final static Logger logger = LoggerFactory.getLogger(VoteActionController.class);
}
