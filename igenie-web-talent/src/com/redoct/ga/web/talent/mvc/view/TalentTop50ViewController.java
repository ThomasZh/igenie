package com.redoct.ga.web.talent.mvc.view;

import java.io.IOException;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.talent.TalentInfo;
import com.oct.ga.talent.GaTalentService;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.web.talent.ApplicationContextProvider;

@Controller
public class TalentTop50ViewController
{
	@RequestMapping(value = "/talent-top50", method = RequestMethod.GET)
	public ModelAndView getPages()
			throws IOException, InterruptedException
	{
		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		SupAccountService supAccountService = (SupAccountService) ctx.getBean("supAccountService");
		GaTalentService talentService = (GaTalentService) ctx.getBean("gaTalentService");

		List<TalentInfo> talents = talentService.query(1, 50);
		for (TalentInfo talent : talents) {
			AccountBasic account = supAccountService.queryAccount(talent.getAccountId());
			talent.setNickname(account.getNickname());
			talent.setAvatarUrl(account.getAvatarUrl());
		}

		ModelAndView model = new ModelAndView("talent-top50");
		model.addObject("talents", talents);
		return model;
	}
}
