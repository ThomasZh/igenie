package com.redoct.ga.web.talent.mvc.view;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.talent.TalentInfo;
import com.oct.ga.comm.domain.talent.TalentScore;
import com.oct.ga.talent.GaTalentService;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.web.talent.ApplicationContextProvider;
import com.redoct.ga.web.wechat.WechatUserSession;

@Controller
public class TalentProfileViewController
{
	@RequestMapping(value = "/talent-profile", method = RequestMethod.GET)
	public ModelAndView getPages(HttpServletRequest request)
			throws IOException, InterruptedException
	{
		request.setCharacterEncoding("UTF-8");// ���ÿͻ�������������������"UTF-8"�ַ����

		String ekey = request.getParameter("ekey");
		logger.debug("ekey: " + ekey);

		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		SupAccountService supAccountService = (SupAccountService) ctx.getBean("supAccountService");
		GaTalentService talentService = (GaTalentService) ctx.getBean("gaTalentService");

		AccountBasic account = supAccountService.queryAccount(ekey);

		ModelAndView model = new ModelAndView("talent-profile");
		model.addObject("ekey", ekey);
		model.addObject("nickname", account.getNickname());
		model.addObject("avatarUrl", account.getAvatarUrl());

		HttpSession session = request.getSession();
		WechatUserSession user = (WechatUserSession) session.getAttribute("user");
		if (user == null) {
			model.addObject("isMe", false);
			model.addObject("isVote", false);
		} else {
			String sessionAccountId = user.getAccountId();
			if (sessionAccountId.equals(ekey)) {
				model.addObject("isMe", true);
			} else { // another user
				model.addObject("isMe", false);

				if (talentService.isVote(ekey, sessionAccountId)) {
					model.addObject("isVote", true);
					if (talentService.isJoin(sessionAccountId)) {
						model.addObject("isJoin", true);
					} else {
						model.addObject("isJoin", false);
					}
				} else {
					model.addObject("isVote", false);
				}
			}
		}

		TalentScore score = talentService.query(ekey);
		model.addObject("position", score.getPosition());
		model.addObject("voted", score.getVotedNum());
		
		List<TalentInfo> votes = talentService.queryVote(ekey, 1, 50);
		for (TalentInfo talent : votes) {
			AccountBasic voteAccount = supAccountService.queryAccount(talent.getAccountId());
			talent.setNickname(voteAccount.getNickname());
			talent.setAvatarUrl(voteAccount.getAvatarUrl());
		}
		model.addObject("votes", votes);
		
		return model;
	}

	private final static Logger logger = LoggerFactory.getLogger(TalentProfileViewController.class);
}
