package com.oct.ga.admin.mvc;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.invite.domain.GaInviteMasterInfo;
import com.oct.ga.service.GaInviteService;
import com.redoct.ga.sup.account.SupAccountService;

@Controller
public class IndexPhpViewController
{
	@RequestMapping("/index.php")
	public ModelAndView getPages(HttpServletRequest request)
			throws UnsupportedEncodingException
	{
		request.setCharacterEncoding("UTF-8");// ���ÿͻ�������������������"UTF-8"�ַ����

		String ekey = request.getParameter("ekey");
		logger.debug("ekey:" + ekey);

		ModelAndView model = new ModelAndView();
		model.setViewName("/invite/friend");
		model.addObject("ekey", ekey);

		try {
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			GaInviteService inviteService = (GaInviteService) ctx.getBean("gaInviteService");
			SupAccountService accountService = (SupAccountService) ctx.getBean("supAccountService");

			GaInviteMasterInfo invite = inviteService.queryMaster(ekey);
			if (invite.getInviteType() == GlobalArgs.INVITE_TYPE_REGISTER_AND_FOLLOW_BY_KEY) {
				if (invite.getInviteId() != null && invite.getInviteId().length() > 0) { // exist
					model.addObject("rs", "ok");

					String fromAccountId = invite.getFromAccountId();
					// id, nickname, imageUrl
					AccountMaster friendInfo = accountService.queryAccountMaster(fromAccountId);

					model.addObject("friendId", fromAccountId);
					model.addObject("friendName", friendInfo.getNickname());
					model.addObject("friendImageUrl", friendInfo.getAvatarUrl());
					logger.debug("friendId: " + fromAccountId);
					logger.debug("friendName: " + friendInfo.getNickname());
					logger.debug("friendImageUrl: " + friendInfo.getAvatarUrl());
				} else {
					model.addObject("rs", "notExist");
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addObject("rs", "notExist");
		}

		return model;
	}

	private final static Logger logger = LoggerFactory.getLogger(IndexPhpViewController.class);

}
