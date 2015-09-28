package com.redoct.ga.web.talent.mvc.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ActivityInviteViewController
{
	@RequestMapping(value = "/activity-invite", method = RequestMethod.GET)
	public ModelAndView getPages()
	{
		ModelAndView model = new ModelAndView("activity-invite");
		return model;
	}
}
