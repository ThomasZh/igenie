package com.oct.ga.admin.mvc;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.comm.domain.club.ActivityDetailInfo;
import com.oct.ga.comm.domain.desc.GaDescCell;
import com.oct.ga.comm.domain.desc.GaDescChapter;
import com.oct.ga.invite.domain.GaInviteMasterInfo;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaDescService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaInviteService;
import com.oct.ga.service.GaMomentService;
import com.redoct.ga.sup.account.SupAccountService;

@Controller
public class ActivityViewController
{
	@RequestMapping("/invite/activity")
	public ModelAndView login(HttpServletRequest request)
			throws UnsupportedEncodingException
	{
		request.setCharacterEncoding("UTF-8");// ���ÿͻ�������������������"UTF-8"�ַ����

		String inviteId = request.getParameter("id");
		logger.debug("inviteId: " + inviteId);

		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		GaInviteService inviteService = (GaInviteService) ctx.getBean("gaInviteService");
		GaActivityService activityService = (GaActivityService) ctx.getBean("clubActivityService");
		GaGroupService groupService = (GaGroupService) ctx.getBean("gaGroupService");
		SupAccountService accountService = (SupAccountService) ctx.getBean("supAccountService");
		GaMomentService momentService = (GaMomentService) ctx.getBean("gaMomentService");
		GaDescService descService = (GaDescService) ctx.getBean("gaDescService");

		GaInviteMasterInfo invite = inviteService.queryMaster(inviteId);
		ModelAndView model = new ModelAndView();
		model.setViewName("/invite/activity");
		model.addObject("ekey", inviteId);

		if (invite.getInviteId() != null && invite.getInviteId().length() > 0) { // exist
			model.addObject("rs", "ok");

			try {
				String activityId = invite.getChannelId();
				model.addObject("id", activityId);
				ActivityDetailInfo activityDetail = activityService.query(activityId, null);

				short applyFormType = activityDetail.getApplyFormType();
				model.addObject("applyFormType", applyFormType);

				String leaderId = groupService.queryLeaderId(activityId);
				// id, nickname, imageUrl
				AccountMaster leaderInfo = accountService.queryAccountMaster(leaderId);
				model.addObject("leaderId", leaderId);
				model.addObject("leaderName", leaderInfo.getNickname());
				model.addObject("leaderImageUrl", leaderInfo.getAvatarUrl());
				logger.debug("leaderId: " + leaderId);
				logger.debug("leaderName: " + leaderInfo.getNickname());
				logger.debug("leaderImageUrl: " + leaderInfo.getAvatarUrl());

				model.addObject("activityId", activityId);
				model.addObject("activityName", activityDetail.getName());
				model.addObject("activityDesc", activityDetail.getDesc());
				model.addObject("startTime", DatetimeUtil.time2Str(activityDetail.getStartTime()));
				model.addObject("endTime", DatetimeUtil.time2Str(activityDetail.getEndTime()));
				model.addObject("locDesc", activityDetail.getLocDesc());
				logger.debug("activityId: " + activityId);
				logger.debug("activityName: " + leaderId);
				logger.debug("activityDesc: " + leaderId);
				logger.debug("startTime: " + leaderId);
				logger.debug("endTime: " + leaderId);
				logger.debug("locDesc: " + leaderId);

				List<GaDescChapter> descChapters = descService.query(activityId);
				for (GaDescChapter descChapter : descChapters) {
					logger.debug("seq: " + descChapter.getSeq());
					logger.debug("title: " + descChapter.getTitle());

					List<GaDescCell> cells = descChapter.getCells();
					for (GaDescCell cell : cells) {
						logger.debug("seq: " + cell.getSeq());
						logger.debug("type: " + cell.getType());
						logger.debug("txt: " + cell.getTxt());
					}
				}
				model.addObject("descs", descChapters);
			} catch (Exception e) {
				logger.error(e.getMessage());
				model.addObject("rs", "notExist");
			}
		} else {
			model.addObject("rs", "notExist");
		}

		return model;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActivityViewController.class);
}
