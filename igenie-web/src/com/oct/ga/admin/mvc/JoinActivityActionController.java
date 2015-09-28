package com.oct.ga.admin.mvc;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

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
import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.gatekeeper.SupServerInfo;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaApplyService;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaFollowingService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.client.socket.SupSocketConnectionManager;
import com.redoct.ga.sup.message.SupMessageService;

@Controller
public class JoinActivityActionController
{
	public static final String APP_ID = "wxaa328c83d3132bfb";
	public static final String APP_SECRET = "32bbf99a46d80b24bae81e8c8558c42f";
	public static final String DOMAIN = "planc2c.com";

	@RequestMapping("/invite/join")
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
		model.setViewName("/invite/joinSuccess");

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

				String myAccountId = null;
				int currentTimestamp = DatetimeUtil.currentTimestamp();
				ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
				SupAccountService accountService = (SupAccountService) ctx.getBean("supAccountService");
				GaGroupService groupService = (GaGroupService) ctx.getBean("gaGroupService");
				GaBadgeNumService badgeNumService = (GaBadgeNumService) ctx.getBean("gaBadgeNumService");
				GaSyncVerService syncVerService = (GaSyncVerService) ctx.getBean("gaSyncVerService");
				GaFollowingService followingService = (GaFollowingService) ctx.getBean("gaFollowingService");
				GaApplyService applyService = (GaApplyService) ctx.getBean("gaApplyService");
				GaActivityService activityService = (GaActivityService) ctx.getBean("clubActivityService");
				GaTaskService taskService = (GaTaskService) ctx.getBean("gaTaskService");

				if (accountService.verifyExist(GlobalArgs.ACCOUNT_LOGIN_BY_WECHAT, unionid)) {
					logger.warn("sessionId=[" + session.getId() + "]|This unionid(" + unionid + ") already exist!");
					AccountBasic account = accountService.queryAccount(GlobalArgs.ACCOUNT_LOGIN_BY_WECHAT, unionid);
					myAccountId = account.getAccountId();
					logger.info("sessionId=[" + session.getId() + "]|accountId=[" + myAccountId + "]|nickname=["
							+ nickname + "]| login success)");
				} else {
					myAccountId = accountService.createAccount(nickname, headimgurl, "", currentTimestamp);
					accountService.createLogin(myAccountId, GlobalArgs.ACCOUNT_LOGIN_BY_WECHAT, unionid,
							currentTimestamp);
					logger.info("sessionId=[" + session.getId() + "]|accountId=[" + myAccountId + "]|nickname=["
							+ nickname + "]| register success)");
				}

				if (groupService.isActive(activityId)) {
					String leaderId = groupService.queryLeaderId(activityId);

					// apply
					if (activityService.queryApproveType(activityId) == GlobalArgs.TRUE) {
						groupService.applyWaitJoin(activityId, myAccountId, currentTimestamp, leaderId);
						short action = GlobalArgs.INVITE_STATE_APPLY;
						applyService.modify(myAccountId, leaderId, activityId, action, null, currentTimestamp);

						GaTaskLog log = new GaTaskLog();
						log.setLogId(UUID.randomUUID().toString());
						log.setChannelId(activityId);
						log.setFromAccountId(myAccountId);
						log.setActionTag(action);
						log.setToActionId(activityId);
						taskService.addLog(log, currentTimestamp);

						short syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
						taskService.addLogExtend(log.getLogId(), leaderId, activityId, action, syncState,
								currentTimestamp);
						syncState = GlobalArgs.SYNC_STATE_RECEIVED;
						taskService.addLogExtend(log.getLogId(), myAccountId, activityId, action, syncState,
								currentTimestamp);

						String groupName = groupService.queryGroupName(activityId);
						// send this apply message to online leader
						try {
							SupMessageService supMessageService = (SupMessageService) ctx.getBean("supMessageService");

							MsgFlowBasicInfo msgFlowBasicInfo = new MsgFlowBasicInfo();
							msgFlowBasicInfo.setLogId(log.getLogId());
							msgFlowBasicInfo.setFromAccountId(myAccountId);
							msgFlowBasicInfo.setFromAccountName(nickname);
							msgFlowBasicInfo.setFromAccountAvatarUrl(headimgurl);
							msgFlowBasicInfo.setToActionAccountId(leaderId);
							msgFlowBasicInfo.setToActionId(log.getChannelId());
							msgFlowBasicInfo.setActionTag(log.getActionTag());
							msgFlowBasicInfo.setChannelId(log.getChannelId());
							msgFlowBasicInfo.setChannelName(groupName);

							SupServerInfo server = new SupServerInfo();
							server.setId("68df84ba-9ec0-4df5-b62d-dc57d997529f");
							server.setIp("siriusa");
							server.setPort(15106);
							server.setType(GlobalArgs.SUP_TYPE_MESSAGE_SERVER);
							server.setActive(true);

							SupSocketConnectionManager socketConnectionManager = GenericSingleton
									.getInstance(SupSocketConnectionManager.class);
							socketConnectionManager.add(server);

							supMessageService.sendMsgFlow(msgFlowBasicInfo, currentTimestamp);
						} catch (Exception e) {
							logger.error("send apply notify message error: " + LogErrorMessage.getFullInfo(e));
						}

						model.addObject("approved", "false");
					} else { // join
						groupService.joinAsMember(activityId, myAccountId, currentTimestamp);
						short action = GlobalArgs.INVITE_STATE_JOIN;
						applyService.modify(myAccountId, leaderId, activityId, action, null, currentTimestamp);

						// Logic: follow to each other.
						followingService.follow(leaderId, myAccountId, currentTimestamp);
						followingService.follow(myAccountId, leaderId, currentTimestamp);

						GaTaskLog log = new GaTaskLog();
						log.setLogId(UUID.randomUUID().toString());
						log.setChannelId(activityId);
						log.setFromAccountId(myAccountId);
						log.setActionTag(action);
						log.setToActionId(activityId);
						taskService.addLog(log, currentTimestamp);

						short syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
						taskService.addLogExtend(log.getLogId(), leaderId, activityId, action, syncState,
								currentTimestamp);
						syncState = GlobalArgs.SYNC_STATE_RECEIVED;
						taskService.addLogExtend(log.getLogId(), myAccountId, activityId, action, syncState,
								currentTimestamp);

						String groupName = groupService.queryGroupName(activityId);
						// send this apply message to online leader
						try {
							SupMessageService supMessageService = (SupMessageService) ctx.getBean("supMessageService");

							MsgFlowBasicInfo msgFlowBasicInfo = new MsgFlowBasicInfo();
							msgFlowBasicInfo.setLogId(log.getLogId());
							msgFlowBasicInfo.setFromAccountId(myAccountId);
							msgFlowBasicInfo.setFromAccountName(nickname);
							msgFlowBasicInfo.setFromAccountAvatarUrl(headimgurl);
							msgFlowBasicInfo.setToActionAccountId(leaderId);
							msgFlowBasicInfo.setToActionId(log.getChannelId());
							msgFlowBasicInfo.setActionTag(log.getActionTag());
							msgFlowBasicInfo.setChannelId(log.getChannelId());
							msgFlowBasicInfo.setChannelName(groupName);

							SupServerInfo server = new SupServerInfo();
							server.setId("68df84ba-9ec0-4df5-b62d-dc57d997529f");
							server.setIp("siriusa");
							server.setPort(15106);
							server.setType(GlobalArgs.SUP_TYPE_MESSAGE_SERVER);
							server.setActive(true);

							SupSocketConnectionManager socketConnectionManager = GenericSingleton
									.getInstance(SupSocketConnectionManager.class);
							socketConnectionManager.add(server);

							supMessageService.sendMsgFlow(msgFlowBasicInfo, currentTimestamp);
						} catch (Exception e) {
							logger.error("send apply notify message error: " + LogErrorMessage.getFullInfo(e));
						}

						model.addObject("approved", "true");
					}

					// if add/remove member, task info version must increase.
					syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
							myAccountId, Command.UPLOAD_APPLICANTS_REQ);
					syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_MEMBER, currentTimestamp,
							myAccountId, Command.UPLOAD_APPLICANTS_REQ);

					short applyNum = badgeNumService.countApplyNum(leaderId);
					badgeNumService.modifyApplyNum(leaderId, applyNum);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	private final static Logger logger = LoggerFactory.getLogger(JoinActivityActionController.class);
}
