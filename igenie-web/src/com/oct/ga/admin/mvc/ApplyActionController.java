package com.oct.ga.admin.mvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.apply.GaApplicantCell;
import com.oct.ga.comm.domain.apply.GaApplicantInfo;
import com.oct.ga.comm.domain.apply.GaApplicantTemplate;
import com.oct.ga.comm.domain.apply.GaApplicantTemplateCell;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaApplyService;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaFollowingService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.redoct.ga.sup.account.SupAccountService;

@Controller
public class ApplyActionController
{
	@RequestMapping("/invite/applyAction")
	public ModelAndView getPages(HttpServletRequest request)
			throws IOException
	{
		request.setCharacterEncoding("UTF-8");// ���ÿͻ�������������������"UTF-8"�ַ����

		String activityId = request.getParameter("hidden_id");
		logger.debug("activityId: " + activityId);
		String strColumnNum = request.getParameter("hidden_participationColumnNum");
		int columns = Integer.parseInt(strColumnNum);
		logger.debug("columns: " + columns);
		String strRowNum = request.getParameter("hidden_participationRowNum");
		int rows = Integer.parseInt(strRowNum);
		logger.debug("rows: " + rows);
		String strRowArray = request.getParameter("hidden_participationRowArray");
		logger.debug("strRowArray: " + strRowArray);
		String[] rowArray = strRowArray.split(",");

		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		SupAccountService accountService = (SupAccountService) ctx.getBean("supAccountService");
		GaApplyService applyService = (GaApplyService) ctx.getBean("gaApplyService");
		GaActivityService activityService = (GaActivityService) ctx.getBean("clubActivityService");
		GaGroupService groupService = (GaGroupService) ctx.getBean("gaGroupService");
		GaBadgeNumService badgeNumService = (GaBadgeNumService) ctx.getBean("gaBadgeNumService");
		GaSyncVerService syncVerService = (GaSyncVerService) ctx.getBean("gaSyncVerService");
		GaFollowingService followingService = (GaFollowingService) ctx.getBean("gaFollowingService");

		GaApplicantTemplate template = applyService.queryApplicantTemplate(activityId);
		String contactJson = template.getContactJson();
		String participationJson = template.getParticipationJson();
		List<GaApplicantTemplateCell> templateContactCells = null;
		List<GaApplicantTemplateCell> templateParticipationCells = null;

		Gson gson = new Gson();
		if (contactJson != null && contactJson.length() > 0) {
			templateContactCells = gson.fromJson(contactJson, new TypeToken<List<GaApplicantTemplateCell>>()
			{
			}.getType());
		}
		if (participationJson != null && participationJson.length() > 0) {
			templateParticipationCells = gson.fromJson(participationJson,
					new TypeToken<List<GaApplicantTemplateCell>>()
					{
					}.getType());
		}

		int currentTimestamp = DatetimeUtil.currentTimestamp();
		List<GaApplicantCell> contactInfo = new ArrayList<GaApplicantCell>();
		for (int i = 0; i < templateContactCells.size(); i++) {
			String val = request.getParameter("inputContact_" + (i + 1));
			logger.debug("inputContact_" + (i + 1) + ": " + val);
			String utf8val = null;
			if (val != null)
				utf8val = new String(val.getBytes("iso8859-1"), "utf-8");

			GaApplicantCell cell = new GaApplicantCell();
			cell.setSeq(i + 1);
			cell.setName(templateContactCells.get(i).getName());
			cell.setVal(utf8val);

			contactInfo.add(cell);
		}

		List<GaApplicantInfo> participationInfos = new ArrayList<GaApplicantInfo>();
		for (int i = 0; i < rows; i++) {
			GaApplicantInfo participationInfo = new GaApplicantInfo();
			List<GaApplicantCell> participationCells = new ArrayList<GaApplicantCell>();
			for (int k = 0; k < columns; k++) {
				GaApplicantCell cell = new GaApplicantCell();

				String name = templateParticipationCells.get(k).getName();
				String val = request.getParameter("inputParticipation_" + (i + 1) + "_" + (k + 1));
				logger.debug("inputParticipation_" + (i + 1) + "_" + (k + 1) + ": " + val);
				String utf8val = null;
				if (val != null)
					utf8val = new String(val.getBytes("iso8859-1"), "utf-8");
				logger.debug("iso8859-1: " + utf8val);

				cell.setSeq(k + 1);
				cell.setName(name);
				cell.setVal(utf8val);

				participationCells.add(cell);
			}

			participationInfo.setApplicant(participationCells);
			participationInfo.setSeq(i + 1);
			participationInfos.add(participationInfo);
		}

		String myAccountId = GlobalArgs.ID_DEFAULT_NONE;
		HttpSession session = request.getSession();
		WechatUserSession user = (WechatUserSession) session.getAttribute("user");
		if (user != null) {
			String unionid = user.getUnionid();
			String nickname = user.getNickname();
			String avatarUrl = user.getHeadimgurl();
			logger.debug("unionid: " + unionid);
			logger.debug("nickname: " + nickname);
			logger.debug("avatarUrl: " + avatarUrl);

			try {
				// LOGIC: account is exist?
				if (accountService.verifyExist(GlobalArgs.ACCOUNT_LOGIN_BY_WECHAT, unionid)) {
					AccountBasic accountBase = accountService.queryAccount(GlobalArgs.ACCOUNT_LOGIN_BY_WECHAT, unionid);
					myAccountId = accountBase.getAccountId();

					logger.warn("sessionId=[" + session.getId() + "]|accountId=[" + myAccountId + "]|commandTag=["
							+ Command.UPLOAD_APPLICANTS_REQ + "]|ErrorCode=[" + ErrorCode.REGISTER_EMAIL_EXIST
							+ "]|This unionid(" + unionid + ") already exist!");
				} else { // not exist
					myAccountId = accountService.createAccount(nickname, avatarUrl, "", currentTimestamp);
					accountService.createLogin(myAccountId, GlobalArgs.ACCOUNT_LOGIN_BY_WECHAT, unionid,
							currentTimestamp);
					logger.info("sessionId=[" + session.getId() + "]|deviceId=[wechat bowser]|accountId=["
							+ myAccountId + "]|commandTag=[" + Command.UPLOAD_APPLICANTS_REQ + "]|user=[" + nickname
							+ "]| register success)");
				}
			} catch (Exception e) {
				logger.error(LogErrorMessage.getFullInfo(e));
			}

			if (groupService.isActive(activityId)) {
				String leaderId = groupService.queryLeaderId(activityId);

				// apply
				if (activityService.queryApproveType(activityId) == GlobalArgs.TRUE) {
					groupService.applyWaitJoin(activityId, myAccountId, currentTimestamp, leaderId);
					applyService.modify(myAccountId, leaderId, activityId, GlobalArgs.INVITE_STATE_APPLY, null,
							currentTimestamp);
				} else {
					groupService.joinAsMember(activityId, myAccountId, currentTimestamp);
					applyService.modify(myAccountId, leaderId, activityId, GlobalArgs.INVITE_STATE_JOIN, null,
							currentTimestamp);

					// Logic: follow to each other.
					followingService.follow(leaderId, myAccountId, currentTimestamp);
					followingService.follow(myAccountId, leaderId, currentTimestamp);
				}

				// if add/remove member, task info version must increase.
				syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
						myAccountId, Command.UPLOAD_APPLICANTS_REQ);
				syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_MEMBER, currentTimestamp,
						myAccountId, Command.UPLOAD_APPLICANTS_REQ);

				short applyNum = badgeNumService.countApplyNum(leaderId);
				badgeNumService.modifyApplyNum(leaderId, applyNum);

				// Logic: add contactInfoJson & applicantInfos into db
				if (activityService.queryApplyFormType(activityId) == GlobalArgs.TRUE) {
					String contactInfoJson = gson.toJson(contactInfo);
					applyService.modifyApplicantContact(activityId, myAccountId, contactInfoJson, currentTimestamp);

					applyService.removeAllApplicant(activityId, myAccountId);

					for (GaApplicantInfo applicantInfo : participationInfos) {
						String json = gson.toJson(applicantInfo.getApplicant());
						logger.debug("json: " + json);
						applyService.addApplicant(activityId, myAccountId, applicantInfo.getSeq(), json,
								currentTimestamp);
					}
				}
			}
		} else { // no unionid
			;
		}

		ModelAndView model = new ModelAndView();
		model.setViewName("/invite/applySuccess");

		return model;
	}

	private final static Logger logger = LoggerFactory.getLogger(ApplyActionController.class);
}
